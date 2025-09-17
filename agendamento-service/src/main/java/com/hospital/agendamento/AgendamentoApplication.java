package com.hospital.agendamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hospital.agendamento", "com.hospital.common"})
public class AgendamentoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgendamentoApplication.class, args);
    }
}