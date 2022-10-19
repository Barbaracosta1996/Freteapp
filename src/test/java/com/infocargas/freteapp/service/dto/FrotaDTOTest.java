package com.infocargas.freteapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrotaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrotaDTO.class);
        FrotaDTO frotaDTO1 = new FrotaDTO();
        frotaDTO1.setId(1L);
        FrotaDTO frotaDTO2 = new FrotaDTO();
        assertThat(frotaDTO1).isNotEqualTo(frotaDTO2);
        frotaDTO2.setId(frotaDTO1.getId());
        assertThat(frotaDTO1).isEqualTo(frotaDTO2);
        frotaDTO2.setId(2L);
        assertThat(frotaDTO1).isNotEqualTo(frotaDTO2);
        frotaDTO1.setId(null);
        assertThat(frotaDTO1).isNotEqualTo(frotaDTO2);
    }
}
