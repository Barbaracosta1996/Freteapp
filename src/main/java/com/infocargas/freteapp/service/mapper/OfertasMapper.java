package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ofertas} and its DTO {@link OfertasDTO}.
 */
@Mapper(componentModel = "spring", uses = { PerfilMapper.class })
public interface OfertasMapper extends EntityMapper<OfertasDTO, Ofertas> {
    @Mapping(target = "perfilCompleto", source = "perfil")
    @Mapping(target = "perfil", source = "perfil")
    OfertasDTO toDto(Ofertas s);
}
