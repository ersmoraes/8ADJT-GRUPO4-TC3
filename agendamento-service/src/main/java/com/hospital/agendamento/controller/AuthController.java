package com.hospital.agendamento.controller;

import com.hospital.agendamento.dto.JwtResponse;
import com.hospital.agendamento.dto.LoginRequest;
import com.hospital.agendamento.model.Usuario;
import com.hospital.agendamento.repository.UsuarioRepository;
import com.hospital.agendamento.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            
            Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            return ResponseEntity.ok(new JwtResponse(
                token, 
                usuario.getUsername(), 
                usuario.getNome(), 
                usuario.getTipoUsuario().getRole()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.badRequest().body("Token inválido");
        }
        
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        return ResponseEntity.ok(usuario);
    }
}