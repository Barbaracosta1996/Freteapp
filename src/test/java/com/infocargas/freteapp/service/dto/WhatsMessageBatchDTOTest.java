package com.infocargas.freteapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WhatsMessageBatchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WhatsMessageBatchDTO.class);
        WhatsMessageBatchDTO whatsMessageBatchDTO1 = new WhatsMessageBatchDTO();
        whatsMessageBatchDTO1.setId(1L);
        WhatsMessageBatchDTO whatsMessageBatchDTO2 = new WhatsMessageBatchDTO();
        assertThat(whatsMessageBatchDTO1).isNotEqualTo(whatsMessageBatchDTO2);
        whatsMessageBatchDTO2.setId(whatsMessageBatchDTO1.getId());
        assertThat(whatsMessageBatchDTO1).isEqualTo(whatsMessageBatchDTO2);
        whatsMessageBatchDTO2.setId(2L);
        assertThat(whatsMessageBatchDTO1).isNotEqualTo(whatsMessageBatchDTO2);
        whatsMessageBatchDTO1.setId(null);
        assertThat(whatsMessageBatchDTO1).isNotEqualTo(whatsMessageBatchDTO2);
    }
}
