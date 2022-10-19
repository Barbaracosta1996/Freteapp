package com.infocargas.freteapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RotasOfertasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RotasOfertas.class);
        RotasOfertas rotasOfertas1 = new RotasOfertas();
        rotasOfertas1.setId(1L);
        RotasOfertas rotasOfertas2 = new RotasOfertas();
        rotasOfertas2.setId(rotasOfertas1.getId());
        assertThat(rotasOfertas1).isEqualTo(rotasOfertas2);
        rotasOfertas2.setId(2L);
        assertThat(rotasOfertas1).isNotEqualTo(rotasOfertas2);
        rotasOfertas1.setId(null);
        assertThat(rotasOfertas1).isNotEqualTo(rotasOfertas2);
    }
}
