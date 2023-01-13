package com.infocargas.freteapp.web.rest;

import static com.infocargas.freteapp.config.Constants.TOKEN_CALLBACK;

import com.google.gson.Gson;
import com.infocargas.freteapp.controller.FacebookController;
import com.infocargas.freteapp.controller.RDStationController;
import com.infocargas.freteapp.domain.SettingsCargaApp;
import com.infocargas.freteapp.domain.enumeration.*;
import com.infocargas.freteapp.repository.OfertasRepository;
import com.infocargas.freteapp.repository.SettingsCargaAppRepository;
import com.infocargas.freteapp.response.facebook.FacebookResponse;
import com.infocargas.freteapp.response.facebook.FacebookSendResponse;
import com.infocargas.freteapp.service.PerfilService;
import com.infocargas.freteapp.service.RotasOfertasService;
import com.infocargas.freteapp.service.SolicitacaoService;
import com.infocargas.freteapp.service.WhatsMessageBatchService;
import com.infocargas.freteapp.service.dto.*;
import com.infocargas.freteapp.service.dto.routes.GoogleLegs;
import com.infocargas.freteapp.service.dto.routes.GoogleRoutes;
import com.infocargas.freteapp.service.mapper.OfertasMapper;
import com.infocargas.freteapp.utils.GeoUtils;
import com.infocargas.freteapp.web.rest.errors.BadRequestAlertException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
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

    private final OfertasRepository ofertasRepository;

    private final OfertasMapper ofertasMapper;

    private final SettingsCargaAppRepository settingsCargaAppRepository;

    public CallbacksResources(
        PerfilService perfilService,
        WhatsMessageBatchService whatsMessageBatchService,
        FacebookController facebookController,
        RDStationController rdStationController,
        RotasOfertasService rotasOfertasService,
        SolicitacaoService solicitacaoService,
        OfertasRepository ofertasRepository,
        OfertasMapper ofertasMapper,
        SettingsCargaAppRepository settingsCargaAppRepository
    ) {
        this.perfilService = perfilService;
        this.whatsMessageBatchService = whatsMessageBatchService;
        this.facebookController = facebookController;
        this.rdStationController = rdStationController;
        this.rotasOfertasService = rotasOfertasService;
        this.solicitacaoService = solicitacaoService;
        this.ofertasRepository = ofertasRepository;
        this.ofertasMapper = ofertasMapper;
        this.settingsCargaAppRepository = settingsCargaAppRepository;
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
    public ResponseEntity<Void> registerAccount(@RequestBody FacebookResponse facebookResponse) {
        if (facebookResponse != null) {
            if (
                !facebookResponse.getEntry().isEmpty() &&
                !facebookResponse.getEntry().get(0).getChanges().isEmpty() &&
                facebookResponse.getEntry().get(0).getChanges().get(0).getValue() != null &&
                facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getMessages() != null
            ) {
                if (
                    facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getContacts() != null &&
                    facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getContacts().size() > 0
                ) {
                    String waid = facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWaId();
                    Optional<PerfilDTO> perfilDTO = this.perfilService.findByWaId(waid);

                    if (
                        perfilDTO.isPresent() &&
                        facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getContext() != null
                    ) {
                        String messageId = facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId();
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
                                        var minhaOferta = rotasOfertasService.findByIdOferta(message.get().getOfertaId());

                                        var ofertaId = interactive.getListReply().get("id");
                                        var ofertaEscolhida = rotasOfertasService.findByIdOferta(Long.valueOf(ofertaId));

                                        if (
                                            (
                                                ofertaEscolhida.isPresent() &&
                                                ofertaEscolhida.get().getOfertas().getStatus() != StatusOferta.AGUARDANDO_PROPOSTA
                                            ) ||
                                            (
                                                ofertaEscolhida.isPresent() &&
                                                ofertaEscolhida.get().getOfertas().getStatus() != StatusOferta.AGUARDANDO_PROPOSTA
                                            )
                                        ) {
                                            facebookController.sendOneMessage(
                                                minhaOferta.get().getOfertas().getPerfil().getUser().getPhone(),
                                                "A solicitação foi cancelada porque o transporte já foi encerrado. Assim que surgir novas indicações enviaremos aqui."
                                            );
                                            message.get().setStatus(WhatsStatus.CLOSED);
                                            whatsMessageBatchService.save(message.get());
                                            return ResponseEntity.ok().build();
                                        }

                                        ofertaEscolhida.ifPresent(requested -> {
                                            FacebookSendResponse response;

                                            SolicitacaoDTO solicitacaoDTO = new SolicitacaoDTO();
                                            solicitacaoDTO.setRequestedPerfil(minhaOferta.get().getOfertas().getPerfil());
                                            solicitacaoDTO.setPerfil(ofertaEscolhida.get().getOfertas().getPerfil());
                                            solicitacaoDTO.setOfertas(minhaOferta.get().getOfertas());
                                            solicitacaoDTO.setMinhaOferta(ofertaEscolhida.get().getOfertas());
                                            solicitacaoDTO.setDataProposta(ZonedDateTime.now());
                                            solicitacaoDTO.setStatus(StatusSolicitacao.AGUARDANDORESPOSTA);

                                            solicitacaoDTO = solicitacaoService.save(solicitacaoDTO);
                                            solicitacaoDTO.setPerfil(
                                                perfilService.findOne(ofertaEscolhida.get().getOfertas().getPerfil().getId()).get()
                                            );
                                            solicitacaoDTO
                                                .getOfertas()
                                                .setPerfil(perfilService.findOne(minhaOferta.get().getOfertas().getPerfil().getId()).get());

                                            if (minhaOferta.get().getOfertas().getTipoOferta() == TipoOferta.CARGA) {
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
                                            List<RotasOfertasDTO> ofertasList = rotasOfertasService.findAllByStatusAndDate(
                                                StatusOferta.AGUARDANDO_PROPOSTA
                                            );
                                            List<OfertasDTO> ofertas = findNearsOfertas(rotasOfertasDTO.getOfertas().getId(), ofertasList);
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
                                case INDICATION_ALERT_TRANSPORT:
                                    var buttonChoosePerfil = facebookResponse
                                        .getEntry()
                                        .get(0)
                                        .getChanges()
                                        .get(0)
                                        .getValue()
                                        .getMessages()
                                        .get(0)
                                        .getButton();
                                    if (buttonChoosePerfil.getPayload() != null) {
                                        var ofertaPerfil = rotasOfertasService.findByIdOferta(message.get().getOfertaId());
                                        ofertaPerfil.ifPresent(ofertasDTO -> {
                                            var exibirLista = buttonChoosePerfil.getPayload().equals("SIM");

                                            if (exibirLista) {
                                                ofertasDTO
                                                    .getOfertas()
                                                    .setPerfil(perfilService.findOne(ofertasDTO.getOfertas().getPerfil().getId()).get());
                                                var response = sendNearRoute(ofertasDTO.getOfertas().getId());

                                                if (response == null) {
                                                    logger.error("Falhou o envio para o sistema");
                                                } else {
                                                    message.get().setStatus(WhatsStatus.CLOSED);
                                                    whatsMessageBatchService.save(message.get());

                                                    var newMessageBatch = new WhatsMessageBatchDTO();
                                                    newMessageBatch.setStatus(WhatsStatus.OPEN);
                                                    newMessageBatch.setWaidTo(response.getMessages().get(0).getId());
                                                    newMessageBatch.setTipoOferta(ofertaPerfil.get().getOfertas().getTipoOferta());
                                                    newMessageBatch.setOfertaId(ofertaPerfil.get().getOfertas().getId());
                                                    newMessageBatch.setPerfilID(
                                                        ofertaPerfil.get().getOfertas().getPerfil().getId().intValue()
                                                    );
                                                    newMessageBatch.setTipo(WhatsAppType.LIST_ALERT);
                                                    whatsMessageBatchService.save(newMessageBatch);
                                                }
                                            } else {
                                                message.get().setStatus(WhatsStatus.CLOSED);
                                                whatsMessageBatchService.save(message.get());
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
                                                ofertasRepository.save(ofertasMapper.toEntity(ofertas));

                                                ofertasRequested.setStatus(StatusOferta.ATENDIDA);
                                                ofertasRepository.save(ofertasMapper.toEntity(ofertasRequested));

                                                response = facebookController.sendDoneMatch(solicitacao);
                                            } else {
                                                response = facebookController.sendNegativeMessage(solicitacao);
                                            }

                                            if (response != null && response.getError() == null) {
                                                if (solicitacao.getAceitar() == AnwserStatus.SIM) {
                                                    facebookController.sendDoneRequested(solicitacao);
                                                }

                                                message.get().setStatus(WhatsStatus.CLOSED);
                                                whatsMessageBatchService.save(message.get());

                                                var qte = ofertasRequested.getQuantidade() - ofertas.getQuantidade();

                                                if (qte > 0) {
                                                    ofertasRequested.setId(null);
                                                    ofertasRequested.setQuantidade(qte);
                                                    ofertasRequested.setStatus(StatusOferta.AGUARDANDO_PROPOSTA);
                                                    createPortal(ofertasRequested);
                                                }

                                                if (qte < 0) {
                                                    ofertas.setId(null);
                                                    ofertas.setQuantidade(Math.abs(qte));
                                                    ofertasRequested.setStatus(StatusOferta.AGUARDANDO_PROPOSTA);
                                                    createPortal(ofertasRequested);
                                                }
                                            }
                                        });
                                    }
                                    break;
                                case REDIRECT:
                                    sendMessage(facebookResponse);
                                default:
                                    break;
                            }
                        }
                    } else {
                        sendMessage(facebookResponse);
                    }
                }
            }
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> sendMessage(FacebookResponse facebookResponse) {
        Optional<WhatsMessageBatchDTO> message = whatsMessageBatchService.findByMessageId(
            facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId()
        );

        if (message.isPresent() && message.get().getStatus().equals(WhatsStatus.CLOSED)) {
            logger.info("Mensagem encerrada!!! Não notifica mais.");
            return ResponseEntity.ok().build();
        }

        if (message.isPresent() && message.get().getStatus().equals(WhatsStatus.OPEN)) {
            logger.info("Mensagem do sistema!!! Não notifica mais.");
            return ResponseEntity.ok().build();
        }

        if (Objects.equals(facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getType(), "text")) {
            var perfil = perfilService.findByWaId(
                facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWaId()
            );

            facebookController.sendRedirectMessage(facebookResponse, perfil);
        } else {
            facebookController.sendAlertUser(
                facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWaId()
            );
        }

        var newMessageBatch = new WhatsMessageBatchDTO();
        newMessageBatch.setStatus(WhatsStatus.CLOSED);
        newMessageBatch.setWaidTo(facebookResponse.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId());
        newMessageBatch.setPerfilID(0);
        newMessageBatch.setTipo(WhatsAppType.REDIRECT);
        whatsMessageBatchService.save(newMessageBatch);

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

    private FacebookSendResponse sendNearRoute(Long id) {
        List<RotasOfertasDTO> ofertasList = rotasOfertasService.findAllByStatusAndDate(StatusOferta.AGUARDANDO_PROPOSTA);
        var listaRotas = getNearRoute(id);
        var rotasOfertas = rotasOfertasService.findByIdOferta(id);
        rotasOfertas.get().getOfertas().setPerfil(perfilService.findOne(rotasOfertas.get().getOfertas().getPerfil().getId()).get());
        return facebookController.sendNearsRouteNotifcation(listaRotas, rotasOfertas.get());
    }

    private List<OfertasDTO> findNearsOfertas(Long ofertasId, List<RotasOfertasDTO> ofertasList) {
        Optional<RotasOfertasDTO> dto = rotasOfertasService.findByIdOferta(ofertasId);

        List<OfertasDTO> selected = new ArrayList<>();
        Gson g = new Gson();

        dto.ifPresent(rotasOfertas -> {
            GoogleRoutes[] gRoutesArr = g.fromJson(rotasOfertas.getRotas(), GoogleRoutes[].class);

            GoogleRoutes gRoutes = gRoutesArr[0];

            var perfilId = rotasOfertas.getOfertas().getPerfil().getId();

            List<RotasOfertasDTO> allRoutes = ofertasList
                .stream()
                .filter(o ->
                    !Objects.equals(o.getOfertas().getPerfil().getId(), perfilId) &&
                    o.getOfertas().getTipoOferta().equals(rotasOfertas.getOfertas().getTipoOferta())
                )
                .collect(Collectors.toList());

            allRoutes.forEach(oferta -> {
                if (oferta.getId().equals(rotasOfertas.getId())) {
                    return;
                }

                if (!oferta.getOfertas().getStatus().equals(StatusOferta.AGUARDANDO_PROPOSTA)) {
                    return;
                }

                GoogleRoutes[] gRoutesOferta = g.fromJson(oferta.getRotas(), GoogleRoutes[].class);

                GoogleLegs googleLegs = gRoutes.getLegs().get(0);

                AtomicReference<Double> origem = new AtomicReference<>();
                AtomicReference<Double> destino = new AtomicReference<>();

                googleLegs
                    .getSteps()
                    .forEach(googleSteps -> {
                        if (origem.get() == null) {
                            double result1 = GeoUtils.geoDistanceInKm(
                                googleSteps.getStart_location(),
                                gRoutesOferta[0].getLegs().get(0).getStart_location()
                            );
                            //                            double result2 = GeoUtils.geoDistanceInKm(
                            //                                googleSteps.getEnd_location(),
                            //                                gRoutesOferta[0].getLegs().get(0).getStart_location()
                            //                            );

                            if (result1 > 0.0 && result1 <= 100.0) {
                                origem.set(result1);
                            }
                            //                          } else if (result2 > 0.0 && result2 <= 100) {
                            ////                                origem.set(result2);
                            ////                            }
                        }

                        if (destino.get() == null) {
                            //                            double result1 = GeoUtils.geoDistanceInKm(
                            //                                googleSteps.getStart_location(),
                            //                                gRoutesOferta[0].getLegs().get(0).getEnd_location()
                            //                            );
                            double result2 = GeoUtils.geoDistanceInKm(
                                googleSteps.getEnd_location(),
                                gRoutesOferta[0].getLegs().get(0).getEnd_location()
                            );

                            //                            if (result1 > 0.0 && result1 <= 100.0) {
                            //                                destino.set(result1);
                            //                            } else
                            if (result2 > 0.0 && result2 <= 100.0) {
                                destino.set(result2);
                            }
                        }
                    });
                if ((origem.get() != null && origem.get() <= 100) && (destino.get() != null && destino.get() <= 100)) {
                    selected.add(oferta.getOfertas());
                }
            });
        });

        return selected;
    }

    public List<OfertasDTO> getNearRoute(@PathVariable Long id) {
        Optional<RotasOfertasDTO> dto = rotasOfertasService.findByIdOferta(id);
        List<OfertasDTO> selected = new ArrayList<>();
        Gson g = new Gson();

        if (dto.isPresent()) {
            RotasOfertasDTO rotasOfertasDTO = dto.get();
            GoogleRoutes[] gRoutesArr = g.fromJson(rotasOfertasDTO.getRotas(), GoogleRoutes[].class);

            GoogleRoutes gRoutes = gRoutesArr[0];

            List<RotasOfertasDTO> allRoutes = rotasOfertasService.findAllNotPerfilId(
                rotasOfertasDTO.getOfertas().getPerfil().getId(),
                rotasOfertasDTO.getOfertas().getTipoOferta() == TipoOferta.CARGA ? TipoOferta.VAGAS : TipoOferta.CARGA,
                StatusOferta.AGUARDANDO_PROPOSTA
            );

            allRoutes.forEach(oferta -> {
                if (oferta.getId().equals(rotasOfertasDTO.getId())) {
                    return;
                }

                GoogleRoutes[] gRoutesOferta = g.fromJson(oferta.getRotas(), GoogleRoutes[].class);

                GoogleLegs googleLegs = gRoutes.getLegs().get(0);

                AtomicReference<Double> origem = new AtomicReference<>();
                AtomicReference<Double> destino = new AtomicReference<>();

                googleLegs
                    .getSteps()
                    .forEach(googleSteps -> {
                        if (origem.get() == null) {
                            double result1 = GeoUtils.geoDistanceInKm(
                                googleSteps.getStart_location(),
                                gRoutesOferta[0].getLegs().get(0).getStart_location()
                            );
                            double result2 = GeoUtils.geoDistanceInKm(
                                googleSteps.getEnd_location(),
                                gRoutesOferta[0].getLegs().get(0).getStart_location()
                            );

                            if (result1 > 0.0 && result1 <= 100.0) {
                                origem.set(result1);
                            } else if (result2 > 0.0 && result2 <= 100) {
                                origem.set(result2);
                            }
                        }

                        if (destino.get() == null) {
                            double result1 = GeoUtils.geoDistanceInKm(
                                googleSteps.getStart_location(),
                                gRoutesOferta[0].getLegs().get(0).getEnd_location()
                            );
                            double result2 = GeoUtils.geoDistanceInKm(
                                googleSteps.getEnd_location(),
                                gRoutesOferta[0].getLegs().get(0).getEnd_location()
                            );

                            if (result1 > 0.0 && result1 <= 100.0) {
                                destino.set(result1);
                            } else if (result2 > 0.0 && result2 <= 100.0) {
                                destino.set(result2);
                            }
                        }
                    });

                if ((origem.get() != null && origem.get() <= 100) && (destino.get() != null && destino.get() <= 100)) {
                    selected.add(oferta.getOfertas());
                }
            });
        }

        return selected;
    }

    private OfertasDTO createPortal(OfertasDTO ofertasDTO) {
        var ofertas = ofertasRepository.save(ofertasMapper.toEntity(ofertasDTO));
        facebookController.createRegistrationOffer(ofertasDTO);

        if (ofertas.getId() != null) {
            rotasOfertasService.saveNewRoute(ofertasMapper.toDto(ofertas));
        }

        return ofertasMapper.toDto(ofertas);
    }
}
