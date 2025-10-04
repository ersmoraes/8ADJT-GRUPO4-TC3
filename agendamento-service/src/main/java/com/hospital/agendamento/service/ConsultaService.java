package com.hospital.agendamento.service;

import com.hospital.agendamento.dto.ConsultaRequest;
import com.hospital.agendamento.model.Consulta;
import com.hospital.agendamento.model.Usuario;
import com.hospital.agendamento.repository.ConsultaRepository;
import com.hospital.agendamento.repository.UsuarioRepository;
import com.hospital.common.dto.ConsultaDTO;
import com.hospital.common.enums.StatusConsulta;
import com.hospital.common.enums.TipoUsuario;
import com.hospital.common.events.ConsultaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ConsultaService {
    
    @Autowired
    private ConsultaRepository consultaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RabbitMQService rabbitMQService;
    
    public Consulta criarConsulta(ConsultaRequest request, String usernameLogado) {
        // Validar se o usuário logado tem permissão
        Usuario usuarioLogado = usuarioRepository.findByUsername(usernameLogado)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        if (usuarioLogado.getTipoUsuario() != TipoUsuario.MEDICO && 
            usuarioLogado.getTipoUsuario() != TipoUsuario.ENFERMEIRO) {
            throw new RuntimeException("Sem permissão para criar consultas");
        }
        
        // Buscar paciente e médico
        Usuario paciente = usuarioRepository.findById(request.getPacienteId())
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            
        Usuario medico = usuarioRepository.findById(request.getMedicoId())
            .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
            
        if (paciente.getTipoUsuario() != TipoUsuario.PACIENTE) {
            throw new RuntimeException("Usuário selecionado não é um paciente");
        }
        
        if (medico.getTipoUsuario() != TipoUsuario.MEDICO) {
            throw new RuntimeException("Usuário selecionado não é um médico");
        }
        
        // Validar se a data não é no passado
        if (request.getDataHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível agendar consulta no passado");
        }
        
        // Criar consulta
        Consulta consulta = new Consulta(paciente, medico, request.getDataHora(), request.getObservacoes());
        if (request.getStatus() != null) {
            consulta.setStatus(request.getStatus());
        }
        
        consulta = consultaRepository.save(consulta);
        
        // Enviar evento para o RabbitMQ
        enviarEventoConsulta("CRIADA", consulta);
        
        return consulta;
    }
    
    public Consulta atualizarConsulta(Long id, ConsultaRequest request, String usernameLogado) {
        Usuario usuarioLogado = usuarioRepository.findByUsername(usernameLogado)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        if (usuarioLogado.getTipoUsuario() != TipoUsuario.MEDICO && 
            usuarioLogado.getTipoUsuario() != TipoUsuario.ENFERMEIRO) {
            throw new RuntimeException("Sem permissão para editar consultas");
        }
        
        Consulta consulta = consultaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
            
        // Atualizar campos se fornecidos
        if (request.getDataHora() != null) {
            if (request.getDataHora().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Não é possível agendar consulta no passado");
            }
            consulta.setDataHora(request.getDataHora());
        }
        
        if (request.getObservacoes() != null) {
            consulta.setObservacoes(request.getObservacoes());
        }
        
        if (request.getStatus() != null) {
            consulta.setStatus(request.getStatus());
        }

        consulta.setDataAtualizacao(LocalDateTime.now());

        consulta = consultaRepository.save(consulta);
        
        // Enviar evento para o RabbitMQ
        enviarEventoConsulta("EDITADA", consulta);
        
        return consulta;
    }
    
    public List<Consulta> listarConsultasPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        switch (usuario.getTipoUsuario()) {
            case PACIENTE:
                return consultaRepository.findByPacienteId(usuario.getId());
            case MEDICO:
                return consultaRepository.findByMedicoId(usuario.getId());
            case ENFERMEIRO:
                // Enfermeiros podem ver todas as consultas
                return consultaRepository.findAll();
            default:
                throw new RuntimeException("Tipo de usuário não reconhecido");
        }
    }
    
    public List<Consulta> listarConsultasFuturas(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        LocalDateTime agora = LocalDateTime.now();
        
        switch (usuario.getTipoUsuario()) {
            case PACIENTE:
                return consultaRepository.findConsultasFuturasPorPaciente(usuario.getId(), agora);
            case MEDICO:
                return consultaRepository.findConsultasFuturasPorMedico(usuario.getId(), agora);
            case ENFERMEIRO:
                // Enfermeiros podem ver todas as consultas futuras
                return consultaRepository.findConsultasPorPeriodo(agora, agora.plusYears(1));
            default:
                throw new RuntimeException("Tipo de usuário não reconhecido");
        }
    }
    
    public Consulta obterConsultaPorId(Long id, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        Consulta consulta = consultaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
            
        // Verificar se o usuário tem permissão para ver esta consulta
        switch (usuario.getTipoUsuario()) {
            case PACIENTE:
                if (!consulta.getPaciente().getId().equals(usuario.getId())) {
                    throw new RuntimeException("Sem permissão para acessar esta consulta");
                }
                break;
            case MEDICO:
                if (!consulta.getMedico().getId().equals(usuario.getId())) {
                    throw new RuntimeException("Sem permissão para acessar esta consulta");
                }
                break;
            case ENFERMEIRO:
                // Enfermeiros podem ver qualquer consulta
                break;
            default:
                throw new RuntimeException("Tipo de usuário não reconhecido");
        }
        
        return consulta;
    }
    
    public void cancelarConsulta(Long id, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        if (usuario.getTipoUsuario() != TipoUsuario.MEDICO && 
            usuario.getTipoUsuario() != TipoUsuario.ENFERMEIRO) {
            throw new RuntimeException("Sem permissão para cancelar consultas");
        }
        
        Consulta consulta = consultaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
            
        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
        
        // Enviar evento para o RabbitMQ
        enviarEventoConsulta("CANCELADA", consulta);
    }
    
    private void enviarEventoConsulta(String tipoEvento, Consulta consulta) {
        ConsultaDTO consultaDTO = new ConsultaDTO(
            consulta.getId(),
            consulta.getPaciente().getId(),
            consulta.getMedico().getId(),
            consulta.getDataHora(),
            consulta.getObservacoes(),
            consulta.getStatus()
        );
        
        ConsultaEvent evento = new ConsultaEvent(
            tipoEvento,
            consultaDTO,
            consulta.getPaciente().getEmail(),
            consulta.getPaciente().getNome()
        );
        
        rabbitMQService.enviarEventoConsulta(evento);
    }
}