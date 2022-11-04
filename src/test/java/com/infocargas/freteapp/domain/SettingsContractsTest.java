package com.infocargas.freteapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infocargas.freteapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettingsContractsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettingsContracts.class);
        SettingsContracts settingsContracts1 = new SettingsContracts();
        settingsContracts1.setId(1L);
        SettingsContracts settingsContracts2 = new SettingsContracts();
        settingsContracts2.setId(settingsContracts1.getId());
        assertThat(settingsContracts1).isEqualTo(settingsContracts2);
        settingsContracts2.setId(2L);
        assertThat(settingsContracts1).isNotEqualTo(settingsContracts2);
        settingsContracts1.setId(null);
        assertThat(settingsContracts1).isNotEqualTo(settingsContracts2);
    }
}
