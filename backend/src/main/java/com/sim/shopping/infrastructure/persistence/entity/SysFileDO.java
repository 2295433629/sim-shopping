package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_file")
public class SysFileDO extends BaseEntity {
    private String originalName;
    private String fileUrl;
    private String fileName;
    private String uploaderType;
    private String fileCategory;
    private String filePath;
    private String fileType;
    private Long uploaderId;
    private Long fileSize;

    public String getOriginalName() { return this.originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }
    public String getFileUrl() { return this.fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getFileName() { return this.fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getUploaderType() { return this.uploaderType; }
    public void setUploaderType(String uploaderType) { this.uploaderType = uploaderType; }
    public String getFileCategory() { return this.fileCategory; }
    public void setFileCategory(String fileCategory) { this.fileCategory = fileCategory; }
    public String getFilePath() { return this.filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getFileType() { return this.fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public Long getUploaderId() { return this.uploaderId; }
    public void setUploaderId(Long uploaderId) { this.uploaderId = uploaderId; }
    public Long getFileSize() { return this.fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
}
