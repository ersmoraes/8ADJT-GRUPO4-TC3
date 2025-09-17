package com.hospital.agendamento.security;

import com.hospital.agendamento.model.Usuario;
import com.hospital.agendamento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        
        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getAtivo(),
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority(usuario.getTipoUsuario().getRole()))
        );
    }
}