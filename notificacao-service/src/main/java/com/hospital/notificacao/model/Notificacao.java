package com.hospital.notificacao.model;

import com.hospital.common.enums.StatusConsulta;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "notificacoes")
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long consultaId;
    
    @Column(nullable = false)
    private Long pacienteId;
    
    @Column(nullable = false)
    private String pacienteNome;
    
    @Column(nullable = false)
    private String pacienteEmail;
    
    @Column(nullable = false)
    private LocalDateTime dataConsulta;
    
    @Column(nullable = false, length = 50)
    private String tipoEvento; // CRIADA, EDITADA, CANCELADA
    
    @Enumerated(EnumType.STRING)
    private StatusConsulta statusConsulta;
    
    @Column(nullable = false)
    private Boolean emailEnviado = false;
    
    @Column
    private LocalDateTime dataEnvioEmail;
    
    @Column(length = 500)
    private String mensagemErro;
    
    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    // Construtores
    public Notificacao() {}
    
    public Notificacao(Long consultaId, Long pacienteId, String pacienteNome, 
                      String pacienteEmail, LocalDateTime dataConsulta, String tipoEvento) {
        this.consultaId = consultaId;
        this.pacienteId = pacienteId;
        this.pacienteNome = pacienteNome;
        this.pacienteEmail = pacienteEmail;
        this.dataConsulta = dataConsulta;
        this.tipoEvento = tipoEvento;
    }
}