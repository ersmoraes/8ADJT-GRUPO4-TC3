package com.hospital.common.enums;

public enum TipoUsuario {
    MEDICO("ROLE_MEDICO"),
    ENFERMEIRO("ROLE_ENFERMEIRO"),
    PACIENTE("ROLE_PACIENTE");
    
    private final String role;
    
    TipoUsuario(String role) {
        this.role = role;
    }
    
    public String getRole() {
        return role;
    }
}