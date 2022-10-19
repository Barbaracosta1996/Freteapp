package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.Solicitacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Solicitacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>, JpaSpecificationExecutor<Solicitacao> {
    List<Solicitacao> findByOfertasPerfilId(Long perfilId);
}
