package com.xssdhr.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web项目配置类
 * @author xssdhr
 * @site www.dzty.top
 * @company 南京点子跳跃有限公司
 * @create 2022-02-24 11:45
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE","OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Value("${web.avatarImagesFilePath}")
    private String avatarPath;
    @Value("${web.certificateImageFilePath}")
    private String certificateImageFilePath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/image/userAvatar/**").addResourceLocations("file:D:\\WorkSpace\\2023\\userAvatar\\");
        registry.addResourceHandler("/image/userAvatar/**").addResourceLocations("file:"+avatarPath);
        registry.addResourceHandler("/image/certificateImage/**").addResourceLocations("file:"+certificateImageFilePath);
    }

}
