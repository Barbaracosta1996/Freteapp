package com.infocargas.freteapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettingsCargaAppMapperTest {

    private SettingsCargaAppMapper settingsCargaAppMapper;

    @BeforeEach
    public void setUp() {
        settingsCargaAppMapper = new SettingsCargaAppMapperImpl();
    }
}
