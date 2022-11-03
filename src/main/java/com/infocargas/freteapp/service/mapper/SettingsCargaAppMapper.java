package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.SettingsCargaApp;
import com.infocargas.freteapp.service.dto.SettingsCargaAppDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SettingsCargaApp} and its DTO {@link SettingsCargaAppDTO}.
 */
@Mapper(componentModel = "spring")
public interface SettingsCargaAppMapper extends EntityMapper<SettingsCargaAppDTO, SettingsCargaApp> {}
