package com.infocargas.freteapp.web.rest.vm.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Body to WhatsApp Message.
 */
public class FacebookMessageVM {

    /**
     * Using only in headers
     * exemplo: whatsapp
     */
    @JsonProperty("messaging_product")
    private String messagingProduct;

    /**
     * Type of recipient example individual
     */
    @JsonProperty("recipient_type")
    private String recipientType;

    /**
     * Number of whatsapp to receive this message.
     * Need use country code without (+) plus code.
     */
    @JsonProperty("to")
    private String phoneWhatsApp;

    /**
     * Type of message that will be sending.
     * Values: [text, image, audio, document, template, hsm]
     */
    private String type;

    /**
     * Have template of message that will be sent.
     * The template need be created in Meta Business.
     */
    private FacebookTemplateVM template;

    /**
     * Have template of message that will be sent.
     * The template need be created in Meta Business.
     */
    private FacebookInteractiveVM interactive;

    /**
     * have all content of this message that will be sent.
     * Example: [body, buttons, button, image, etc]
     */
    private List<FacebookComponetsVM> componets;

    @JsonProperty("text")
    private Map<String, Object> messageBody;

    private List<FacebookContactVM> contacts;

    public String getMessagingProduct() {
        return messagingProduct;
    }

    public void setMessagingProduct(String messagingProduct) {
        this.messagingProduct = messagingProduct;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public String getPhoneWhatsApp() {
        return phoneWhatsApp;
    }

    public void setPhoneWhatsApp(String phoneWhatsApp) {
        this.phoneWhatsApp = phoneWhatsApp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FacebookTemplateVM getTemplate() {
        return template;
    }

    public void setTemplate(FacebookTemplateVM template) {
        this.template = template;
    }

    public List<FacebookComponetsVM> getComponets() {
        return componets;
    }

    public void setComponets(List<FacebookComponetsVM> componets) {
        this.componets = componets;
    }

    public FacebookInteractiveVM getInteractive() {
        return interactive;
    }

    public void setInteractive(FacebookInteractiveVM interactive) {
        this.interactive = interactive;
    }

    public List<FacebookContactVM> getContacts() {
        return contacts;
    }

    public void setContacts(List<FacebookContactVM> contacts) {
        this.contacts = contacts;
    }

    public Map<String, Object> getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(Map<String, Object> messageBody) {
        this.messageBody = messageBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookMessageVM)) return false;

        FacebookMessageVM messageVM = (FacebookMessageVM) o;

        if (
            getMessagingProduct() != null
                ? !getMessagingProduct().equals(messageVM.getMessagingProduct())
                : messageVM.getMessagingProduct() != null
        ) return false;
        if (
            getRecipientType() != null ? !getRecipientType().equals(messageVM.getRecipientType()) : messageVM.getRecipientType() != null
        ) return false;
        if (
            getPhoneWhatsApp() != null ? !getPhoneWhatsApp().equals(messageVM.getPhoneWhatsApp()) : messageVM.getPhoneWhatsApp() != null
        ) return false;
        if (getType() != null ? !getType().equals(messageVM.getType()) : messageVM.getType() != null) return false;
        if (getTemplate() != null ? !getTemplate().equals(messageVM.getTemplate()) : messageVM.getTemplate() != null) return false;
        if (
            getInteractive() != null ? !getInteractive().equals(messageVM.getInteractive()) : messageVM.getInteractive() != null
        ) return false;
        if (getComponets() != null ? !getComponets().equals(messageVM.getComponets()) : messageVM.getComponets() != null) return false;
        return getContacts() != null ? getContacts().equals(messageVM.getContacts()) : messageVM.getContacts() == null;
    }

    @Override
    public int hashCode() {
        int result = getMessagingProduct() != null ? getMessagingProduct().hashCode() : 0;
        result = 31 * result + (getRecipientType() != null ? getRecipientType().hashCode() : 0);
        result = 31 * result + (getPhoneWhatsApp() != null ? getPhoneWhatsApp().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getTemplate() != null ? getTemplate().hashCode() : 0);
        result = 31 * result + (getInteractive() != null ? getInteractive().hashCode() : 0);
        result = 31 * result + (getComponets() != null ? getComponets().hashCode() : 0);
        result = 31 * result + (getContacts() != null ? getContacts().hashCode() : 0);
        return result;
    }
}
