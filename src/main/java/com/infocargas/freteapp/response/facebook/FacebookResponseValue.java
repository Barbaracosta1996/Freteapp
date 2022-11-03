package com.infocargas.freteapp.response.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class FacebookResponseValue {

    @JsonProperty("messaging_product")
    private String messagingProduct;

    private Map<String, String> metadata;

    private List<FacebookResponseContacts> contacts;

    private List<FacebookResponseMessages> messages;

    public String getMessagingProduct() {
        return messagingProduct;
    }

    public void setMessagingProduct(String messagingProduct) {
        this.messagingProduct = messagingProduct;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public List<FacebookResponseContacts> getContacts() {
        return contacts;
    }

    public void setContacts(List<FacebookResponseContacts> contacts) {
        this.contacts = contacts;
    }

    public List<FacebookResponseMessages> getMessages() {
        return messages;
    }

    public void setMessages(List<FacebookResponseMessages> messages) {
        this.messages = messages;
    }
}
