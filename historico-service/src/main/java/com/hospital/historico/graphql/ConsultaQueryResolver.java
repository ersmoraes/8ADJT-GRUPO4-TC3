package com.hospital.historico.graphql;

import com.hospital.common.enums.StatusConsulta;
import com.hospital.historico.model.HistoricoConsulta;
import com.hospital.historico.service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ConsultaQueryResolver {

    @Autowired
    private HistoricoService historicoService;

    @QueryMapping
    public List<HistoricoConsulta> consultasPorPaciente(@Argument Long pacienteId) {
        return historicoService.buscarConsultasPorPaciente(pacienteId);
    }

    @QueryMapping
    public List<HistoricoConsulta> consultasPorMedico(@Argument Long medicoId) {
        return historicoService.buscarConsultasPorMedico(medicoId);
    }

    @QueryMapping
    public List<HistoricoConsulta> consultasPorStatus(@Argument StatusConsulta status) {
        return historicoService.buscarConsultasPorStatus(status);
    }

    @QueryMapping
    public List<HistoricoConsulta> consultasFuturasPorPaciente(@Argument Long pacienteId) {
        return historicoService.buscarConsultasFuturasPorPaciente(pacienteId);
    }

    @QueryMapping
    public List<HistoricoConsulta> consultasPorPeriodo(@Argument String dataInicio, @Argument String dataFim) {
        LocalDateTime inicio = LocalDateTime.parse(dataInicio);
        LocalDateTime fim = LocalDateTime.parse(dataFim);
        return historicoService.buscarConsultasPorPeriodo(inicio, fim);
    }

    @QueryMapping
    public HistoricoConsulta consultaPorId(@Argument Long consultaId) {
        return historicoService.buscarConsultaPorId(consultaId);
    }

    @QueryMapping
    public List<HistoricoConsulta> todasConsultas() {
        return historicoService.buscarTodasConsultas();
    }
}