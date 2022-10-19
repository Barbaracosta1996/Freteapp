package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.RotasOfertas;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.RotasOfertasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RotasOfertas} and its DTO {@link RotasOfertasDTO}.
 */
@Mapper(componentModel = "spring")
public interface RotasOfertasMapper extends EntityMapper<RotasOfertasDTO, RotasOfertas> {
    @Mapping(target = "ofertas", source = "ofertas")
    RotasOfertasDTO toDto(RotasOfertas s);

    @Named("ofertasId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OfertasDTO toDtoOfertasId(Ofertas ofertas);
}
