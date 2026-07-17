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
 * LocalFileStorage
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
     * 保存
     * @param file file
     * @return 返回结果
     */
    public String save(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件不能为空");
        }

        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String dirPath = uploadPath + File.separator + dateDir;
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

        String url = urlPrefix + "/" + dateDir + "/" + newFilename;
        log.info("File saved: {}", url);
        return url;
    }

}
