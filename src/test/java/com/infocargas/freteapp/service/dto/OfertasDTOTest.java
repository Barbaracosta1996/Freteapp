package com.infocargas.freteapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OfertasDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfertasDTO.class);
        OfertasDTO ofertasDTO1 = new OfertasDTO();
        ofertasDTO1.setId(1L);
        OfertasDTO ofertasDTO2 = new OfertasDTO();
        assertThat(ofertasDTO1).isNotEqualTo(ofertasDTO2);
        ofertasDTO2.setId(ofertasDTO1.getId());
        assertThat(ofertasDTO1).isEqualTo(ofertasDTO2);
        ofertasDTO2.setId(2L);
        assertThat(ofertasDTO1).isNotEqualTo(ofertasDTO2);
        ofertasDTO1.setId(null);
        assertThat(ofertasDTO1).isNotEqualTo(ofertasDTO2);
    }
}
