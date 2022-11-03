package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.domain.User;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import com.infocargas.freteapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Perfil} and its DTO {@link PerfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface PerfilMapper extends EntityMapper<PerfilDTO, Perfil> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    PerfilDTO toDto(Perfil s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "phone", source = "telephoneNumber")
    UserDTO toDtoUserLogin(User user);
}
