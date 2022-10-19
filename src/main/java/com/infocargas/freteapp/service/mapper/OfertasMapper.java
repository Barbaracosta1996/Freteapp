package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ofertas} and its DTO {@link OfertasDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfertasMapper extends EntityMapper<OfertasDTO, Ofertas> {
    @Mapping(target = "perfil", source = "perfil", qualifiedByName = "perfilId")
    OfertasDTO toDto(Ofertas s);

    @Named("perfilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PerfilDTO toDtoPerfilId(Perfil perfil);
}
