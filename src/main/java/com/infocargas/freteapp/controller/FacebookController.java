package com.infocargas.freteapp.controller;

import com.infocargas.freteapp.config.Constants;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.proxy.FacebookApiProxy;
import com.infocargas.freteapp.response.facebook.FacebookSendResponse;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import com.infocargas.freteapp.service.dto.RotasOfertasDTO;
import com.infocargas.freteapp.service.dto.SolicitacaoDTO;
import com.infocargas.freteapp.service.schedule.FindNearRouteSchedule;
import com.infocargas.freteapp.web.rest.vm.facebook.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class FacebookController {

    private final Logger log = LoggerFactory.getLogger(FindNearRouteSchedule.class);

    private final FacebookApiProxy facebookApiProxy;

    public FacebookController(FacebookApiProxy facebookApiProxy) {
        this.facebookApiProxy = facebookApiProxy;
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

        sendNotification(message);
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

        FacebookParameterVM header = new FacebookParameterVM();
        header.setType("text");
        header.setText("Olá " + requested.getOfertas().getPerfil().getNome());
        interactive.setHeader(header);

        Map<String, String> body = new HashMap<>();
        body.put(
            "text",
            "Nós encontramos " +
            ofertas.size() +
            " indicações de transporte para sua viagem de " +
            requested.getOfertas().getOrigem() +
            " para " +
            requested.getOfertas().getDestino() +
            " no dia " +
            requested.getOfertas().getDataFechamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        interactive.setBody(body);

        Map<String, String> footer = new HashMap<>();
        footer.put("text", "Tá on? Transportou!");
        interactive.setFooter(footer);

        FacebookActionsVM actions = new FacebookActionsVM();
        actions.setButton("Ver Opções");

        FacebookSectionsVM sectionsVM = new FacebookSectionsVM();
        sectionsVM.setTitle("Opções");

        List<FacebookRowsVM> rows = new ArrayList<>();

        ofertas.forEach(ofertasDTO -> {
            FacebookRowsVM row = new FacebookRowsVM();
            row.setId(ofertasDTO.getId().toString());
            row.setTitle(ofertasDTO.getDataFechamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            String text = "de: " + ofertasDTO.getOrigem() + " para: " + ofertasDTO.getDestino() + " | vagas: " + ofertasDTO.getQuantidade();
            row.setDescription(text.substring(0, 70));
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
        message.setPhoneWhatsApp("55" + solicitacao.getOfertas().getPerfil().getUser().getPhone());

        List<FacebookContactVM> facebookContactVMS = new ArrayList<>();

        FacebookContactVM contactVM = new FacebookContactVM();

        Map<String, String> address = new HashMap<>();
        address.put("city", solicitacao.getPerfil().getCidade());
        address.put("state", solicitacao.getPerfil().getEstado());

        contactVM.setAddresses(address);

        List<Map<String, String>> emails = new ArrayList<>();

        Map<String, String> email = new HashMap<>();
        email.put("email", solicitacao.getPerfil().getUser().getEmail());
        email.put("type", "Infocargas");

        emails.add(email);

        contactVM.setEmails(emails);

        Map<String, String> name = new HashMap<>();
        name.put("formatted_name", solicitacao.getPerfil().getNome());
        List<String> nameSplit = List.of(solicitacao.getPerfil().getUser().getName().split(""));
        name.put("first_name", nameSplit.get(0));
        if (nameSplit.size() > 1) {
            name.put("last_name", nameSplit.get(nameSplit.size() - 1));
        }

        contactVM.setName(name);

        Map<String, String> org = new HashMap<>();
        org.put("company", solicitacao.getPerfil().getTipoConta().name().toLowerCase());
        org.put("department", solicitacao.getOfertas().getTipoTransporte().name().toLowerCase());

        contactVM.setName(org);

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

    private FacebookSendResponse sendNotification(FacebookMessageVM message) {
        try {
            return facebookApiProxy.createMessage("whatsapp", Constants.FACEBOOK_TOKEN, message);
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
}
