package org.poolc.api.book.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BookWebMvcConfig implements WebMvcConfigurer {
    private final BookBearerAuthInterceptor bearerAuthInterceptor;

    public BookWebMvcConfig(BookBearerAuthInterceptor bearerAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/book");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/book/**");

    }
}