package com.sim.shopping.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置类
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
     * @param registry registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
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
