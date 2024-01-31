package org.tiatus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@ComponentScan(basePackages = "org.tiatus.*")
@EnableJpaRepositories("org.tiatus.*")
@EnableMethodSecurity(securedEnabled = true)
@EntityScan("org.tiatus.*")
public class ServerApplication {

    public static void main(String[] args) {
            SpringApplication.run(ServerApplication.class, args);
    }

}
