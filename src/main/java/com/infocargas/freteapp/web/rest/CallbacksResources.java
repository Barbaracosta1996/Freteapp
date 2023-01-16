package com.infocargas.freteapp.web.rest;

import static com.infocargas.freteapp.config.Constants.TOKEN_CALLBACK;

import com.infocargas.freteapp.controller.RDStationController;
import com.infocargas.freteapp.domain.SettingsCargaApp;
import com.infocargas.freteapp.repository.SettingsCargaAppRepository;
import com.infocargas.freteapp.response.facebook.FacebookResponse;
import com.infocargas.freteapp.service.*;
import com.infocargas.freteapp.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CallbacksResources {

    private final Logger logger = LoggerFactory.getLogger(CallbacksResources.class);

    private final RDStationController rdStationController;

    private final SettingsCargaAppRepository settingsCargaAppRepository;

    private final CallbacksService callbacksService;

    public CallbacksResources(
        RDStationController rdStationController,
        SettingsCargaAppRepository settingsCargaAppRepository,
        CallbacksService callbacksService
    ) {
        this.rdStationController = rdStationController;
        this.settingsCargaAppRepository = settingsCargaAppRepository;
        this.callbacksService = callbacksService;
    }

    @GetMapping("/callbacks/rd/auth")
    @ResponseStatus(HttpStatus.OK)
    public void rdCallbackReceiver(@RequestParam(required = false, name = "code") String code) {
        logger.info("Acesso concedido: {}", code);
        Optional<SettingsCargaApp> settingsApp = settingsCargaAppRepository.findById(1221L);
        settingsApp.ifPresent(app -> {
            app.setRdCode(code);
            settingsCargaAppRepository.save(app);
            rdStationController.authentication(code);
        });
    }

    @PostMapping(value = "/callbacks/wp/returned")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> webhookFacebookWhatsappCallback(@RequestBody FacebookResponse facebookResponse) {
        if (facebookResponse != null) {
            if (
                !facebookResponse.getEntry().isEmpty() &&
                !facebookResponse.getEntry().get(0).getChanges().isEmpty() &&
                facebookResponse.getEntry().get(0).getChanges().get(0).getValue() != null
            ) {
                var facebookResponseChange = facebookResponse.getEntry().get(0).getChanges().get(0);

                if (callbacksService.validateMessages(facebookResponseChange)) {
                    callbacksService.updateStatusMessage(facebookResponseChange);

                    if (facebookResponseChange.getValue().getMessages() != null) {
                        if (
                            facebookResponseChange.getValue().getContacts() != null &&
                            facebookResponseChange.getValue().getContacts().size() > 0
                        ) {
                            return callbacksService.processingFacebookMessage(facebookResponseChange);
                        }
                    }
                } else {
                    return ResponseEntity.ok().build();
                }
            }
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/callbacks/wp/returned")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> facebookValid(
        @RequestParam("hub.mode") String mode,
        @RequestParam("hub.challenge") Integer challenge,
        @RequestParam("hub.verify_token") String verify_token
    ) {
        if (!TOKEN_CALLBACK.equals(verify_token)) {
            throw new BadRequestAlertException("Token Invalid", "Callback", "invalidToken");
        }

        return ResponseEntity.ok().body(challenge);
    }
}
