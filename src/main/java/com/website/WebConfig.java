package com.website;

import com.website.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /*
        registry.addInterceptor(new LoginCheckInterceptor())
                .excludePathPatterns()
                .addPathPatterns()
                .order(1);
         */
        return;
    }
}
