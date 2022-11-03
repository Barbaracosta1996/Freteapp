package com.infocargas.freteapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WhatsMessageBatchMapperTest {

    private WhatsMessageBatchMapper whatsMessageBatchMapper;

    @BeforeEach
    public void setUp() {
        whatsMessageBatchMapper = new WhatsMessageBatchMapperImpl();
    }
}
