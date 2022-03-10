package com.example.chatting_software.comfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/register.html").setViewName("register");
        registry.addViewController("/ChangeUserName.html").setViewName("ChangeUserName");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerIntercepter()).
                addPathPatterns("/**").
                excludePathPatterns("/index.html",
                        "/",
                        "/user/login",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/register.html",
                        "/user/gotoRegister",
                        "/user/register");
    }
}
