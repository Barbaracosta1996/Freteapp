package com.infocargas.freteapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OfertasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ofertas.class);
        Ofertas ofertas1 = new Ofertas();
        ofertas1.setId(1L);
        Ofertas ofertas2 = new Ofertas();
        ofertas2.setId(ofertas1.getId());
        assertThat(ofertas1).isEqualTo(ofertas2);
        ofertas2.setId(2L);
        assertThat(ofertas1).isNotEqualTo(ofertas2);
        ofertas1.setId(null);
        assertThat(ofertas1).isNotEqualTo(ofertas2);
    }
}
