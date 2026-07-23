package com.sim.shopping.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置类，仅公开公共上传目录，敏感文件通过控制器鉴权访问
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
     * 仅注册公开目录 /uploads/public/**，敏感文件/private/** 不直接暴露
     * @param registry registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
        registry.addResourceHandler(urlPrefix + "/public/**")
                .addResourceLocations("file:" + location + "public/");
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
