package com.website;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.config.filter.ExceptionLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManager;
import javax.servlet.DispatcherType;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("errors", "messages");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource());
        return localValidatorFactoryBean;
    }

    //
    //@Override
    //public void addInterceptors(InterceptorRegistry registry) {
    //    registry.addInterceptor(new LoginCheckInterceptor())
    //            .excludePathPatterns()
    //            .addPathPatterns()
    //            .order(1);
    //}

    //@Bean
    //public FilterRegistrationBean<ExceptionLogFilter> filterRegistrationBean() {
    //    FilterRegistrationBean<ExceptionLogFilter> registry = new FilterRegistrationBean(new ExceptionLogFilter());
    //    registry.setOrder(1);
    //    registry.addUrlPatterns("/*");
    //    registry.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
    //    return registry;
    //}
}
