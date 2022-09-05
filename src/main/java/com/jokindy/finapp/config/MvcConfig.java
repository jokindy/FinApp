package com.jokindy.finapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("").setViewName("main/index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/menu").setViewName("main/menu");
        registry.addViewController("/about").setViewName("main/about");
        registry.addViewController("/contact").setViewName("main/contact");
    }
}