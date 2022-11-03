package com.infocargas.freteapp.response.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FacebookSendResponse {

    @JsonProperty("messaging_product")
    private String messagingProduct;

    private List<FacebookResponseContacts> contacts;

    private List<FacebookResponseMessages> messages;

    private FacebookException error;

    public String getMessagingProduct() {
        return messagingProduct;
    }

    public void setMessagingProduct(String messagingProduct) {
        this.messagingProduct = messagingProduct;
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

    public FacebookException getError() {
        return error;
    }

    public void setError(FacebookException error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookSendResponse)) return false;

        FacebookSendResponse that = (FacebookSendResponse) o;

        if (
            getMessagingProduct() != null ? !getMessagingProduct().equals(that.getMessagingProduct()) : that.getMessagingProduct() != null
        ) return false;
        if (getContacts() != null ? !getContacts().equals(that.getContacts()) : that.getContacts() != null) return false;
        if (getMessages() != null ? !getMessages().equals(that.getMessages()) : that.getMessages() != null) return false;
        return getError() != null ? getError().equals(that.getError()) : that.getError() == null;
    }

    @Override
    public int hashCode() {
        int result = getMessagingProduct() != null ? getMessagingProduct().hashCode() : 0;
        result = 31 * result + (getContacts() != null ? getContacts().hashCode() : 0);
        result = 31 * result + (getMessages() != null ? getMessages().hashCode() : 0);
        result = 31 * result + (getError() != null ? getError().hashCode() : 0);
        return result;
    }
}
