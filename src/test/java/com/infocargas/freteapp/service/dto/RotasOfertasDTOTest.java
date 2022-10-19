package com.infocargas.freteapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RotasOfertasDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RotasOfertasDTO.class);
        RotasOfertasDTO rotasOfertasDTO1 = new RotasOfertasDTO();
        rotasOfertasDTO1.setId(1L);
        RotasOfertasDTO rotasOfertasDTO2 = new RotasOfertasDTO();
        assertThat(rotasOfertasDTO1).isNotEqualTo(rotasOfertasDTO2);
        rotasOfertasDTO2.setId(rotasOfertasDTO1.getId());
        assertThat(rotasOfertasDTO1).isEqualTo(rotasOfertasDTO2);
        rotasOfertasDTO2.setId(2L);
        assertThat(rotasOfertasDTO1).isNotEqualTo(rotasOfertasDTO2);
        rotasOfertasDTO1.setId(null);
        assertThat(rotasOfertasDTO1).isNotEqualTo(rotasOfertasDTO2);
    }
}
