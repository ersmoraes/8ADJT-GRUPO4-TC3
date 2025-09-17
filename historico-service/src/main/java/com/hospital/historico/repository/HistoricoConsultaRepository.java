package com.hospital.historico.repository;

import com.hospital.historico.model.HistoricoConsulta;
import com.hospital.common.enums.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricoConsultaRepository extends JpaRepository<HistoricoConsulta, Long> {
    
    List<HistoricoConsulta> findByPacienteId(Long pacienteId);
    
    List<HistoricoConsulta> findByMedicoId(Long medicoId);
    
    List<HistoricoConsulta> findByStatus(StatusConsulta status);
    
    @Query("SELECT h FROM HistoricoConsulta h WHERE h.pacienteId = :pacienteId AND h.dataHora >= :dataInicio")
    List<HistoricoConsulta> findConsultasFuturasPorPaciente(@Param("pacienteId") Long pacienteId, 
                                                           @Param("dataInicio") LocalDateTime dataInicio);
    
    @Query("SELECT h FROM HistoricoConsulta h WHERE h.dataHora BETWEEN :dataInicio AND :dataFim")
    List<HistoricoConsulta> findConsultasPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio,
                                                   @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT h FROM HistoricoConsulta h WHERE h.pacienteId = :pacienteId ORDER BY h.dataHora DESC")
    List<HistoricoConsulta> findHistoricoPorPacienteOrdenado(@Param("pacienteId") Long pacienteId);
}