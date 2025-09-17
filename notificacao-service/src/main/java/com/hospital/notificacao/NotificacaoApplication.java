package com.hospital.notificacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.hospital.notificacao", "com.hospital.common"})
public class NotificacaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificacaoApplication.class, args);
    }
}
