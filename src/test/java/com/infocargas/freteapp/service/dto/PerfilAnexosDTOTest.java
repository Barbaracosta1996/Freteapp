package com.infocargas.freteapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerfilAnexosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerfilAnexosDTO.class);
        PerfilAnexosDTO perfilAnexosDTO1 = new PerfilAnexosDTO();
        perfilAnexosDTO1.setId(1L);
        PerfilAnexosDTO perfilAnexosDTO2 = new PerfilAnexosDTO();
        assertThat(perfilAnexosDTO1).isNotEqualTo(perfilAnexosDTO2);
        perfilAnexosDTO2.setId(perfilAnexosDTO1.getId());
        assertThat(perfilAnexosDTO1).isEqualTo(perfilAnexosDTO2);
        perfilAnexosDTO2.setId(2L);
        assertThat(perfilAnexosDTO1).isNotEqualTo(perfilAnexosDTO2);
        perfilAnexosDTO1.setId(null);
        assertThat(perfilAnexosDTO1).isNotEqualTo(perfilAnexosDTO2);
    }
}
