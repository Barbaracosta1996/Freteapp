package com.infocargas.freteapp.web.rest;

import static com.infocargas.freteapp.config.Constants.TOKEN_CALLBACK;

import com.infocargas.freteapp.controller.FacebookController;
import com.infocargas.freteapp.controller.RDStationController;
import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.SettingsCargaApp;
import com.infocargas.freteapp.domain.enumeration.*;
import com.infocargas.freteapp.repository.SettingsCargaAppRepository;
import com.infocargas.freteapp.response.facebook.FacebookResponse;
import com.infocargas.freteapp.response.facebook.FacebookSendResponse;
import com.infocargas.freteapp.service.*;
import com.infocargas.freteapp.service.dto.*;
import com.infocargas.freteapp.web.rest.errors.BadRequestAlertException;
import java.time.ZonedDateTime;
import java.util.List;
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

    private final PerfilService perfilService;
    private final WhatsMessageBatchService whatsMessageBatchService;

    private final FacebookController facebookController;

    private final RDStationController rdStationController;

    private final RotasOfertasService rotasOfertasService;

    private final SolicitacaoService solicitacaoService;

    private final OfertasService ofertasService;

    private final SettingsCargaAppRepository settingsCargaAppRepository;

    public CallbacksResources(
        PerfilService perfilService,
        WhatsMessageBatchService whatsMessageBatchService,
        FacebookController facebookController,
        RotasOfertasService rotasOfertasService,
        SolicitacaoService solicitacaoService,
        OfertasService ofertasService,
        SettingsCargaAppRepository settingsCargaAppRepository,
        RDStationController rdStationController
    ) {
        this.perfilService = perfilService;
        this.whatsMessageBatchService = whatsMessageBatchService;
        this.facebookController = facebookController;
        this.rotasOfertasService = rotasOfertasService;
        this.solicitacaoService = solicitacaoService;
        this.ofertasService = ofertasService;
        this.settingsCargaAppRepository = settingsCargaAppRepository;
        this.rdStationController = rdStationController;
    }

    @GetMapping("/callbacks/rd/auth")
    @ResponseStatus(HttpStatus.OK)
    public void rdCallbackReceiver(@RequestParam(required = false, name = "code") String code) {
        logger.info("Acesso concedido: {}", code);
        Optional<SettingsCargaApp> settingsApp = settingsCargaAppRepository.findById(1L);
        settingsApp.ifPresent(app -> {
            app.setRdCode(code);
            settingsCargaAppRepository.save(app);
            rdStationController.authentication(code);
        });
    }

    @PostMapping(value = "/callbacks/wp/returned")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> registerAccount(@RequestBody FacebookResponse facebookResponse) {
        if (facebookResponse != null) {
            if (
                !facebookResponse.getEntry().isEmpty() &&
                !facebookResponse.getEntry().get(0).getChanges().isEmpty() &&
                facebookResponse.getEntry().get(0).getChanges().get(0).getValue() != null &&
                !facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getMessages().isEmpty()
            ) {
                if (
                    facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getContacts() != null &&
                    facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getContacts().size() > 0
                ) {
                    String waid = facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWaId();
                    Optional<PerfilDTO> perfilDTO = this.perfilService.findByWaId(waid);

                    if (perfilDTO.isPresent()) {
                        String messageId = facebookResponse
                            .getEntry()
                            .get(0)
                            .getChanges()
                            .get(0)
                            .getValue()
                            .getMessages()
                            .get(0)
                            .getContext()
                            .get("id");
                        Optional<WhatsMessageBatchDTO> message = whatsMessageBatchService.findByMessageId(messageId);

                        if (message.isPresent()) {
                            switch (message.get().getTipo()) {
                                case LIST_ALERT:
                                    var interactive = facebookResponse
                                        .getEntry()
                                        .get(0)
                                        .getChanges()
                                        .get(0)
                                        .getValue()
                                        .getMessages()
                                        .get(0)
                                        .getInteractive();
                                    if (interactive.getListReply() != null) {
                                        var ofertaPerfil = rotasOfertasService.findByIdOferta(message.get().getOfertaId());

                                        var ofertaId = interactive.getListReply().get("id");
                                        var requestedOferta = rotasOfertasService.findByIdOferta(Long.valueOf(ofertaId));

                                        if (
                                            (
                                                requestedOferta.isPresent() &&
                                                requestedOferta.get().getOfertas().getStatus() != StatusOferta.AGUARDANDO_PROPOSTA
                                            ) ||
                                            (
                                                requestedOferta.isPresent() &&
                                                requestedOferta.get().getOfertas().getStatus() != StatusOferta.AGUARDANDO_PROPOSTA
                                            )
                                        ) {
                                            facebookController.sendOneMessage(
                                                ofertaPerfil.get().getOfertas().getPerfil().getUser().getPhone(),
                                                "A solicitação foi cancelada porque o transporte já foi encerrado. Assim que surgir novas indicações enviaremos aqui."
                                            );
                                            message.get().setStatus(WhatsStatus.CLOSED);
                                            whatsMessageBatchService.save(message.get());
                                            return ResponseEntity.ok().build();
                                        }

                                        requestedOferta.ifPresent(requested -> {
                                            FacebookSendResponse response;

                                            SolicitacaoDTO solicitacaoDTO = new SolicitacaoDTO();
                                            solicitacaoDTO.setRequestedPerfil(ofertaPerfil.get().getOfertas().getPerfil());
                                            solicitacaoDTO.setPerfil(requestedOferta.get().getOfertas().getPerfil());
                                            solicitacaoDTO.setOfertas(ofertaPerfil.get().getOfertas());
                                            solicitacaoDTO.setMinhaOferta(requestedOferta.get().getOfertas());
                                            solicitacaoDTO.setDataProposta(ZonedDateTime.now());
                                            solicitacaoDTO.setStatus(StatusSolicitacao.AGUARDANDORESPOSTA);

                                            if (ofertaPerfil.get().getOfertas().getTipoOferta() == TipoOferta.CARGA) {
                                                solicitacaoDTO = solicitacaoService.save(solicitacaoDTO);
                                                solicitacaoDTO.setPerfil(
                                                    perfilService.findOne(requestedOferta.get().getOfertas().getPerfil().getId()).get()
                                                );
                                                solicitacaoDTO
                                                    .getOfertas()
                                                    .setPerfil(
                                                        perfilService.findOne(ofertaPerfil.get().getOfertas().getPerfil().getId()).get()
                                                    );
                                                response = facebookController.sendRequestCargo(solicitacaoDTO);
                                            } else {
                                                response = facebookController.sendRequestVacancies(solicitacaoDTO);
                                            }

                                            facebookController.sendOneMessage(
                                                solicitacaoDTO.getRequestedPerfil().getUser().getPhone(),
                                                String.format(
                                                    "Enviamos sua solicitação para o(a) *%s* e será necessário a aprovação do mesmo(a) para dar sequência no processo. " +
                                                    "Assim que houver quaisquer atualização entraremos em contato.",
                                                    solicitacaoDTO.getPerfil().getTipoConta().name().toLowerCase()
                                                )
                                            );

                                            if (response.getError() == null) {
                                                message.get().setStatus(WhatsStatus.CLOSED);
                                                whatsMessageBatchService.save(message.get());

                                                var newMessageBatch = new WhatsMessageBatchDTO();
                                                newMessageBatch.setStatus(WhatsStatus.OPEN);
                                                newMessageBatch.setWaidTo(response.getMessages().get(0).getId());
                                                newMessageBatch.setTipoOferta(solicitacaoDTO.getOfertas().getTipoOferta());
                                                newMessageBatch.setOfertaId(solicitacaoDTO.getId());
                                                newMessageBatch.setPerfilID(solicitacaoDTO.getPerfil().getId().intValue());
                                                newMessageBatch.setTipo(WhatsAppType.LIST_ALERT_CHOOSED);
                                                whatsMessageBatchService.save(newMessageBatch);
                                            }
                                        });
                                    }
                                    break;
                                case INDICATION_ALERT:
                                    var buttonIndication = facebookResponse
                                        .getEntry()
                                        .get(0)
                                        .getChanges()
                                        .get(0)
                                        .getValue()
                                        .getMessages()
                                        .get(0)
                                        .getButton();
                                    if (buttonIndication.getPayload() != null && buttonIndication.getPayload().equals("Sim")) {
                                        var ofertasPerfil = rotasOfertasService.findByIdOferta(message.get().getOfertaId());
                                        ofertasPerfil.ifPresent(rotasOfertasDTO -> {
                                            List<OfertasDTO> ofertas = rotasOfertasService.findNearsOfertas(
                                                rotasOfertasDTO.getOfertas().getId()
                                            );
                                            var response = facebookController.sendNearsRouteNotifcation(ofertas, rotasOfertasDTO);
                                            if (response.getError() == null) {
                                                message.get().setStatus(WhatsStatus.CLOSED);
                                                whatsMessageBatchService.save(message.get());

                                                var newMessageBatch = new WhatsMessageBatchDTO();
                                                newMessageBatch.setStatus(WhatsStatus.OPEN);
                                                newMessageBatch.setWaidTo(response.getMessages().get(0).getId());
                                                newMessageBatch.setTipoOferta(rotasOfertasDTO.getOfertas().getTipoOferta());
                                                newMessageBatch.setOfertaId(rotasOfertasDTO.getId());
                                                newMessageBatch.setPerfilID(perfilDTO.get().getId().intValue());
                                                newMessageBatch.setTipo(WhatsAppType.LIST_ALERT);
                                                whatsMessageBatchService.save(newMessageBatch);
                                            }
                                        });
                                    }
                                    break;
                                case LIST_ALERT_CHOOSED:
                                    var buttonChoose = facebookResponse
                                        .getEntry()
                                        .get(0)
                                        .getChanges()
                                        .get(0)
                                        .getValue()
                                        .getMessages()
                                        .get(0)
                                        .getButton();
                                    if (buttonChoose.getPayload() != null) {
                                        var solicitacaoDTO = solicitacaoService.findOne(message.get().getOfertaId());
                                        solicitacaoDTO.ifPresent(solicitacao -> {
                                            solicitacao.setStatus(
                                                buttonChoose.getPayload().equals("Sim")
                                                    ? StatusSolicitacao.ACEITOU
                                                    : StatusSolicitacao.CANCELOU
                                            );
                                            solicitacao.setAceitar(
                                                buttonChoose.getPayload().equals("Sim") ? AnwserStatus.SIM : AnwserStatus.NAO
                                            );

                                            solicitacao = solicitacaoService.save(solicitacao);

                                            solicitacao.setPerfil(perfilService.findOne(solicitacao.getPerfil().getId()).get());
                                            solicitacao.setRequestedPerfil(
                                                perfilService.findOne(solicitacao.getRequestedPerfil().getId()).get()
                                            );

                                            OfertasDTO ofertas = solicitacao.getOfertas();
                                            OfertasDTO ofertasRequested = solicitacao.getMinhaOferta();

                                            FacebookSendResponse response;

                                            if (solicitacao.getAceitar() == AnwserStatus.SIM) {
                                                ofertas.setStatus(StatusOferta.ATENDIDA);
                                                ofertasService.save(ofertas);

                                                ofertasRequested.setStatus(StatusOferta.ATENDIDA);
                                                ofertasService.save(ofertasRequested);

                                                facebookController.sendOneMessage(
                                                    solicitacao.getRequestedPerfil().getUser().getPhone(),
                                                    String.format(
                                                        "O(A) %s %s aceitou seu pedido e estamos lhe enviando o contato " +
                                                        "do mesmo para que você possa combinar os detalhes da viagem.",
                                                        perfilDTO.get().getTipoConta().name(),
                                                        perfilDTO.get().getNome()
                                                    )
                                                );

                                                response = facebookController.sendContact(solicitacao);
                                            } else {
                                                response = facebookController.sendNegativeMessage(solicitacao);
                                            }

                                            if (response.getError() == null) {
                                                if (solicitacao.getAceitar() == AnwserStatus.SIM) {
                                                    facebookController.sendOneMessage(
                                                        solicitacao.getPerfil().getUser().getPhone(),
                                                        String.format(
                                                            "Enviamos sua resposta para o(a) %s %s e em breve o mesmo " +
                                                            "deve entrar em contato para combinar a viagem. " +
                                                            "Abaixo segue o contato do responsável.",
                                                            solicitacao.getOfertas().getPerfil().getTipoConta().name(),
                                                            solicitacao.getOfertas().getPerfil().getNome()
                                                        )
                                                    );
                                                }
                                                facebookController.sendContactPerfil(
                                                    solicitacao.getRequestedPerfil(),
                                                    solicitacao.getPerfil().getUser().getPhone()
                                                );

                                                message.get().setStatus(WhatsStatus.CLOSED);
                                                whatsMessageBatchService.save(message.get());

                                                var qte = ofertasRequested.getQuantidade() - ofertas.getQuantidade();

                                                if (qte > 0) {
                                                    ofertasRequested.setId(null);
                                                    ofertasRequested.setQuantidade(qte);
                                                    ofertasRequested.setStatus(StatusOferta.AGUARDANDO_PROPOSTA);
                                                    ofertasService.createPortal(ofertasRequested);
                                                }

                                                if (qte < 0) {
                                                    ofertas.setId(null);
                                                    ofertas.setQuantidade(Math.abs(qte));
                                                    ofertasRequested.setStatus(StatusOferta.AGUARDANDO_PROPOSTA);
                                                    ofertasService.createPortal(ofertasRequested);
                                                }
                                            }
                                        });
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
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
