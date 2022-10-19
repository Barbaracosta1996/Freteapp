package com.infocargas.freteapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerfilAnexosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerfilAnexos.class);
        PerfilAnexos perfilAnexos1 = new PerfilAnexos();
        perfilAnexos1.setId(1L);
        PerfilAnexos perfilAnexos2 = new PerfilAnexos();
        perfilAnexos2.setId(perfilAnexos1.getId());
        assertThat(perfilAnexos1).isEqualTo(perfilAnexos2);
        perfilAnexos2.setId(2L);
        assertThat(perfilAnexos1).isNotEqualTo(perfilAnexos2);
        perfilAnexos1.setId(null);
        assertThat(perfilAnexos1).isNotEqualTo(perfilAnexos2);
    }
}
