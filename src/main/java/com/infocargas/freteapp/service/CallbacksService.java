package com.infocargas.freteapp.service;

import com.google.gson.Gson;
import com.infocargas.freteapp.controller.FacebookController;
import com.infocargas.freteapp.domain.enumeration.*;
import com.infocargas.freteapp.repository.OfertasRepository;
import com.infocargas.freteapp.response.facebook.*;
import com.infocargas.freteapp.service.dto.*;
import com.infocargas.freteapp.service.dto.routes.GoogleLegs;
import com.infocargas.freteapp.service.dto.routes.GoogleRoutes;
import com.infocargas.freteapp.service.mapper.OfertasMapper;
import com.infocargas.freteapp.utils.GeoUtils;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CallbacksService {

    private final Logger logger = LoggerFactory.getLogger(CallbacksService.class);

    private final FacebookController facebookController;

    private final WhatsMessageBatchService whatsMessageBatchService;

    private final PerfilService perfilService;

    private final RotasOfertasService rotasOfertasService;

    private final SolicitacaoService solicitacaoService;

    private final OfertasRepository ofertasRepository;

    private final OfertasMapper ofertasMapper;

    public CallbacksService(
        FacebookController facebookController,
        WhatsMessageBatchService whatsMessageBatchService,
        PerfilService perfilService,
        RotasOfertasService rotasOfertasService,
        SolicitacaoService solicitacaoService,
        OfertasRepository ofertasRepository,
        OfertasMapper ofertasMapper
    ) {
        this.facebookController = facebookController;
        this.whatsMessageBatchService = whatsMessageBatchService;
        this.perfilService = perfilService;
        this.rotasOfertasService = rotasOfertasService;
        this.solicitacaoService = solicitacaoService;
        this.ofertasRepository = ofertasRepository;
        this.ofertasMapper = ofertasMapper;
    }

    /**
     * Verifica Status da Mensagem para não ficar a enviar dados antigo para o usuário.
     * @param facebookResponseStatus status da mensagem
     * @return True ou false. Verdadeiro continua o processo da api.
     */
    public Boolean checkingStatusMessage(FacebookResponseStatus facebookResponseStatus) {
        var messageOptional = this.whatsMessageBatchService.findByMessageIdWithoutStatus(facebookResponseStatus.getId());

        var message = new WhatsMessageBatchDTO();
        if (messageOptional.isEmpty()) {
            message = newMessageStatus(facebookResponseStatus);
            messageOptional.add(message);
        }

        return setStatusMessage(facebookResponseStatus, messageOptional);
    }

    private Boolean setStatusMessage(FacebookResponseStatus facebookResponseStatus, List<WhatsMessageBatchDTO> messages) {
        AtomicReference<Boolean> result = new AtomicReference<>(false);
        messages.forEach(message -> {
            switch (facebookResponseStatus.getStatus()) {
                case "read":
                    if (message.getStatusMessage() != MessageFacebookWPStatus.READ) {
                        message.setStatusMessage(MessageFacebookWPStatus.READ);
                        whatsMessageBatchService.save(message);
                    }
                    result.set(false);
                case "delivered":
                    if (message.getStatusMessage() != MessageFacebookWPStatus.DELIVERY) {
                        message.setStatusMessage(MessageFacebookWPStatus.DELIVERY);
                        whatsMessageBatchService.save(message);
                    }
                    result.set(false);
                case "sent":
                    if (message.getStatusMessage() != MessageFacebookWPStatus.SENT) {
                        message.setStatusMessage(MessageFacebookWPStatus.SENT);
                        whatsMessageBatchService.save(message);
                    }
                    result.set(false);
                default:
                    result.set(!message.getStatus().equals(WhatsStatus.CLOSED));
            }
        });
        return result.get();
    }

    private WhatsMessageBatchDTO newMessageStatus(FacebookResponseStatus facebookResponseStatus) {
        var message = new WhatsMessageBatchDTO();
        var perfil = perfilService.findByWaId(facebookResponseStatus.getRecipientId());
        if (facebookResponseStatus.getStatus().equals("read")) {
            message.setStatusMessage(MessageFacebookWPStatus.READ);
        } else if (facebookResponseStatus.getStatus().equals("delivered")) {
            message.setStatusMessage(MessageFacebookWPStatus.DELIVERY);
        } else {
            message.setStatusMessage(MessageFacebookWPStatus.SENT);
        }

        if (perfil.isPresent()) {
            message.setPerfilID(perfil.get().getId().intValue());
        } else {
            message.setPerfilID(0);
        }
        message.setWaidTo(facebookResponseStatus.getId());
        message.setTipo(WhatsAppType.REDIRECT);
        message.setStatus(WhatsStatus.CLOSED);

        return whatsMessageBatchService.save(message);
    }

    /**
     * Atualiza o status da mensagem do cliente para entregue e lida.
     * @param facebookResponse dados da mensagem.
     */
    public void updateStatusMessage(FacebookResponseChange facebookResponse) {
        facebookController.updateMessageStatus(facebookResponse, "delivered");
        facebookController.updateMessageStatus(facebookResponse, "read");
    }

    /***
     * Envia o retorno da oferta escolhida na lista e envia para cliente.
     * @param interactive resposta do botão
     * @param message mensagem
     */
    public void processListAlerts(FacebookResponseInteractive interactive, WhatsMessageBatchDTO message) {
        if (interactive.getListReply() != null) {
            var minhaOferta = rotasOfertasService.findByIdOferta(message.getOfertaId());

            var ofertaId = interactive.getListReply().get("id");
            var ofertaEscolhida = rotasOfertasService.findByIdOferta(Long.valueOf(ofertaId));

            if (
                (ofertaEscolhida.isPresent() && ofertaEscolhida.get().getOfertas().getStatus() != StatusOferta.AGUARDANDO_PROPOSTA) ||
                (ofertaEscolhida.isPresent() && ofertaEscolhida.get().getOfertas().getStatus() != StatusOferta.AGUARDANDO_PROPOSTA)
            ) {
                minhaOferta.ifPresent(rotasOfertasDTO -> {
                    facebookController.sendOneMessage(
                        rotasOfertasDTO.getOfertas().getPerfil().getUser().getPhone(),
                        "A solicitação foi cancelada porque o transporte já foi encerrado. Assim que surgir novas indicações enviaremos aqui."
                    );
                    message.setStatus(WhatsStatus.CLOSED);
                    whatsMessageBatchService.save(message);
                });
            } else {
                ofertaEscolhida.ifPresent(requested -> {
                    FacebookSendResponse response;

                    if (minhaOferta.isPresent()) {
                        SolicitacaoDTO solicitacaoDTO = new SolicitacaoDTO();
                        solicitacaoDTO.setRequestedPerfil(minhaOferta.get().getOfertas().getPerfil());
                        solicitacaoDTO.setPerfil(ofertaEscolhida.get().getOfertas().getPerfil());
                        solicitacaoDTO.setOfertas(minhaOferta.get().getOfertas());
                        solicitacaoDTO.setMinhaOferta(ofertaEscolhida.get().getOfertas());
                        solicitacaoDTO.setDataProposta(ZonedDateTime.now());
                        solicitacaoDTO.setStatus(StatusSolicitacao.AGUARDANDORESPOSTA);

                        solicitacaoDTO = solicitacaoService.save(solicitacaoDTO);
                        solicitacaoDTO.setPerfil(perfilService.findOne(ofertaEscolhida.get().getOfertas().getPerfil().getId()).get());
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
                            message.setStatus(WhatsStatus.CLOSED);
                            whatsMessageBatchService.save(message);

                            var newMessageBatch = new WhatsMessageBatchDTO();
                            newMessageBatch.setStatus(WhatsStatus.OPEN);
                            newMessageBatch.setWaidTo(response.getMessages().get(0).getId());
                            newMessageBatch.setTipoOferta(solicitacaoDTO.getOfertas().getTipoOferta());
                            newMessageBatch.setOfertaId(solicitacaoDTO.getId());
                            newMessageBatch.setPerfilID(solicitacaoDTO.getPerfil().getId().intValue());
                            newMessageBatch.setTipo(WhatsAppType.LIST_ALERT_CHOOSED);
                            whatsMessageBatchService.save(newMessageBatch);
                        }
                    }
                });
            }
        }
    }

    /**
     * Processa a notificação de indicações de transporte e envia a lista de oferta para o cliente
     * @param buttonIndication Botão de indicações
     * @param perfilDTO perfil
     * @param message mensagem
     */
    private void processIndicationAlerts(FacebookButton buttonIndication, Optional<PerfilDTO> perfilDTO, WhatsMessageBatchDTO message) {
        if (buttonIndication.getPayload() != null && buttonIndication.getPayload().equals("Sim")) {
            var ofertasPerfil = rotasOfertasService.findByIdOferta(message.getOfertaId());
            ofertasPerfil.ifPresent(rotasOfertasDTO -> {
                List<OfertasDTO> ofertas = getNearRoute(rotasOfertasDTO.getOfertas().getId());
                var response = facebookController.sendNearsRouteNotifcation(ofertas, rotasOfertasDTO);
                if (response.getError() == null) {
                    message.setStatus(WhatsStatus.CLOSED);
                    whatsMessageBatchService.save(message);

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
    }

    /**
     * Envia oferta selecionada para o motorista/transportador
     * @param buttonChoose oferta escolhida
     * @param message dados para mensagem.
     */
    private void processListaChoosed(FacebookButton buttonChoose, WhatsMessageBatchDTO message) {
        if (buttonChoose.getPayload() != null) {
            var solicitacaoDTO = solicitacaoService.findOne(message.getOfertaId());
            solicitacaoDTO.ifPresent(solicitacao -> {
                solicitacao.setStatus(buttonChoose.getPayload().equals("Sim") ? StatusSolicitacao.ACEITOU : StatusSolicitacao.CANCELOU);
                solicitacao.setAceitar(buttonChoose.getPayload().equals("Sim") ? AnwserStatus.SIM : AnwserStatus.NAO);

                solicitacao = solicitacaoService.save(solicitacao);

                solicitacao.setPerfil(perfilService.findOne(solicitacao.getPerfil().getId()).get());
                solicitacao.setRequestedPerfil(perfilService.findOne(solicitacao.getRequestedPerfil().getId()).get());

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

                    message.setStatus(WhatsStatus.CLOSED);
                    whatsMessageBatchService.save(message);

                    var messages = whatsMessageBatchService.findByNearRouteStatusByOferta(
                        solicitacao.getOfertas().getId(),
                        WhatsStatus.OPEN
                    );

                    messages.forEach(msg -> {
                        msg.setStatus(WhatsStatus.CLOSED);
                        whatsMessageBatchService.save(msg);
                    });

                    messages =
                        whatsMessageBatchService.findByNearRouteStatusByOferta(solicitacao.getMinhaOferta().getId(), WhatsStatus.OPEN);

                    messages.forEach(msg -> {
                        msg.setStatus(WhatsStatus.CLOSED);
                        whatsMessageBatchService.save(msg);
                    });

                    var qte = ofertasRequested.getQuantidade() - ofertas.getQuantidade();

                    if (qte > 0) {
                        ofertasRequested.setId(null);
                        ofertasRequested.setQuantidade(qte);
                        ofertasRequested.setStatus(StatusOferta.AGUARDANDO_PROPOSTA);
                        createOfertasPortal(ofertasRequested);
                    }

                    if (qte < 0) {
                        ofertas.setId(null);
                        ofertas.setQuantidade(Math.abs(qte));
                        ofertasRequested.setStatus(StatusOferta.AGUARDANDO_PROPOSTA);
                        createOfertasPortal(ofertasRequested);
                    }
                }
            });
        }
    }

    /**
     * Cria vaga/cargas quando sobrar após o match.
     * @param ofertasDTO oferta que será criada
     * @return retorna oferta salva.
     */
    private OfertasDTO createOfertasPortal(OfertasDTO ofertasDTO) {
        var ofertas = ofertasRepository.save(ofertasMapper.toEntity(ofertasDTO));
        facebookController.createRegistrationOffer(ofertasDTO);

        if (ofertas.getId() != null) {
            rotasOfertasService.saveNewRoute(ofertasMapper.toDto(ofertas));
        }

        return ofertasMapper.toDto(ofertas);
    }

    /**
     * Verifica resposta do cliente e envia a lista de ofertas.
     * @param buttonChoosePerfil botão com a resporta (SIM ou NÃO)
     * @param message dados da mensagem
     */
    private void processTransportIndicationAlert(FacebookButton buttonChoosePerfil, WhatsMessageBatchDTO message) {
        if (buttonChoosePerfil.getPayload() != null) {
            var ofertaPerfil = rotasOfertasService.findByIdOferta(message.getOfertaId());
            ofertaPerfil.ifPresent(ofertasDTO -> {
                var exibirLista = buttonChoosePerfil.getPayload().equals("SIM");

                if (exibirLista) {
                    ofertasDTO.getOfertas().setPerfil(perfilService.findOne(ofertasDTO.getOfertas().getPerfil().getId()).get());
                    var response = sendNearRoute(ofertasDTO.getOfertas().getId());

                    if (response == null) {
                        logger.error("Falhou o envio para o sistema");
                    } else {
                        message.setStatus(WhatsStatus.CLOSED);
                        whatsMessageBatchService.save(message);

                        var newMessageBatch = new WhatsMessageBatchDTO();
                        newMessageBatch.setStatus(WhatsStatus.OPEN);
                        newMessageBatch.setWaidTo(response.getMessages().get(0).getId());
                        newMessageBatch.setTipoOferta(ofertasDTO.getOfertas().getTipoOferta());
                        newMessageBatch.setOfertaId(ofertasDTO.getOfertas().getId());
                        newMessageBatch.setPerfilID(ofertasDTO.getOfertas().getPerfil().getId().intValue());
                        newMessageBatch.setTipo(WhatsAppType.LIST_ALERT);
                        whatsMessageBatchService.save(newMessageBatch);
                    }
                } else {
                    message.setStatus(WhatsStatus.CLOSED);
                    whatsMessageBatchService.save(message);
                }
            });
        }
    }

    /**
     * Procura ofertas abertas no sistema
     * @return Retorna Lista de ofertas.
     */
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
                            gRoutesOferta[0].getLegs()
                                .forEach(distance -> {
                                    double result1 = GeoUtils.geoDistanceInKm(
                                        googleSteps.getStart_location(),
                                        distance.getStart_location()
                                    );
                                    if (result1 > 0.0 && result1 <= 100.0) {
                                        origem.set(result1);
                                    }
                                });
                        }

                        if (destino.get() == null) {
                            gRoutesOferta[0].getLegs()
                                .forEach(distance -> {
                                    double result2 = GeoUtils.geoDistanceInKm(googleSteps.getEnd_location(), distance.getEnd_location());
                                    if (result2 > 0.0 && result2 <= 100.0) {
                                        destino.set(result2);
                                    }
                                });
                        }
                    });

                if ((origem.get() != null && origem.get() <= 100) && (destino.get() != null && destino.get() <= 100)) {
                    selected.add(oferta.getOfertas());
                }
            });
        }

        return selected;
    }

    /**
     * Gera lista de oferta para o cliente e envia.
     * @param id código da oferta
     * @return retorna resporta do facebookApi
     */
    private FacebookSendResponse sendNearRoute(Long id) {
        List<RotasOfertasDTO> ofertasList = rotasOfertasService.findAllByStatusAndDate(StatusOferta.AGUARDANDO_PROPOSTA);
        var listaRotas = getNearRoute(id);
        var rotasOfertas = rotasOfertasService.findByIdOferta(id);
        rotasOfertas.get().getOfertas().setPerfil(perfilService.findOne(rotasOfertas.get().getOfertas().getPerfil().getId()).get());
        return facebookController.sendNearsRouteNotifcation(listaRotas, rotasOfertas.get());
    }

    /**
     * Processa mensagens enviado via webhook do facebook.
     * @param facebookResponseChange dados da mensagem ou status a ser processado
     * @return retorna status da requisição.
     */
    public ResponseEntity<Void> processingFacebookMessage(FacebookResponseChange facebookResponseChange) {
        String waid = facebookResponseChange.getValue().getContacts().get(0).getWaId();
        Optional<PerfilDTO> perfilDTO = this.perfilService.findByWaId(waid);

        if (perfilDTO.isPresent() && facebookResponseChange.getValue().getMessages().get(0).getContext() != null) {
            String messageId = facebookResponseChange.getValue().getMessages().get(0).getContext().get("id");
            var messages = whatsMessageBatchService.findByMessageIdWithoutStatus(messageId);

            messages.forEach(message -> {
                if (message.getStatus() != WhatsStatus.OPEN) {
                    logger.info("Mensagem cancelada ou fechada.");
                    return;
                }

                switch (message.getTipo()) {
                    case LIST_ALERT:
                        processListAlerts(facebookResponseChange.getValue().getMessages().get(0).getInteractive(), message);
                        break;
                    case INDICATION_ALERT:
                        processIndicationAlerts(facebookResponseChange.getValue().getMessages().get(0).getButton(), perfilDTO, message);
                        break;
                    case INDICATION_ALERT_TRANSPORT:
                        processTransportIndicationAlert(facebookResponseChange.getValue().getMessages().get(0).getButton(), message);
                        break;
                    case LIST_ALERT_CHOOSED:
                        processListaChoosed(facebookResponseChange.getValue().getMessages().get(0).getButton(), message);
                        break;
                    case REDIRECT:
                        sendMessage(facebookResponseChange);
                    default:
                        facebookController.updateMessageStatus(facebookResponseChange, "delivered");
                        facebookController.updateMessageStatus(facebookResponseChange, "read");
                        break;
                }
            });
        } else {
            sendMessage(facebookResponseChange);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Redireciona mensagens não identificadas para o suporte da infocargas.
     * @param facebookResponse dados da mensagem enviada.
     * @return retorna status da requisição.
     */
    public ResponseEntity<Void> sendMessage(FacebookResponseChange facebookResponse) {
        List<WhatsMessageBatchDTO> message = whatsMessageBatchService.findByMessageIdWithoutStatus(
            facebookResponse.getValue().getMessages().get(0).getId()
        );

        var perfil = perfilService.findByWaId(facebookResponse.getValue().getContacts().get(0).getWaId());

        if (message != null) {
            return ResponseEntity.ok().build();
        }

        if (Objects.equals(facebookResponse.getValue().getMessages().get(0).getType(), "text")) {
            facebookController.sendRedirectMessage(facebookResponse, perfil);
        } else {
            facebookController.sendAlertUser(facebookResponse.getValue().getContacts().get(0).getWaId());
        }

        var newMessageBatch = new WhatsMessageBatchDTO();
        newMessageBatch.setStatus(WhatsStatus.CLOSED);
        newMessageBatch.setWaidTo(facebookResponse.getValue().getMessages().get(0).getId());

        if (perfil.isPresent()) {
            newMessageBatch.setPerfilID(perfil.get().getId().intValue());
        } else {
            newMessageBatch.setPerfilID(0);
        }

        newMessageBatch.setTipo(WhatsAppType.REDIRECT);
        whatsMessageBatchService.save(newMessageBatch);

        updateStatusMessage(facebookResponse);

        return ResponseEntity.ok().build();
    }

    public boolean validateMessages(FacebookResponseChange facebookResponseChange) {
        var facebookResponseStatus = facebookResponseChange.getValue().getStatuses();

        if (facebookResponseStatus != null && !checkingStatusMessage(facebookResponseStatus.get(0))) {
            return false;
        } else {
            if (facebookResponseChange.getValue().getMessages() == null || facebookResponseChange.getValue().getMessages().isEmpty()) {
                return true;
            }

            List<WhatsMessageBatchDTO> message = whatsMessageBatchService.findByMessageIdWithoutStatus(
                facebookResponseChange.getValue().getMessages().get(0).getId()
            );

            if (message == null || message.isEmpty()) {
                return true;
            }

            AtomicInteger okStatus = new AtomicInteger();

            message.forEach(msg -> {
                if (msg.getStatus().equals(WhatsStatus.CLOSED)) {
                    okStatus.getAndIncrement();
                }
            });

            return okStatus.get() == message.size();
        }
    }
}
