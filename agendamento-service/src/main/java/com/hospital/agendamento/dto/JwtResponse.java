package com.hospital.agendamento.dto;

public class JwtResponse {
    private final String token;
    private final String tipo = "Bearer";
    private final String username;
    private final String nome;
    private final String role;
    
    // Construtor
    public JwtResponse(String token, String username, String nome, String role) {
        this.token = token;
        this.username = username;
        this.nome = nome;
        this.role = role;
    }

    // Getters necessários para serialização JSON (Jackson)
    public String getToken() { return token; }
    public String getTipo() { return tipo; }
    public String getUsername() { return username; }
    public String getNome() { return nome; }
    public String getRole() { return role; }
}