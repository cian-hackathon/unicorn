package com.thelads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UnicornApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnicornApplication.class, args);
    }
}
