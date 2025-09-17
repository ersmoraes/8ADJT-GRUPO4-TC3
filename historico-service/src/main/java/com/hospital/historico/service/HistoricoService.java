package com.hospital.historico.service;

import com.hospital.historico.model.HistoricoConsulta;
import com.hospital.historico.repository.HistoricoConsultaRepository;
import com.hospital.common.dto.ConsultaDTO;
import com.hospital.common.enums.StatusConsulta;
import com.hospital.common.events.ConsultaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class HistoricoService {
    
    @Autowired
    private HistoricoConsultaRepository historicoRepository;
    
    public void processarEventoConsulta(ConsultaEvent evento) {
        ConsultaDTO consulta = evento.getConsulta();
        
        // Buscar se já existe registro para esta consulta
        HistoricoConsulta historico = historicoRepository.findById(consulta.getId())
            .orElse(null);
        
        if (historico == null) {
            // Criar novo registro
            historico = new HistoricoConsulta(
                consulta.getId(),
                consulta.getPacienteId(),
                consulta.getMedicoId(),
                evento.getPacienteNome(),
                "Dr. Médico", // TODO: buscar nome do médico
                evento.getPacienteEmail(),
                consulta.getDataHora(),
                consulta.getObservacoes(),
                consulta.getStatus()
            );
        } else {
            // Atualizar registro existente
            historico.setDataHora(consulta.getDataHora());
            historico.setObservacoes(consulta.getObservacoes());
            historico.setStatus(consulta.getStatus());
            historico.setDataAtualizacao(LocalDateTime.now());
        }
        
        historicoRepository.save(historico);
        
        System.out.println("Histórico processado para consulta ID: " + consulta.getId() + 
                          " - Evento: " + evento.getTipoEvento());
    }
    
    public List<HistoricoConsulta> buscarConsultasPorPaciente(Long pacienteId) {
        return historicoRepository.findHistoricoPorPacienteOrdenado(pacienteId);
    }
    
    public List<HistoricoConsulta> buscarConsultasPorMedico(Long medicoId) {
        return historicoRepository.findByMedicoId(medicoId);
    }
    
    public List<HistoricoConsulta> buscarConsultasPorStatus(StatusConsulta status) {
        return historicoRepository.findByStatus(status);
    }
    
    public List<HistoricoConsulta> buscarConsultasFuturasPorPaciente(Long pacienteId) {
        return historicoRepository.findConsultasFuturasPorPaciente(pacienteId, LocalDateTime.now());
    }
    
    public List<HistoricoConsulta> buscarConsultasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return historicoRepository.findConsultasPorPeriodo(dataInicio, dataFim);
    }
    
    public HistoricoConsulta buscarConsultaPorId(Long consultaId) {
        return historicoRepository.findById(consultaId).orElse(null);
    }
    
    public List<HistoricoConsulta> buscarTodasConsultas() {
        return historicoRepository.findAll();
    }
}