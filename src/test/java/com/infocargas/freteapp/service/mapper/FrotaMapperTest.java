package com.infocargas.freteapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FrotaMapperTest {

    private FrotaMapper frotaMapper;

    @BeforeEach
    public void setUp() {
        frotaMapper = new FrotaMapperImpl();
    }
}
