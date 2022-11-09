package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.RotasOfertas;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoCarga;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import feign.Param;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RotasOfertas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RotasOfertasRepository extends JpaRepository<RotasOfertas, Long>, JpaSpecificationExecutor<RotasOfertas> {
    @Query(
        "SELECT o FROM RotasOfertas o WHERE NOT o.ofertas.perfil.id = ?1 " +
        "AND o.ofertas.tipoOferta = ?2 " +
        "AND o.ofertas.status = ?3 " +
        "AND o.ofertas.dataFechamento >= ?4 "
    )
    List<RotasOfertas> allOfertasAvaiable(Long perfilId, TipoOferta tipoOferta, StatusOferta statusOferta, ZonedDateTime date);

    Optional<RotasOfertas> findByOfertasId(Long ofertasId);

    List<RotasOfertas> findAllByOfertasStatusAndOfertasDataFechamentoGreaterThanEqual(StatusOferta statusOferta, ZonedDateTime dateMax);

    List<RotasOfertas> findRotasOfertasByOfertasStatusAndOfertasDataFechamentoLessThanEqual(StatusOferta statusOferta, ZonedDateTime date);
}
