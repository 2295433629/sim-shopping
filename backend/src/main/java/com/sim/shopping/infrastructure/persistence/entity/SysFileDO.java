package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysFile数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取Original Name
     * @return 返回结果
     */
    public String getOriginalName() { return this.originalName; }
    /**
     * set Original Name
     * @param originalName originalName
     */
    public void setOriginalName(String originalName) { this.originalName = originalName; }
    /**
     * 获取File Url
     * @return 返回结果
     */
    public String getFileUrl() { return this.fileUrl; }
    /**
     * set File Url
     * @param fileUrl fileUrl
     */
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    /**
     * 获取File Name
     * @return 返回结果
     */
    public String getFileName() { return this.fileName; }
    /**
     * set File Name
     * @param fileName fileName
     */
    public void setFileName(String fileName) { this.fileName = fileName; }
    /**
     * 获取Uploader Type
     * @return 返回结果
     */
    public String getUploaderType() { return this.uploaderType; }
    /**
     * set Uploader Type
     * @param uploaderType uploaderType
     */
    public void setUploaderType(String uploaderType) { this.uploaderType = uploaderType; }
    /**
     * 获取File Category
     * @return 返回结果
     */
    public String getFileCategory() { return this.fileCategory; }
    /**
     * set File Category
     * @param fileCategory fileCategory
     */
    public void setFileCategory(String fileCategory) { this.fileCategory = fileCategory; }
    /**
     * 获取File Path
     * @return 返回结果
     */
    public String getFilePath() { return this.filePath; }
    /**
     * set File Path
     * @param filePath filePath
     */
    public void setFilePath(String filePath) { this.filePath = filePath; }
    /**
     * 获取File Type
     * @return 返回结果
     */
    public String getFileType() { return this.fileType; }
    /**
     * set File Type
     * @param fileType fileType
     */
    public void setFileType(String fileType) { this.fileType = fileType; }
    /**
     * 获取Uploader Id
     * @return 返回结果
     */
    public Long getUploaderId() { return this.uploaderId; }
    /**
     * set Uploader Id
     * @param uploaderId uploaderId
     */
    public void setUploaderId(Long uploaderId) { this.uploaderId = uploaderId; }
    /**
     * 获取File Size
     * @return 返回结果
     */
    public Long getFileSize() { return this.fileSize; }
    /**
     * set File Size
     * @param fileSize fileSize
     */
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
}
