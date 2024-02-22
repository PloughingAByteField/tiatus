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
        //admin
        registry
            .addViewController("/admin")
            .setViewName("forward:/admin/index.html");
        registry
            .addViewController("/admin/")
            .setViewName("forward:/admin/index.html");
        registry
            .addViewController("/admin/{path2:[\\w]+}")
            .setViewName("forward:/admin/index.html");
        registry
            .addViewController("/admin/{path2}/{path3:[\\w]+}")
            .setViewName("forward:/admin/index.html");
        registry
            .addViewController("/admin/{path2}/{path3}/{path4:[\\w]+}")
            .setViewName("forward:/admin/index.html");
        registry
            .addViewController("/admin/{path2}/{path3}/{path4}/{path5:[\\w]+}")
            .setViewName("forward:/admin/index.html");

        // adjudicator
        registry
            .addViewController("/adjudicator")
            .setViewName("forward:/adjudicator/index.html");
        registry
            .addViewController("/adjudicator/")
            .setViewName("forward:/adjudicator/index.html");
        registry
            .addViewController("/adjudicator/{path2:[\\w]+}")
            .setViewName("forward:/adjudicator/index.html");
        registry
            .addViewController("/adjudicator/{path2}/{path3:[\\w]+}")
            .setViewName("forward:/adjudicator/index.html");
        registry
            .addViewController("/adjudicator/{path2}/{path3}/{path4:[\\w]+}")
            .setViewName("forward:/adjudicator/index.html");
        registry
            .addViewController("/adjudicator/{path2}/{path3}/{path4}/{path5:[\\w]+}")
            .setViewName("forward:/adjudicator/index.html");
        
        // timing
        registry
            .addViewController("/timing")
            .setViewName("forward:/timing/index.html");
        registry
            .addViewController("/timing/")
            .setViewName("forward:/timing/index.html");
        registry
            .addViewController("/timing/{path2:[\\w]+}")
            .setViewName("forward:/timing/index.html");
        registry
            .addViewController("/timing/{path2}/{path3:[\\w]+}")
            .setViewName("forward:/timing/index.html");
        registry
            .addViewController("/timing/{path2}/{path3}/{path4:[\\w]+}")
            .setViewName("forward:/timing/index.html");
        registry
            .addViewController("/timing/{path2}/{path3}/{path4}/{path5:[\\w]+}")
            .setViewName("forward:/timing/index.html");
                
        // results
        registry
            .addViewController("/results")
            .setViewName("forward:/results/index.html");
        registry
            .addViewController("/results/")
            .setViewName("forward:/results/index.html");
        registry
            .addViewController("/results/{path2:[\\w]+}")
            .setViewName("forward:/results/index.html");
        registry
            .addViewController("/results/{path2}/{path3:[\\w]+}")
            .setViewName("forward:/results/index.html");
        registry
            .addViewController("/results/{path2}/{path3}/{path4:[\\w]+}")
            .setViewName("forward:/results/index.html");
        registry
            .addViewController("/results/{path2}/{path3}/{path4}/{path5:[\\w]+}")
            .setViewName("forward:/results/index.html");

    }

    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}
