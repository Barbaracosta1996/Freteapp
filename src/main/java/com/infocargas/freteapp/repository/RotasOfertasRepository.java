package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.RotasOfertas;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoCarga;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the RotasOfertas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RotasOfertasRepository extends JpaRepository<RotasOfertas, Long>, JpaSpecificationExecutor<RotasOfertas> {
    List<RotasOfertas> findByOfertasPerfilIdIsNotAndOfertasTipoOfertaIsAndOfertasStatusIs(Long perfilId, TipoOferta tipoOferta, StatusOferta statusOferta);

    Optional<RotasOfertas> findByOfertasId(Long ofertasId);
}
