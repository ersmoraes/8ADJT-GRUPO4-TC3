package com.hospital.agendamento.repository;

import com.hospital.agendamento.model.Consulta;
import com.hospital.common.enums.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    
    List<Consulta> findByPacienteId(Long pacienteId);
    
    List<Consulta> findByMedicoId(Long medicoId);
    
    List<Consulta> findByStatus(StatusConsulta status);
    
    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId AND c.dataHora >= :dataInicio")
    List<Consulta> findConsultasFuturasPorPaciente(@Param("pacienteId") Long pacienteId, 
                                                   @Param("dataInicio") LocalDateTime dataInicio);
    
    @Query("SELECT c FROM Consulta c WHERE c.medico.id = :medicoId AND c.dataHora >= :dataInicio")
    List<Consulta> findConsultasFuturasPorMedico(@Param("medicoId") Long medicoId, 
                                                @Param("dataInicio") LocalDateTime dataInicio);
    
    @Query("SELECT c FROM Consulta c WHERE c.dataHora BETWEEN :dataInicio AND :dataFim")
    List<Consulta> findConsultasPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio,
                                          @Param("dataFim") LocalDateTime dataFim);
}