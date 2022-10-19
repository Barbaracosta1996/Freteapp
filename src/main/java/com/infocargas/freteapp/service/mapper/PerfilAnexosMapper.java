package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.domain.PerfilAnexos;
import com.infocargas.freteapp.service.dto.PerfilAnexosDTO;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PerfilAnexos} and its DTO {@link PerfilAnexosDTO}.
 */
@Mapper(componentModel = "spring")
public interface PerfilAnexosMapper extends EntityMapper<PerfilAnexosDTO, PerfilAnexos> {
    @Mapping(target = "perfil", source = "perfil", qualifiedByName = "perfilId")
    PerfilAnexosDTO toDto(PerfilAnexos s);

    @Named("perfilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PerfilDTO toDtoPerfilId(Perfil perfil);
}
