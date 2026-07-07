package com.sim.shopping.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.path}")
    private String uploadPath;

    @Value("${app.upload.url-prefix}")
    private String urlPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
        registry.addResourceHandler(urlPrefix + "/**")
                .addResourceLocations("file:" + location);
    }

    public String getUploadPath() { return this.uploadPath; }
    public void setUploadPath(String uploadPath) { this.uploadPath = uploadPath; }
    public String getUrlPrefix() { return this.urlPrefix; }
    public void setUrlPrefix(String urlPrefix) { this.urlPrefix = urlPrefix; }
}
