package com.infocargas.freteapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettingsCargaAppTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettingsCargaApp.class);
        SettingsCargaApp settingsCargaApp1 = new SettingsCargaApp();
        settingsCargaApp1.setId(1L);
        SettingsCargaApp settingsCargaApp2 = new SettingsCargaApp();
        settingsCargaApp2.setId(settingsCargaApp1.getId());
        assertThat(settingsCargaApp1).isEqualTo(settingsCargaApp2);
        settingsCargaApp2.setId(2L);
        assertThat(settingsCargaApp1).isNotEqualTo(settingsCargaApp2);
        settingsCargaApp1.setId(null);
        assertThat(settingsCargaApp1).isNotEqualTo(settingsCargaApp2);
    }
}
