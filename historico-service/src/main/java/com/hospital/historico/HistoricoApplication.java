package com.hospital.historico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hospital.historico", "com.hospital.common"})
public class HistoricoApplication {
    public static void main(String[] args) {
        SpringApplication.run(HistoricoApplication.class, args);
    }
}