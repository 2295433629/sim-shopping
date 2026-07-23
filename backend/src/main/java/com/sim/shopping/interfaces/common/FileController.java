package com.sim.shopping.interfaces.common;

import com.sim.shopping.infrastructure.aop.Log;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.infrastructure.storage.LocalFileStorage;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 文件上传控制器，处理图片等文件的上传，支持公共文件与敏感文件分离
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
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );
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

        String contentType = file.getContentType();
        if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            return ApiResponse.error(400, "不支持的文件类型: " + contentType);
        }

        try {
            String accessType = "license".equals(type) ? "private" : "public";
            String url = localFileStorage.save(file, accessType);
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

    /**
     * 下载私有文件（敏感文件需登录后访问）
     * @param year 年
     * @param month 月
     * @param day 日
     * @param filename 文件名
     * @param request request
     * @return 文件资源
     */
    @GetMapping("/file/private/{year}/{month}/{day}/{filename}")
    public ResponseEntity<Resource> downloadPrivateFile(
            @PathVariable String year,
            @PathVariable String month,
            @PathVariable String day,
            @PathVariable String filename,
            HttpServletRequest request) {

        // 确保用户已登录（任意角色均可查看自己的敏感文件）
        try {
            SecurityUtils.getCurrentUserId();
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }

        String relativePath = year + "/" + month + "/" + day + "/" + filename;
        Path filePath = localFileStorage.resolvePrivateFile(relativePath);
        java.io.File file = filePath.toFile();

        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build();
        }

        // 防止路径遍历攻击
        try {
            Path uploadDir = localFileStorage.resolvePrivateFile("").normalize();
            Path target = filePath.normalize();
            if (!target.startsWith(uploadDir)) {
                return ResponseEntity.status(403).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }

        Resource resource = new FileSystemResource(file);
        String contentType = determineContentType(request, file);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }

    private String determineContentType(HttpServletRequest request, java.io.File file) {
        String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }
}
