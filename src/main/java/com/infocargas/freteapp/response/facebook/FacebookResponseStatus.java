package com.infocargas.freteapp.response.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class FacebookResponseStatus {

    private String id;

    @JsonProperty("recipient_id")
    private String recipientId;

    private String status;

    private Timestamp timestamp;

    private FacebookConversation conversation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public FacebookConversation getConversation() {
        return conversation;
    }

    public void setConversation(FacebookConversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public String toString() {
        return (
            "FacebookResponseStatus{" +
            "id='" +
            id +
            '\'' +
            ", recipientId='" +
            recipientId +
            '\'' +
            ", status='" +
            status +
            '\'' +
            ", timestamp=" +
            timestamp +
            ", conversation=" +
            conversation +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookResponseStatus)) return false;

        FacebookResponseStatus that = (FacebookResponseStatus) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getRecipientId() != null ? !getRecipientId().equals(that.getRecipientId()) : that.getRecipientId() != null) return false;
        if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) return false;
        if (getTimestamp() != null ? !getTimestamp().equals(that.getTimestamp()) : that.getTimestamp() != null) return false;
        return getConversation() != null ? getConversation().equals(that.getConversation()) : that.getConversation() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getRecipientId() != null ? getRecipientId().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        result = 31 * result + (getConversation() != null ? getConversation().hashCode() : 0);
        return result;
    }
}
