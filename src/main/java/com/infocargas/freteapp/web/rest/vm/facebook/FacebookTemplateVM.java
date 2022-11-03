package com.infocargas.freteapp.web.rest.vm.facebook;

import java.util.List;
import java.util.Map;

public class FacebookTemplateVM {

    /**
     * Template name created in Meta Business.
     * Need be created in facebook.
     */
    private String name;

    /**
     * Code of language created in template.
     * default pt_BR
     * see another code in https://developers.facebook.com/docs/whatsapp/api/messages/message-templates#language
     */
    private Map<String, String> language;

    /**
     * Matrix have content type of message
     * see https://developers.facebook.com/docs/whatsapp/on-premises/reference/messages
     */
    private List<FacebookComponetsVM> components;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getLanguage() {
        return language;
    }

    public void setLanguage(Map<String, String> language) {
        this.language = language;
    }

    public List<FacebookComponetsVM> getComponents() {
        return components;
    }

    public void setComponents(List<FacebookComponetsVM> components) {
        this.components = components;
    }
}
