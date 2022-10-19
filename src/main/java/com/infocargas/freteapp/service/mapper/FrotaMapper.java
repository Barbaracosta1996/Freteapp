package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.CategoriaVeiculo;
import com.infocargas.freteapp.domain.Frota;
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.service.dto.CategoriaVeiculoDTO;
import com.infocargas.freteapp.service.dto.FrotaDTO;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Frota} and its DTO {@link FrotaDTO}.
 */
@Mapper(componentModel = "spring")
public interface FrotaMapper extends EntityMapper<FrotaDTO, Frota> {
    @Mapping(target = "perfil", source = "perfil", qualifiedByName = "perfilId")
    @Mapping(target = "categoriaVeiculo", source = "categoriaVeiculo", qualifiedByName = "categoriaVeiculoId")
    FrotaDTO toDto(Frota s);

    @Named("perfilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PerfilDTO toDtoPerfilId(Perfil perfil);

    @Named("categoriaVeiculoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoriaVeiculoDTO toDtoCategoriaVeiculoId(CategoriaVeiculo categoriaVeiculo);
}
