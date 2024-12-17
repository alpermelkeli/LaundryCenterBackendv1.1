package org.alpermelkeli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LaundryCenterApiApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LaundryCenterApiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(LaundryCenterApiApplication.class, args);
    }
}

