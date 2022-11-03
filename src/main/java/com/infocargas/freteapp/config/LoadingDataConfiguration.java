package com.infocargas.freteapp.config;

import com.infocargas.freteapp.service.SettingsCargaAppService;
import com.infocargas.freteapp.service.dto.SettingsCargaAppDTO;
import java.util.Optional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class LoadingDataConfiguration implements InitializingBean {

    private final SettingsCargaAppService settingsCargaAppService;

    public LoadingDataConfiguration(SettingsCargaAppService settingsCargaAppService) {
        this.settingsCargaAppService = settingsCargaAppService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        var settingsCargaApp = settingsCargaAppService.findOne(1L);

        if (settingsCargaApp.isEmpty()) {
            SettingsCargaAppDTO settingsCargaAppDTO = new SettingsCargaAppDTO();
            settingsCargaAppDTO.setId(1221L);
            settingsCargaAppService.save(settingsCargaAppDTO);
        }
    }
}
