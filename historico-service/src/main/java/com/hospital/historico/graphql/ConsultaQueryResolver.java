package com.hospital.historico.graphql;

import com.hospital.common.enums.StatusConsulta;
import com.hospital.historico.model.HistoricoConsulta;
import com.hospital.historico.service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConsultaQueryResolver {
    
    @Autowired
    private HistoricoService historicoService;

    @QueryMapping
    public List<HistoricoConsulta> consultasPorPaciente(Long pacienteId) {
        return historicoService.buscarConsultasPorPaciente(pacienteId);
    }

    @QueryMapping
    public List<HistoricoConsulta> consultasPorMedico(Long medicoId) {
        return historicoService.buscarConsultasPorMedico(medicoId);
    }

    @QueryMapping
    public List<HistoricoConsulta> consultasPorStatus(StatusConsulta status) {
        return historicoService.buscarConsultasPorStatus(status);
    }

    @QueryMapping
    public List<HistoricoConsulta> consultasFuturasPorPaciente(Long pacienteId) {
        return historicoService.buscarConsultasFuturasPorPaciente(pacienteId);
    }

    @QueryMapping
    public List<HistoricoConsulta> consultasPorPeriodo(String dataInicio, String dataFim) {
        LocalDateTime inicio = LocalDateTime.parse(dataInicio);
        LocalDateTime fim = LocalDateTime.parse(dataFim);
        return historicoService.buscarConsultasPorPeriodo(inicio, fim);
    }

    @QueryMapping
    public HistoricoConsulta consultaPorId(Long consultaId) {
        return historicoService.buscarConsultaPorId(consultaId);
    }

    @QueryMapping
    public List<HistoricoConsulta> todasConsultas() {
        return historicoService.buscarTodasConsultas();
    }
}