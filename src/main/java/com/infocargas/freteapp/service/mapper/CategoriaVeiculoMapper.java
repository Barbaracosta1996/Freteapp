package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.CategoriaVeiculo;
import com.infocargas.freteapp.service.dto.CategoriaVeiculoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoriaVeiculo} and its DTO {@link CategoriaVeiculoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaVeiculoMapper extends EntityMapper<CategoriaVeiculoDTO, CategoriaVeiculo> {}
