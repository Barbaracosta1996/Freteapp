package com.infocargas.freteapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PerfilMapperTest {

    private PerfilMapper perfilMapper;

    @BeforeEach
    public void setUp() {
        perfilMapper = new PerfilMapperImpl();
    }
}
