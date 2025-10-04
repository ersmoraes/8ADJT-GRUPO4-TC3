package com.hospital.agendamento.model;

import com.hospital.common.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "usuarios")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(nullable = false)
    private Boolean ativo = true;

    // Construtores
    public Usuario() {
    }

    public Usuario(String username, String password, String nome, String email, TipoUsuario tipoUsuario) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }
}