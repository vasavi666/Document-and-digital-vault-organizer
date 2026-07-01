package com.smartvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SmartVaultApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartVaultApplication.class, args);
    }
}
