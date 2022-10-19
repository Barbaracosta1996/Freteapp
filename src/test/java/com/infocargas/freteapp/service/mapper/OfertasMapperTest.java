package com.infocargas.freteapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OfertasMapperTest {

    private OfertasMapper ofertasMapper;

    @BeforeEach
    public void setUp() {
        ofertasMapper = new OfertasMapperImpl();
    }
}
