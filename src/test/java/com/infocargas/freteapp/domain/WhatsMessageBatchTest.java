package com.infocargas.freteapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WhatsMessageBatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WhatsMessageBatch.class);
        WhatsMessageBatch whatsMessageBatch1 = new WhatsMessageBatch();
        whatsMessageBatch1.setId(1L);
        WhatsMessageBatch whatsMessageBatch2 = new WhatsMessageBatch();
        whatsMessageBatch2.setId(whatsMessageBatch1.getId());
        assertThat(whatsMessageBatch1).isEqualTo(whatsMessageBatch2);
        whatsMessageBatch2.setId(2L);
        assertThat(whatsMessageBatch1).isNotEqualTo(whatsMessageBatch2);
        whatsMessageBatch1.setId(null);
        assertThat(whatsMessageBatch1).isNotEqualTo(whatsMessageBatch2);
    }
}
