package com.infocargas.freteapp.config;

import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.service.OfertasService;
import com.infocargas.freteapp.service.SettingsCargaAppService;
import com.infocargas.freteapp.service.dto.SettingsCargaAppDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class LoadingDataConfiguration implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(LoadingDataConfiguration.class);

    private final SettingsCargaAppService settingsCargaAppService;

    public LoadingDataConfiguration(SettingsCargaAppService settingsCargaAppService) {
        this.settingsCargaAppService = settingsCargaAppService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        var settingsCargaApp = settingsCargaAppService.findOne(1221L);

        if (settingsCargaApp.isEmpty()) {
            SettingsCargaAppDTO settingsCargaAppDTO = new SettingsCargaAppDTO();
            settingsCargaAppDTO.setId(1221L);
            settingsCargaAppService.save(settingsCargaAppDTO);
        }
        //        log.info("------------------ Lista todas ofertas com status ----------------------");
        //
        //        var ofertas = ofertasService.findAllByStatus(StatusOferta.AGUARDANDO_PROPOSTA);
        //        ofertas.forEach(ofertasService::saveNewRoute);
        //
        //        log.info("------------------ Atualizado com rotas ofertas ------------------------");

    }
}
