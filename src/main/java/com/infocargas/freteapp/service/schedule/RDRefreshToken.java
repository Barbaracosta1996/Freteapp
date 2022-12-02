package com.infocargas.freteapp.service.schedule;

import com.infocargas.freteapp.proxy.RDStationProxy;
import com.infocargas.freteapp.repository.SettingsCargaAppRepository;
import com.infocargas.freteapp.web.rest.vm.RDStationRefresh;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RDRefreshToken {

    private final Logger logger = LoggerFactory.getLogger(RDRefreshToken.class);

    @Autowired
    private Environment environment;

    @Autowired
    private RDStationProxy rdStationProxy;

    @Autowired
    private SettingsCargaAppRepository repository;

    @Scheduled(fixedDelayString = "PT25H")
    protected void updateRdToken() {
        logger.info("Updating RDStation TOKEN --- {}", ZonedDateTime.now());

        try {
            var settings = repository.findById(1221L);

            settings.ifPresent(settingsApp -> {
                if (settingsApp.getRdCode() == null) {
                    logger.error("RDStation não está conectado. \n {} ", ZonedDateTime.now());
                    return;
                }

                RDStationRefresh refresh = new RDStationRefresh();

                if (
                    Arrays
                        .stream(environment.getActiveProfiles())
                        .anyMatch(env -> env.equalsIgnoreCase("hom") || env.equalsIgnoreCase("dev"))
                ) {
                    refresh.setClientId("e8c7213b-a677-4391-91c3-d84ac58234a3");
                    refresh.setClientSecret("d470be1e0321452dbfe6d23e8772582e");
                } else {
                    refresh.setClientId("ced6d171-e480-44a5-aa2a-04cbe237d40c");
                    refresh.setClientSecret("47cef8d317dd4b9998c300c099948a7b");
                }

                refresh.setRefreshToken(settingsApp.getRdAppRefreshToken());

                var newToken = this.rdStationProxy.refreshRDStation(refresh);

                if (newToken.getStatusCode().is2xxSuccessful()) {
                    settingsApp.setRdAppToken(Objects.requireNonNull(newToken.getBody()).getAccessToken());
                    settingsApp.setRdAppRefreshToken(newToken.getBody().getRefresh_token());

                    this.repository.save(settingsApp);

                    logger.info("Token RdStation renew in {}", ZonedDateTime.now());
                } else {
                    logger.error("Erro ao gerar token: {} \n date: {}", newToken.getStatusCode().getReasonPhrase(), ZonedDateTime.now());
                }
            });
        } catch (Exception e) {
            logger.error("Erro ao gerar token: {} \n {} ", e.getMessage(), ZonedDateTime.now());
        }
    }
}
