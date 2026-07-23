package com.sim.shopping.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
/**
 * LocalFileStorage，支持公共文件和私有文件分离存储
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Component
public class LocalFileStorage {

    private static final Logger log = LoggerFactory.getLogger(LocalFileStorage.class);

    private final String uploadPath;
    private final String urlPrefix;

    public LocalFileStorage(
            @Value("${app.upload.path}") String uploadPath,
            @Value("${app.upload.url-prefix}") String urlPrefix
    ) {
        this.uploadPath = uploadPath;
        this.urlPrefix = urlPrefix;
    }

    /**
     * 保存公共文件（可直接通过URL访问）
     * @param file file
     * @return 返回结果
     */
    public String save(MultipartFile file) throws IOException {
        return save(file, "public");
    }

    /**
     * 保存文件，根据访问级别存入不同子目录
     * @param file file
     * @param accessType 访问级别：public（公开）或 private（需鉴权）
     * @return 公开文件返回直接URL，私有文件返回控制器代理路径
     */
    public String save(MultipartFile file, String accessType) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件不能为空");
        }

        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String subDir = "private".equals(accessType) ? "private" : "public";
        String dirPath = uploadPath + File.separator + subDir + File.separator + dateDir;
        Path dir = Paths.get(dirPath);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFilename = UUID.randomUUID().toString().replace("-", "") + extension;

        Path filePath = dir.resolve(newFilename);
        file.transferTo(filePath.toFile());

        String url;
        if ("private".equals(accessType)) {
            url = "/api/common/file/private/" + dateDir + "/" + newFilename;
        } else {
            url = urlPrefix + "/public/" + dateDir + "/" + newFilename;
        }
        log.info("File saved [{}]: {}", accessType, url);
        return url;
    }

    /**
     * 获取私有文件在服务器上的绝对路径
     * @param relativePath 相对路径（格式：yyyy/MM/dd/filename）
     * @return 绝对路径
     */
    public Path resolvePrivateFile(String relativePath) {
        String location = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
        return Paths.get(location + "private/" + relativePath);
    }

}
