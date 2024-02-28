package com.heeju.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // This is where the basic settings for MVC settings are set. This part can be defined in advance in Spring and
    // used as is, but it can also be used by overriding and modifying the part.
    @Value("${uploadPath}") // Read the “uploadPath” property value set in application.properties.
    String uploadPath;

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**")   // If the URL entered in the web browser starts with
                                                               // /images, the file is read based on the folder set
                                                               // in uploadPath.
                                                               // * matches zero or more characters
                                                               // ** matches zero or more 'directories' in a path
                                                               // The /* pattern is mapped to "/, /aaa, /bbb", etc.,
                                                               // but "/aaa/bbb" is not mapped to /*, but is mapped to /**.
                .addResourceLocations(uploadPath);  // Set the root path to read files saved on the local computer.
    }

}
