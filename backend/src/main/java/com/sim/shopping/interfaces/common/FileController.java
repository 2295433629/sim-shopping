package com.sim.shopping.interfaces.common;

import com.sim.shopping.infrastructure.aop.Log;
import com.sim.shopping.infrastructure.storage.LocalFileStorage;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 文件上传控制器，处理图片等文件的上传
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/common")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private static final Set<String> ALLOWED_TYPES = Set.of("avatar", "product", "review", "banner", "license");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    private final LocalFileStorage localFileStorage;

    public FileController(LocalFileStorage localFileStorage) {
        this.localFileStorage = localFileStorage;
    }

    /**
     * 上传文件
     * @return 返回结果
     */
    @PostMapping("/upload")
    @Log(module = "系统", type = "操作")
    public ApiResponse<Map<String, Object>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "product") String type) {
        
        if (!ALLOWED_TYPES.contains(type)) {
            return ApiResponse.error(400, "不支持的文件类型: " + type);
        }

        if (file.isEmpty()) {
            return ApiResponse.error(400, "文件不能为空");
        }

        if (file.getSize() > MAX_SIZE) {
            return ApiResponse.error(400, "文件大小不能超过5MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return ApiResponse.error(400, "文件名不能为空");
        }

        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return ApiResponse.error(400, "不支持的文件格式: " + extension);
        }

        try {
            String url = localFileStorage.save(file);
            Map<String, Object> result = new HashMap<>();
            result.put("url", url);
            result.put("originalName", originalFilename);
            result.put("size", file.getSize());
            result.put("contentType", file.getContentType());
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ApiResponse.error(500, "文件上传失败: " + e.getMessage());
        }
    }
}
