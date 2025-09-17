package com.hospital.historico.graphql;

import com.hospital.historico.model.HistoricoConsulta;
import com.hospital.historico.service.HistoricoService;
import com.hospital.common.enums.StatusConsulta;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConsultaQueryResolver implements GraphQLQueryResolver {
    
    @Autowired
    private HistoricoService historicoService;
    
    public List<HistoricoConsulta> consultasPorPaciente(Long pacienteId) {
        return historicoService.buscarConsultasPorPaciente(pacienteId);
    }
    
    public List<HistoricoConsulta> consultasPorMedico(Long medicoId) {
        return historicoService.buscarConsultasPorMedico(medicoId);
    }
    
    public List<HistoricoConsulta> consultasPorStatus(StatusConsulta status) {
        return historicoService.buscarConsultasPorStatus(status);
    }
    
    public List<HistoricoConsulta> consultasFuturasPorPaciente(Long pacienteId) {
        return historicoService.buscarConsultasFuturasPorPaciente(pacienteId);
    }
    
    public List<HistoricoConsulta> consultasPorPeriodo(String dataInicio, String dataFim) {
        LocalDateTime inicio = LocalDateTime.parse(dataInicio);
        LocalDateTime fim = LocalDateTime.parse(dataFim);
        return historicoService.buscarConsultasPorPeriodo(inicio, fim);
    }
    
    public HistoricoConsulta consultaPorId(Long consultaId) {
        return historicoService.buscarConsultaPorId(consultaId);
    }
    
    public List<HistoricoConsulta> todasConsultas() {
        return historicoService.buscarTodasConsultas();
    }
}