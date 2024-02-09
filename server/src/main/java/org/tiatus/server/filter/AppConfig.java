package org.tiatus.server.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.Filter;

@Configuration
@EnableTransactionManagement
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin/").setViewName("forward:/admin/index.html");
        registry.addViewController("/adjudicator/").setViewName("forward:/adjudicator/index.html");
        registry.addViewController("/timing/").setViewName("forward:/timing/index.html");
        registry.addViewController("/results/").setViewName("forward:/results/index.html");
    }

    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}
