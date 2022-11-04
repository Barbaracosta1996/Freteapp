package com.infocargas.freteapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettingsContractsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettingsContractsDTO.class);
        SettingsContractsDTO settingsContractsDTO1 = new SettingsContractsDTO();
        settingsContractsDTO1.setId(1L);
        SettingsContractsDTO settingsContractsDTO2 = new SettingsContractsDTO();
        assertThat(settingsContractsDTO1).isNotEqualTo(settingsContractsDTO2);
        settingsContractsDTO2.setId(settingsContractsDTO1.getId());
        assertThat(settingsContractsDTO1).isEqualTo(settingsContractsDTO2);
        settingsContractsDTO2.setId(2L);
        assertThat(settingsContractsDTO1).isNotEqualTo(settingsContractsDTO2);
        settingsContractsDTO1.setId(null);
        assertThat(settingsContractsDTO1).isNotEqualTo(settingsContractsDTO2);
    }
}
