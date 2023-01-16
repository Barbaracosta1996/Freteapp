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

    private List<FacebookResponseStatus> statuses;

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

    public List<FacebookResponseStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<FacebookResponseStatus> statuses) {
        this.statuses = statuses;
    }

    @Override
    public String toString() {
        return (
            "FacebookResponseValue{" +
            "messagingProduct='" +
            messagingProduct +
            '\'' +
            ", metadata=" +
            metadata +
            ", contacts=" +
            contacts +
            ", messages=" +
            messages +
            ", statuses=" +
            statuses +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookResponseValue that)) return false;

        if (
            getMessagingProduct() != null ? !getMessagingProduct().equals(that.getMessagingProduct()) : that.getMessagingProduct() != null
        ) return false;
        if (getMetadata() != null ? !getMetadata().equals(that.getMetadata()) : that.getMetadata() != null) return false;
        if (getContacts() != null ? !getContacts().equals(that.getContacts()) : that.getContacts() != null) return false;
        if (getMessages() != null ? !getMessages().equals(that.getMessages()) : that.getMessages() != null) return false;
        return getStatuses() != null ? getStatuses().equals(that.getStatuses()) : that.getStatuses() == null;
    }

    @Override
    public int hashCode() {
        int result = getMessagingProduct() != null ? getMessagingProduct().hashCode() : 0;
        result = 31 * result + (getMetadata() != null ? getMetadata().hashCode() : 0);
        result = 31 * result + (getContacts() != null ? getContacts().hashCode() : 0);
        result = 31 * result + (getMessages() != null ? getMessages().hashCode() : 0);
        result = 31 * result + (getStatuses() != null ? getStatuses().hashCode() : 0);
        return result;
    }
}
