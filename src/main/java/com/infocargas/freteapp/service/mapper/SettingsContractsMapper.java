package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.SettingsContracts;
import com.infocargas.freteapp.service.dto.SettingsContractsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SettingsContracts} and its DTO {@link SettingsContractsDTO}.
 */
@Mapper(componentModel = "spring")
public interface SettingsContractsMapper extends EntityMapper<SettingsContractsDTO, SettingsContracts> {}
