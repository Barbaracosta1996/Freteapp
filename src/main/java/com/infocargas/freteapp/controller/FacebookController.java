package com.infocargas.freteapp.controller;

import com.infocargas.freteapp.config.Constants;
import com.infocargas.freteapp.domain.User;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.WhatsAppType;
import com.infocargas.freteapp.domain.enumeration.WhatsStatus;
import com.infocargas.freteapp.proxy.FacebookApiProxy;
import com.infocargas.freteapp.response.facebook.FacebookResponseChange;
import com.infocargas.freteapp.response.facebook.FacebookSendResponse;
import com.infocargas.freteapp.service.WhatsMessageBatchService;
import com.infocargas.freteapp.service.dto.*;
import com.infocargas.freteapp.web.rest.vm.facebook.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class FacebookController {

    private final Logger log = LoggerFactory.getLogger(FacebookController.class);

    private final FacebookApiProxy facebookApiProxy;

    private final WhatsMessageBatchService whatsMessageBatchService;

    public FacebookController(FacebookApiProxy facebookApiProxy, WhatsMessageBatchService whatsMessageBatchService) {
        this.facebookApiProxy = facebookApiProxy;
        this.whatsMessageBatchService = whatsMessageBatchService;
    }

    /**
     * Create new registration message to register user.
     * @param perfilDTO register data.
     */
    public FacebookSendResponse createRegistrationMessage(PerfilDTO perfilDTO) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setPhoneWhatsApp("55" + perfilDTO.getUser().getPhone());
        message.setType("template");
        message.setTemplate(getTemplate("welcome_new_customer"));

        List<FacebookComponetsVM> componets = new ArrayList<>();

        /*
           ---------------- Header ----------
        */
        FacebookComponetsVM header = new FacebookComponetsVM();
        header.setType("header");
        List<FacebookParameterVM> headerParameters = new ArrayList<>();
        FacebookParameterVM headerParam = new FacebookParameterVM();
        headerParam.setText(perfilDTO.getUser().getName());
        headerParam.setType("text");
        headerParameters.add(headerParam);

        header.setParameters(headerParameters);

        /*
           ---------------- Body ----------
        */

        FacebookComponetsVM body = new FacebookComponetsVM();
        body.setType("body");
        List<FacebookParameterVM> bodyParameters = new ArrayList<>();
        FacebookParameterVM bodyParam = new FacebookParameterVM();
        bodyParam.setText(perfilDTO.getTipoConta().name().toLowerCase());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        body.setParameters(bodyParameters);

        componets.add(header);
        componets.add(body);

        message.getTemplate().setComponents(componets);

        return sendNotification(message);
    }

    /**
     * Create new registration message to register user.
     * @param ofertas register data.
     */
    public FacebookSendResponse createAlertTransportIndication(OfertasDTO ofertas, Integer vagas) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        log.info("Telefone enviado " + ofertas.getId());
        log.info("Telefone: " + ofertas.getPerfil().getUser().getPhone());
        message.setPhoneWhatsApp("55" + ofertas.getPerfil().getUser().getPhone());
        message.setType("template");
        message.setTemplate(getTemplate("alert_indications_transport"));

        List<FacebookComponetsVM> componets = new ArrayList<>();

        /*
           ---------------- Header ----------
        */
        FacebookComponetsVM header = new FacebookComponetsVM();
        header.setType("header");

        log.info("Nome enviado " + ofertas.getId());
        log.info("Nome: " + ofertas.getPerfil().getUser().getName());

        List<FacebookParameterVM> headerParameters = new ArrayList<>();
        FacebookParameterVM headerParam = new FacebookParameterVM();
        headerParam.setText(ofertas.getPerfil().getNome());
        headerParam.setType("text");
        headerParameters.add(headerParam);

        header.setParameters(headerParameters);

        /*
           ---------------- Body ----------
        */

        FacebookComponetsVM body = new FacebookComponetsVM();
        body.setType("body");
        List<FacebookParameterVM> bodyParameters = new ArrayList<>();

        FacebookParameterVM bodyParam = new FacebookParameterVM();
        bodyParam.setText(vagas.toString());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(ofertas.getOrigem());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(ofertas.getDestino());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        body.setParameters(bodyParameters);

        componets.add(header);
        componets.add(body);

        message.getTemplate().setComponents(componets);

        return sendNotification(message);
    }

    /**
     * send link with token to reset password.
     */
    public void resetPassword(User user) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setPhoneWhatsApp("55" + user.getTelephoneNumber());
        message.setType("template");
        message.setTemplate(getTemplate("reset_password_user"));

        List<FacebookComponetsVM> componets = new ArrayList<>();

        /*
           ---------------- Header ----------
        */
        FacebookComponetsVM header = new FacebookComponetsVM();
        header.setType("header");
        List<FacebookParameterVM> headerParameters = new ArrayList<>();
        FacebookParameterVM headerParam = new FacebookParameterVM();
        headerParam.setText(user.getFirstName());
        headerParam.setType("text");
        headerParameters.add(headerParam);

        header.setParameters(headerParameters);

        /*
           ---------------- Body ----------
        */

        FacebookComponetsVM buttons = new FacebookComponetsVM();
        buttons.setType("button");
        buttons.setSubType("url");
        buttons.setIndex(0);
        List<FacebookParameterVM> parameter = new ArrayList<>();
        FacebookParameterVM bodyParam = new FacebookParameterVM();
        bodyParam.setText(user.getResetKey());
        bodyParam.setType("text");
        parameter.add(bodyParam);

        buttons.setParameters(parameter);

        componets.add(header);
        componets.add(buttons);

        message.getTemplate().setComponents(componets);

        sendNotification(message);
    }

    public void createRegistrationOffer(OfertasDTO ofertasDTO) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setPhoneWhatsApp("55" + ofertasDTO.getPerfil().getUser().getPhone());
        message.setType("template");

        if (ofertasDTO.getTipoOferta() == TipoOferta.VAGAS) {
            message.setTemplate(getTemplate("new_vacancy_notification"));
        } else {
            message.setTemplate(getTemplate("new_cargo_notification"));
        }

        List<FacebookComponetsVM> componets = new ArrayList<>();

        /*
           ---------------- Header ----------
        */
        FacebookComponetsVM header = new FacebookComponetsVM();
        header.setType("header");
        List<FacebookParameterVM> headerParameters = new ArrayList<>();
        FacebookParameterVM headerParam = new FacebookParameterVM();
        headerParam.setText(ofertasDTO.getPerfil().getNome());
        headerParam.setType("text");
        headerParameters.add(headerParam);
        header.setParameters(headerParameters);

        /*
           ---------------- Body ----------
        */

        FacebookComponetsVM body = new FacebookComponetsVM();
        body.setType("body");
        List<FacebookParameterVM> bodyParameters = new ArrayList<>();

        FacebookParameterVM bodyParam = new FacebookParameterVM();
        bodyParam.setText(ofertasDTO.getQuantidade().toString());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        body.setParameters(bodyParameters);

        FacebookParameterVM bodyParam1 = new FacebookParameterVM();
        bodyParam1.setText(ofertasDTO.getDataFechamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        bodyParam1.setType("text");
        bodyParameters.add(bodyParam1);

        FacebookParameterVM bodyParam2 = new FacebookParameterVM();
        bodyParam2.setText(ofertasDTO.getOrigem());
        bodyParam2.setType("text");
        bodyParameters.add(bodyParam2);

        FacebookParameterVM bodyParam3 = new FacebookParameterVM();
        bodyParam3.setText(ofertasDTO.getDestino());
        bodyParam3.setType("text");
        bodyParameters.add(bodyParam3);

        componets.add(header);
        componets.add(body);

        message.getTemplate().setComponents(componets);

        var facebookMessage = sendNotification(message);

        if (facebookMessage != null && facebookMessage.getError() == null) {
            var newMessageBatch = new WhatsMessageBatchDTO();
            newMessageBatch.setStatus(WhatsStatus.CLOSED);
            newMessageBatch.setWaidTo(facebookMessage.getMessages().get(0).getId());
            newMessageBatch.setPerfilID(ofertasDTO.getPerfil().getId().intValue());
            newMessageBatch.setTipo(WhatsAppType.CREATE_OFFER);
            whatsMessageBatchService.save(newMessageBatch);
        }
    }

    public FacebookSendResponse sendRequestCargo(SolicitacaoDTO solicitacao) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setPhoneWhatsApp("55" + solicitacao.getPerfil().getUser().getPhone());
        message.setType("template");

        message.setTemplate(getTemplate("new_requests_cargo"));

        List<FacebookComponetsVM> componets = new ArrayList<>();

        /*
           ---------------- Header ----------
        */
        FacebookComponetsVM header = new FacebookComponetsVM();
        header.setType("header");
        List<FacebookParameterVM> headerParameters = new ArrayList<>();
        FacebookParameterVM headerParam = new FacebookParameterVM();
        headerParam.setText(solicitacao.getPerfil().getNome());
        headerParam.setType("text");
        headerParameters.add(headerParam);
        header.setParameters(headerParameters);

        /*
           ---------------- Body ----------
        */

        FacebookComponetsVM body = new FacebookComponetsVM();
        body.setType("body");
        List<FacebookParameterVM> bodyParameters = new ArrayList<>();

        FacebookParameterVM bodyParam = new FacebookParameterVM();
        bodyParam.setText(
            solicitacao.getOfertas().getPerfil().getTipoConta().name().toLowerCase() + " " + solicitacao.getOfertas().getPerfil().getNome()
        );
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getQuantidade().toString());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getOrigem());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getDestino());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getTipoCarga().name());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getTipoTransporte().name());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        body.setParameters(bodyParameters);

        componets.add(header);
        componets.add(body);

        message.getTemplate().setComponents(componets);

        return sendNotification(message);
    }

    public FacebookSendResponse sendRequestVacancies(SolicitacaoDTO solicitacao) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setPhoneWhatsApp("55" + solicitacao.getPerfil().getUser().getPhone());
        message.setType("template");

        message.setTemplate(getTemplate("new_requests_cargo"));

        List<FacebookComponetsVM> componets = new ArrayList<>();

        /*
           ---------------- Header ----------
        */
        FacebookComponetsVM header = new FacebookComponetsVM();
        header.setType("header");
        List<FacebookParameterVM> headerParameters = new ArrayList<>();
        FacebookParameterVM headerParam = new FacebookParameterVM();
        headerParam.setText(solicitacao.getPerfil().getNome());
        headerParam.setType("text");
        headerParameters.add(headerParam);
        header.setParameters(headerParameters);

        /*
           ---------------- Body ----------
        */

        FacebookComponetsVM body = new FacebookComponetsVM();
        body.setType("body");
        List<FacebookParameterVM> bodyParameters = new ArrayList<>();

        FacebookParameterVM bodyParam = new FacebookParameterVM();
        bodyParam.setText(
            solicitacao.getOfertas().getPerfil().getTipoConta().name().toLowerCase() + " " + solicitacao.getOfertas().getPerfil().getNome()
        );
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getQuantidade().toString());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getOrigem());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getDestino());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getPerfil().getNome());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getTipoTransporte().name());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        body.setParameters(bodyParameters);

        componets.add(header);
        componets.add(body);

        message.getTemplate().setComponents(componets);

        return sendNotification(message);
    }

    /**
     * Envia uma mensagem única para clietne
     * @param phone número do telefone
     * @param body corpo da mensagem.
     * @returne
     */
    public FacebookSendResponse sendOneMessage(String phone, String body) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setPhoneWhatsApp("55" + phone);
        message.setType("text");

        Map<String, Object> messageBody = new HashMap<>();

        messageBody.put("preview_url", false);
        messageBody.put("body", body);

        message.setMessageBody(messageBody);

        return sendNotification(message);
    }

    public FacebookSendResponse sendNegativeMessage(SolicitacaoDTO solicitacao) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setType("interactive");
        message.setPhoneWhatsApp("55" + solicitacao.getOfertas().getPerfil().getUser().getPhone());

        List<FacebookComponetsVM> componets = new ArrayList<>();

        /*
           ---------------- Header ----------
        */
        FacebookComponetsVM header = new FacebookComponetsVM();
        header.setType("header");
        List<FacebookParameterVM> headerParameters = new ArrayList<>();
        FacebookParameterVM headerParam = new FacebookParameterVM();
        headerParam.setText(solicitacao.getOfertas().getPerfil().getUser().getName());
        headerParam.setType("text");
        headerParameters.add(headerParam);

        header.setParameters(headerParameters);

        /*
           ---------------- Body ----------
        */

        FacebookComponetsVM body = new FacebookComponetsVM();
        body.setType("body");
        List<FacebookParameterVM> bodyParameters = new ArrayList<>();
        FacebookParameterVM bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getQuantidade().toString());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getOrigem());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getDestino());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        body.setParameters(bodyParameters);

        componets.add(header);
        componets.add(body);

        message.getTemplate().setComponents(componets);

        return sendNotification(message);
    }

    public FacebookSendResponse sendNearsRouteNotifcation(List<OfertasDTO> ofertas, RotasOfertasDTO requested) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setType("interactive");
        message.setPhoneWhatsApp("55" + requested.getOfertas().getPerfil().getUser().getPhone());

        FacebookInteractiveVM interactive = new FacebookInteractiveVM();
        interactive.setType("list");

        //        FacebookParameterVM header = new FacebookParameterVM();
        //        header.setType("text");
        //        header.setText("Olá " + requested.getOfertas().getPerfil().getNome());
        //        interactive.setHeader(header);

        Map<String, String> body = new HashMap<>();
        body.put("text", "Para acessar a lista de inidicações clique em \"Ver Lista\" abaixo.");
        interactive.setBody(body);

        Map<String, String> footer = new HashMap<>();
        footer.put("text", "Tá on? Transportou!");
        interactive.setFooter(footer);

        FacebookActionsVM actions = new FacebookActionsVM();
        actions.setButton("Ver Lista");

        FacebookSectionsVM sectionsVM = new FacebookSectionsVM();
        sectionsVM.setTitle("Indicações");

        List<FacebookRowsVM> rows = new ArrayList<>();

        ofertas.forEach(ofertasDTO -> {
            FacebookRowsVM row = new FacebookRowsVM();
            row.setId(ofertasDTO.getId().toString());
            row.setTitle(
                ofertasDTO.getDataFechamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " | vagas: " + ofertasDTO.getQuantidade()
            );
            String text =
                "de: " + ofertasDTO.getOrigem() + " para: " + ofertasDTO.getDestino() + "Responsável:" + ofertasDTO.getPerfil().getNome();
            if (text.length() > 70) {
                row.setDescription(text.substring(0, 70));
            } else {
                row.setDescription(text);
            }

            rows.add(row);
        });

        sectionsVM.setRows(rows);

        List<FacebookSectionsVM> sections = new ArrayList<>();
        sections.add(sectionsVM);

        actions.setSections(sections);

        interactive.setAction(actions);
        message.setInteractive(interactive);

        return sendNotification(message);
    }

    public FacebookSendResponse sendContact(SolicitacaoDTO solicitacao) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setType("contacts");
        message.setPhoneWhatsApp("55" + solicitacao.getRequestedPerfil().getUser().getPhone());

        List<FacebookContactVM> facebookContactVMS = new ArrayList<>();

        FacebookContactVM contactVM = new FacebookContactVM();

        if (solicitacao.getPerfil().getCidade() != null && solicitacao.getPerfil().getEstado() != null) {
            Map<String, String> address = new HashMap<>();
            address.put("city", solicitacao.getPerfil().getCidade());
            address.put("state", solicitacao.getPerfil().getEstado());
            contactVM.setAddresses(address);
        }

        List<Map<String, String>> emails = new ArrayList<>();

        Map<String, String> email = new HashMap<>();
        email.put("email", solicitacao.getPerfil().getUser().getEmail());
        email.put("type", "Infocargas");

        emails.add(email);

        contactVM.setEmails(emails);

        Map<String, String> name = new HashMap<>();
        name.put("formatted_name", solicitacao.getPerfil().getNome());
        List<String> nameSplit = List.of(solicitacao.getPerfil().getNome().split(" "));
        name.put("first_name", nameSplit.get(0));
        if (nameSplit.size() > 1) {
            name.put("last_name", nameSplit.get(nameSplit.size() - 1));
        }

        contactVM.setName(name);

        Map<String, String> org = new HashMap<>();
        org.put("company", solicitacao.getPerfil().getTipoConta().name().toLowerCase());
        org.put("department", solicitacao.getOfertas().getTipoTransporte().name().toLowerCase());

        contactVM.setOrg(org);

        List<Map<String, String>> phones = new ArrayList<>();

        Map<String, String> phone = new HashMap<>();
        phone.put("phone", solicitacao.getPerfil().getUser().getPhone());
        phone.put("type", "Whatsapp");
        phone.put("wa_id", solicitacao.getPerfil().getWaId());

        phones.add(phone);

        contactVM.setPhones(phones);

        facebookContactVMS.add(contactVM);

        message.setContacts(facebookContactVMS);

        return sendNotification(message);
    }

    public FacebookSendResponse sendContactPerfil(PerfilDTO perfil, String telefone) {
        FacebookMessageVM message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setType("contacts");
        message.setPhoneWhatsApp("55" + telefone);

        List<FacebookContactVM> facebookContactVMS = new ArrayList<>();

        FacebookContactVM contactVM = new FacebookContactVM();

        if (perfil.getCidade() != null && perfil.getEstado() != null) {
            Map<String, String> address = new HashMap<>();
            address.put("city", perfil.getCidade());
            address.put("state", perfil.getEstado());

            contactVM.setAddresses(address);
        }

        List<Map<String, String>> emails = new ArrayList<>();

        Map<String, String> email = new HashMap<>();
        email.put("email", perfil.getUser().getEmail());
        email.put("type", "Infocargas");

        emails.add(email);

        contactVM.setEmails(emails);

        Map<String, String> name = new HashMap<>();
        name.put("formatted_name", perfil.getNome());
        List<String> nameSplit = List.of(perfil.getNome().split(""));
        name.put("first_name", nameSplit.get(0));
        if (nameSplit.size() > 1) {
            name.put("last_name", nameSplit.get(nameSplit.size() - 1));
        }

        contactVM.setName(name);

        Map<String, String> org = new HashMap<>();
        org.put("company", perfil.getTipoConta().name().toLowerCase());

        contactVM.setOrg(org);

        List<Map<String, String>> phones = new ArrayList<>();

        Map<String, String> phone = new HashMap<>();
        phone.put("phone", perfil.getUser().getPhone());
        phone.put("type", "Whatsapp");
        phone.put("wa_id", perfil.getWaId());

        phones.add(phone);

        contactVM.setPhones(phones);

        facebookContactVMS.add(contactVM);

        message.setContacts(facebookContactVMS);

        return sendNotification(message);
    }

    public FacebookSendResponse sendDoneMatch(SolicitacaoDTO solicitacao) {
        var message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setPhoneWhatsApp("55" + solicitacao.getPerfil().getUser().getPhone());
        message.setType("template");

        message.setTemplate(getTemplate("match_done_message"));

        List<FacebookComponetsVM> componets = new ArrayList<>();

        /*
           ---------------- Header ----------
        */
        FacebookComponetsVM header = new FacebookComponetsVM();
        header.setType("header");
        List<FacebookParameterVM> headerParameters = new ArrayList<>();
        FacebookParameterVM headerParam = new FacebookParameterVM();
        headerParam.setText(solicitacao.getPerfil().getNome());
        headerParam.setType("text");
        headerParameters.add(headerParam);
        header.setParameters(headerParameters);

        /*
           ---------------- Body ----------
        */

        FacebookComponetsVM body = new FacebookComponetsVM();
        body.setType("body");
        List<FacebookParameterVM> bodyParameters = new ArrayList<>();

        FacebookParameterVM bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getOrigem());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getDestino());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getDataFechamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getRequestedPerfil().getNome());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getRequestedPerfil().getUser().getPhone());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getRequestedPerfil().getUser().getLogin());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getQuantidade().toString());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        body.setParameters(bodyParameters);

        componets.add(header);
        componets.add(body);

        message.getTemplate().setComponents(componets);

        return sendNotification(message);
    }

    public FacebookSendResponse sendDoneRequested(SolicitacaoDTO solicitacao) {
        var message = new FacebookMessageVM();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setPhoneWhatsApp("55" + solicitacao.getRequestedPerfil().getUser().getPhone());
        message.setType("template");

        message.setTemplate(getTemplate("match_done_message"));

        List<FacebookComponetsVM> componets = new ArrayList<>();

        /*
           ---------------- Header ----------
        */
        FacebookComponetsVM header = new FacebookComponetsVM();
        header.setType("header");
        List<FacebookParameterVM> headerParameters = new ArrayList<>();
        FacebookParameterVM headerParam = new FacebookParameterVM();
        headerParam.setText(solicitacao.getRequestedPerfil().getNome());
        headerParam.setType("text");
        headerParameters.add(headerParam);
        header.setParameters(headerParameters);

        /*
           ---------------- Body ----------
        */

        FacebookComponetsVM body = new FacebookComponetsVM();
        body.setType("body");
        List<FacebookParameterVM> bodyParameters = new ArrayList<>();

        FacebookParameterVM bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getOrigem());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getDestino());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getDataFechamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getPerfil().getNome());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getPerfil().getUser().getPhone());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getPerfil().getUser().getLogin());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        bodyParam = new FacebookParameterVM();
        bodyParam.setText(solicitacao.getOfertas().getQuantidade().toString());
        bodyParam.setType("text");
        bodyParameters.add(bodyParam);

        body.setParameters(bodyParameters);

        componets.add(header);
        componets.add(body);

        message.getTemplate().setComponents(componets);

        return sendNotification(message);
    }

    public void sendRedirectMessage(FacebookResponseChange facebookResponse, Optional<PerfilDTO> perfil) {
        try {
            var nomeContato = "Não Cadastrado";
            var numeroContato = "Não foi Localizado";
            var emailContato = "Não Cadastrado";
            var codigoSistema = "Não Cadastrado";
            var mensagemContato = facebookResponse.getValue().getMessages().get(0).getText().getBody();

            if (perfil.isPresent()) {
                var perfilDTO = perfil.get();
                numeroContato = perfilDTO.getWaId();
                nomeContato = perfilDTO.getNome();
                codigoSistema = perfilDTO.getId().toString();
                emailContato = perfilDTO.getUser().getLogin();
            } else {
                var contact = facebookResponse.getValue().getContacts().get(0);
                if (contact.getProfile().isEmpty()) {
                    nomeContato = contact.getWaId();
                } else {
                    nomeContato = contact.getProfile().get("name");
                }
                numeroContato = contact.getWaId();
            }

            var message = new FacebookMessageVM();
            message.setMessagingProduct("whatsapp");
            message.setRecipientType("individual");

            if (numeroContato.equals("553197460910") || numeroContato.equals("5531997460910")) {
                log.warn("O número de telefone não pode ser o mesmo para não dar loop infinito.");
                return;
            }

            message.setPhoneWhatsApp("5531997460910");
            message.setType("template");

            message.setTemplate(getTemplate("redirect_message_support"));

            List<FacebookComponetsVM> componets = new ArrayList<>();

            /*
                  ---------------- Body ----------
            */

            FacebookComponetsVM body = new FacebookComponetsVM();
            body.setType("body");
            List<FacebookParameterVM> bodyParameters = new ArrayList<>();

            FacebookParameterVM bodyParam = new FacebookParameterVM();
            bodyParam.setText(nomeContato);
            bodyParam.setType("text");
            bodyParameters.add(bodyParam);

            bodyParam = new FacebookParameterVM();
            bodyParam.setText(numeroContato);
            bodyParam.setType("text");
            bodyParameters.add(bodyParam);

            bodyParam = new FacebookParameterVM();
            bodyParam.setText(mensagemContato);
            bodyParam.setType("text");
            bodyParameters.add(bodyParam);

            bodyParam = new FacebookParameterVM();
            bodyParam.setText(codigoSistema);
            bodyParam.setType("text");
            bodyParameters.add(bodyParam);

            bodyParam = new FacebookParameterVM();
            bodyParam.setText(emailContato);
            bodyParam.setType("text");
            bodyParameters.add(bodyParam);

            bodyParam = new FacebookParameterVM();
            bodyParam.setText(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            bodyParam.setType("text");
            bodyParameters.add(bodyParam);

            body.setParameters(bodyParameters);

            componets.add(body);

            message.getTemplate().setComponents(componets);

            sendNotification(message);

            sendRedirectAlert(numeroContato);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendRedirectAlert(String phone) {
        try {
            var message = new FacebookMessageVM();
            message.setMessagingProduct("whatsapp");
            message.setRecipientType("individual");
            message.setPhoneWhatsApp(phone);
            message.setType("template");

            message.setTemplate(getTemplate("redirect_message_info"));

            sendNotification(message);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendAlertUser(String phone) {
        try {
            var message = new FacebookMessageVM();
            message.setMessagingProduct("whatsapp");
            message.setRecipientType("individual");
            message.setPhoneWhatsApp(phone);
            message.setType("template");

            message.setTemplate(getTemplate("redirect_error_message"));

            sendNotification(message);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private FacebookSendResponse sendNotification(FacebookMessageVM message) {
        try {
            var body = facebookApiProxy.createMessage("whatsapp", Constants.FACEBOOK_TOKEN, message);
            return body;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return null;
    }

    private FacebookTemplateVM getTemplate(String name) {
        FacebookTemplateVM templateVM = new FacebookTemplateVM();

        templateVM.setName(name);

        Map<String, String> language = new HashMap<>();
        language.put("code", "pt_BR");
        templateVM.setLanguage(language);

        return templateVM;
    }

    public void updateMessageStatus(FacebookResponseChange facebookResponse, String status) {
        if (facebookResponse.getValue() != null && facebookResponse.getValue().getMessages() != null) {
            FacebookMessageVM message = new FacebookMessageVM();
            message.setMessagingProduct("whatsapp");
            message.setStatus(status);
            message.setMessageId(facebookResponse.getValue().getMessages().get(0).getId());

            sendNotification(message);
        }
    }
}
