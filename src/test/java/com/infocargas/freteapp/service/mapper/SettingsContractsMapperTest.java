package com.infocargas.freteapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettingsContractsMapperTest {

    private SettingsContractsMapper settingsContractsMapper;

    @BeforeEach
    public void setUp() {
        settingsContractsMapper = new SettingsContractsMapperImpl();
    }
}
