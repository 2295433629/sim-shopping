package com.sim.shopping.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置类，公开上传目录（产品图、头像等公共图片），私有文件通过控制器鉴权
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.path}")
    private String uploadPath;

    @Value("${app.upload.url-prefix}")
    private String urlPrefix;

    /**
     * 添加Resource Handlers
     * 1. /uploads/public/**  新公开文件
     * 2. /uploads/**         兼容旧文件路径（历史文件直接存放在uploads/yyyy/MM/dd/下）
     * /uploads/private/** 虽然被此映射覆盖，但SecurityConfig已拦截需要认证
     * @param registry registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
        // 新公开文件路径（更具体的路径优先匹配）
        registry.addResourceHandler(urlPrefix + "/public/**")
                .addResourceLocations("file:" + location + "public/");
        // 兼容旧文件路径
        registry.addResourceHandler(urlPrefix + "/**")
                .addResourceLocations("file:" + location);
    }

    /**
     * 获取Upload Path
     * @return 返回结果
     */
    public String getUploadPath() { return this.uploadPath; }
    /**
     * set Upload Path
     * @param uploadPath uploadPath
     */
    public void setUploadPath(String uploadPath) { this.uploadPath = uploadPath; }
    /**
     * 获取Url Prefix
     * @return 返回结果
     */
    public String getUrlPrefix() { return this.urlPrefix; }
    /**
     * set Url Prefix
     * @param urlPrefix urlPrefix
     */
    public void setUrlPrefix(String urlPrefix) { this.urlPrefix = urlPrefix; }
}
