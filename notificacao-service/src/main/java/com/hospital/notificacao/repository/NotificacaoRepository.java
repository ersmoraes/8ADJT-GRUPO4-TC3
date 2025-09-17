package com.hospital.notificacao.repository;

import com.hospital.notificacao.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    
    Optional<Notificacao> findByConsultaIdAndTipoEvento(Long consultaId, String tipoEvento);
    
    List<Notificacao> findByConsultaId(Long consultaId);
    
    List<Notificacao> findByPacienteId(Long pacienteId);
    
    List<Notificacao> findByEmailEnviadoFalse();
    
    @Query("SELECT n FROM Notificacao n WHERE n.dataConsulta BETWEEN :amanha AND :doisDias AND n.emailEnviado = false")
    List<Notificacao> findNotificacoesParaLembrete(@Param("amanha") LocalDateTime amanha, 
                                                   @Param("doisDias") LocalDateTime doisDias);
}