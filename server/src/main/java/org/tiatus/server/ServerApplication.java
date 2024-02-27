package org.tiatus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.tiatus.entity.User;
import org.tiatus.service.UserService;

@SpringBootApplication
@ComponentScan(basePackages = "org.tiatus.*")
@EnableJpaRepositories("org.tiatus.*")
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@EntityScan("org.tiatus.*")
@EnableCaching
public class ServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);
        Environment environment = context.getEnvironment();
        String adminUser = null;
        String adminPwd = null;
        
        for (String arg : args) {
            if (arg.startsWith("admin.user=")){
                String[] s = arg.split("admin.user=");
                if (s.length > 1) {
                    adminUser = s[1];
                }
            }
            if (arg.startsWith("admin.pwd=")){
                String[] s = arg.split("admin.pwd=");
                if (s.length > 1) {
                    adminPwd = s[1];
                }
            }
        }

        if (environment.containsProperty("ADMIN_USER")) {
            String arg = environment.getProperty("ADMIN_USER");
            if (arg != null && arg.startsWith("admin.user=")){
                String[] s = arg.split("admin.user=");
                if (s.length > 1) {
                    adminUser = s[1];
                }
            }
        }

        if (environment.containsProperty("ADMIN_USER_PWD")) {
            String arg = environment.getProperty("ADMIN_USER_PWD");
            if (arg != null && arg.startsWith("admin.pwd=")){
                String[] s = arg.split("admin.pwd=");
                if (s.length > 1) {
                    adminPwd = s[1];
                }
            }
        }

        if (adminUser != null && adminPwd != null) {
            User user = new User();
            user.setUserName(adminUser);
            user.setPassword(adminPwd);
            try {
                context.getBean(UserService.class).addAdminUser(user, null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
