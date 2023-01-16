package com.infocargas.freteapp.service.dto;

import com.infocargas.freteapp.domain.enumeration.MessageFacebookWPStatus;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.WhatsAppType;
import com.infocargas.freteapp.domain.enumeration.WhatsStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.WhatsMessageBatch} entity.
 */
public class WhatsMessageBatchDTO implements Serializable {

    private Long id;

    @NotNull
    private WhatsAppType tipo;

    @NotNull
    private String waidTo;

    @NotNull
    private Integer perfilID;

    @NotNull
    private WhatsStatus status;

    private Long ofertaId;

    private TipoOferta tipoOferta;

    private ZonedDateTime notificationDate;

    private ZonedDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private MessageFacebookWPStatus statusMessage;

    private ZonedDateTime dateReadStatus;

    private ZonedDateTime dateDeliveryStatus;

    private ZonedDateTime dateSentStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WhatsAppType getTipo() {
        return tipo;
    }

    public void setTipo(WhatsAppType tipo) {
        this.tipo = tipo;
    }

    public String getWaidTo() {
        return waidTo;
    }

    public void setWaidTo(String waidTo) {
        this.waidTo = waidTo;
    }

    public Integer getPerfilID() {
        return perfilID;
    }

    public void setPerfilID(Integer perfilID) {
        this.perfilID = perfilID;
    }

    public WhatsStatus getStatus() {
        return status;
    }

    public void setStatus(WhatsStatus status) {
        this.status = status;
    }

    public Long getOfertaId() {
        return ofertaId;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }

    public TipoOferta getTipoOferta() {
        return tipoOferta;
    }

    public void setTipoOferta(TipoOferta tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    public ZonedDateTime getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(ZonedDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public MessageFacebookWPStatus getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(MessageFacebookWPStatus statusMessage) {
        this.statusMessage = statusMessage;
    }

    public ZonedDateTime getDateReadStatus() {
        return dateReadStatus;
    }

    public void setDateReadStatus(ZonedDateTime dateReadStatus) {
        this.dateReadStatus = dateReadStatus;
    }

    public ZonedDateTime getDateDeliveryStatus() {
        return dateDeliveryStatus;
    }

    public void setDateDeliveryStatus(ZonedDateTime dateDeliveryStatus) {
        this.dateDeliveryStatus = dateDeliveryStatus;
    }

    public ZonedDateTime getDateSentStatus() {
        return dateSentStatus;
    }

    public void setDateSentStatus(ZonedDateTime dateSentStatus) {
        this.dateSentStatus = dateSentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WhatsMessageBatchDTO)) {
            return false;
        }

        WhatsMessageBatchDTO whatsMessageBatchDTO = (WhatsMessageBatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, whatsMessageBatchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WhatsMessageBatchDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", waidTo='" + getWaidTo() + "'" +
            ", perfilID=" + getPerfilID() +
            ", status='" + getStatus() + "'" +
            ", ofertaId=" + getOfertaId() +
            ", tipoOferta='" + getTipoOferta() + "'" +
            ", notificationDate='" + getNotificationDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
