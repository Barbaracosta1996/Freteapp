package com.infocargas.freteapp.controller;

import com.infocargas.freteapp.proxy.RDStationProxy;
import com.infocargas.freteapp.response.*;
import com.infocargas.freteapp.service.SettingsCargaAppService;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import com.infocargas.freteapp.web.rest.vm.RDStationAuth;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class RDStationController {

    private final RDStationProxy rdStationProxy;
    private final SettingsCargaAppService settingsCargaAppService;

    public RDStationController(RDStationProxy rdStationProxy, SettingsCargaAppService settingsCargaAppService) {
        this.rdStationProxy = rdStationProxy;
        this.settingsCargaAppService = settingsCargaAppService;
    }

    private RDToken authentication() {
        RDStationAuth auth = new RDStationAuth();

        auth.setClientId("e8c7213b-a677-4391-91c3-d84ac58234a3");
        auth.setClientSecret("d470be1e0321452dbfe6d23e8772582e");

        settingsCargaAppService
            .findOne(1L)
            .ifPresent(settings -> {
                auth.setCode(settings.getRdCode());
            });

        if (auth.getCode() != null) {
            ResponseEntity<RDToken> responseToken = rdStationProxy.authRDStation(auth);

            if (responseToken.getStatusCode().value() == 200) {
                return responseToken.getBody();
            }
        }

        return null;
    }

    @Transactional
    public UUID createContact(PerfilDTO perfilDTO) {
        RDEventsDTO eventsDTO = new RDEventsDTO();

        eventsDTO.setEventType("CONVERSION");
        eventsDTO.setEventFamily("CDP");

        RDContactDTO rdContactDTO = new RDContactDTO();

        rdContactDTO.setCountry("Brasil");
        rdContactDTO.setCity(perfilDTO.getCidade());
        rdContactDTO.setState(perfilDTO.getEstado());
        rdContactDTO.setEmail(perfilDTO.getUser().getEmail());
        rdContactDTO.setJobTitle(perfilDTO.getTipoConta().name());
        rdContactDTO.setMobile_phone(String.format("+55 %s", perfilDTO.getUser().getPhone()));
        rdContactDTO.setPersonalPhone(perfilDTO.getTelefoneComercial());
        rdContactDTO.setTags(new String[] { "carga certa site", "api", "cadastro usuario", perfilDTO.getTipoConta().name().toLowerCase() });

        rdContactDTO.setApiIdentification("cargacerta");
        rdContactDTO.setConversionIdentifier("Cadastro Sistema");
        rdContactDTO.setFunnelName("default");
        rdContactDTO.setFunnelStage("lead");
        rdContactDTO.setOpportunityTitle("default");
        rdContactDTO.setCreatedAt(ZonedDateTime.now());
        rdContactDTO.setTrafficSource("Cadastro Carga Certa");
        rdContactDTO.setTrafficCampaign(perfilDTO.getTipoConta().name());
        rdContactDTO.setTrafficValue("cadastro motorista");
        rdContactDTO.setAvailable_for_mailing(true);

        RDLegalBases rdLegalBases = new RDLegalBases();
        rdLegalBases.setCategory("communications");
        rdLegalBases.setType("consent");
        rdLegalBases.setStatus("granted");

        rdContactDTO.setLegal_bases(new RDLegalBases[] { rdLegalBases });

        eventsDTO.setPayload(rdContactDTO);

        RDToken token = authentication();

        if (token != null) {
            ResponseEntity<RDContactResponse> response = rdStationProxy.createContact("Bearer " + token.getAccessToken(), eventsDTO);
            if (response.getStatusCode().value() == 200 || response.getStatusCode().value() == 201) {
                return Objects.requireNonNull(response.getBody()).getUuid();
            }
        }

        return null;
    }
}
