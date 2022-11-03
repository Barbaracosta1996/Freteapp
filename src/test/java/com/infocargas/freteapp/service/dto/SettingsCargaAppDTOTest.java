package com.infocargas.freteapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettingsCargaAppDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettingsCargaAppDTO.class);
        SettingsCargaAppDTO settingsCargaAppDTO1 = new SettingsCargaAppDTO();
        settingsCargaAppDTO1.setId(1L);
        SettingsCargaAppDTO settingsCargaAppDTO2 = new SettingsCargaAppDTO();
        assertThat(settingsCargaAppDTO1).isNotEqualTo(settingsCargaAppDTO2);
        settingsCargaAppDTO2.setId(settingsCargaAppDTO1.getId());
        assertThat(settingsCargaAppDTO1).isEqualTo(settingsCargaAppDTO2);
        settingsCargaAppDTO2.setId(2L);
        assertThat(settingsCargaAppDTO1).isNotEqualTo(settingsCargaAppDTO2);
        settingsCargaAppDTO1.setId(null);
        assertThat(settingsCargaAppDTO1).isNotEqualTo(settingsCargaAppDTO2);
    }
}
