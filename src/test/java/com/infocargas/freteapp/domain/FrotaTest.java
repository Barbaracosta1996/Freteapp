package com.infocargas.freteapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrotaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Frota.class);
        Frota frota1 = new Frota();
        frota1.setId(1L);
        Frota frota2 = new Frota();
        frota2.setId(frota1.getId());
        assertThat(frota1).isEqualTo(frota2);
        frota2.setId(2L);
        assertThat(frota1).isNotEqualTo(frota2);
        frota1.setId(null);
        assertThat(frota1).isNotEqualTo(frota2);
    }
}
