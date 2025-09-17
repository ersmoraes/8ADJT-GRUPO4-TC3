package com.hospital.agendamento.dto;

public class JwtResponse {
    private String token;
    private String tipo = "Bearer";
    private String username;
    private String nome;
    private String role;
    
    // Construtores
    public JwtResponse(String token, String username, String nome, String role) {
        this.token = token;
        this.username = username;
        this.nome = nome;
        this.role = role;
    }
}