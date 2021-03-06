package com.im.chat.config;


import com.im.chat.config.arguementresolver.CurrentUserMethodArgumentResolver;
import com.im.chat.config.interceptor.ContextInformationInterceptor;
import com.im.chat.config.interceptor.CorsInterceptor;
import com.im.chat.config.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MyWebMvcConfigure implements WebMvcConfigurer
{

//    @Value("${project.imageslocation}")
//    private String imagesLocation;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry)
//    {
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations(imagesLocation);
//    }
    @Autowired
    private LoginInterceptor loginInterceptor;


    @Autowired
    private ContextInformationInterceptor contextInformationInterceptor;

    @Autowired
    private CorsInterceptor corsInterceptor;


    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

            registry.addInterceptor(contextInformationInterceptor)
                    .addPathPatterns("/chat/**");
            registry.addInterceptor(loginInterceptor)
                    .addPathPatterns("/chat/**")
                    .excludePathPatterns("/user/unlogin");
        registry.addInterceptor(corsInterceptor)
                    .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver());
    }

    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }
}
