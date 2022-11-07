package com.infocargas.freteapp.domain;

import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.WhatsAppType;
import com.infocargas.freteapp.domain.enumeration.WhatsStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WhatsMessageBatch.
 */
@Entity
@Table(name = "whats_message_batch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WhatsMessageBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private WhatsAppType tipo;

    @NotNull
    @Column(name = "waid_to", nullable = false)
    private String waidTo;

    @NotNull
    @Column(name = "perfil_id", nullable = false)
    private Integer perfilID;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WhatsStatus status;

    @Column(name = "oferta_id")
    private Long ofertaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_oferta")
    private TipoOferta tipoOferta;

    @Column(name = "notification_date")
    private ZonedDateTime notificationDate;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WhatsMessageBatch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WhatsAppType getTipo() {
        return this.tipo;
    }

    public WhatsMessageBatch tipo(WhatsAppType tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(WhatsAppType tipo) {
        this.tipo = tipo;
    }

    public String getWaidTo() {
        return this.waidTo;
    }

    public WhatsMessageBatch waidTo(String waidTo) {
        this.setWaidTo(waidTo);
        return this;
    }

    public void setWaidTo(String waidTo) {
        this.waidTo = waidTo;
    }

    public Integer getPerfilID() {
        return this.perfilID;
    }

    public WhatsMessageBatch perfilID(Integer perfilID) {
        this.setPerfilID(perfilID);
        return this;
    }

    public void setPerfilID(Integer perfilID) {
        this.perfilID = perfilID;
    }

    public WhatsStatus getStatus() {
        return this.status;
    }

    public WhatsMessageBatch status(WhatsStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WhatsStatus status) {
        this.status = status;
    }

    public Long getOfertaId() {
        return this.ofertaId;
    }

    public WhatsMessageBatch ofertaId(Long ofertaId) {
        this.setOfertaId(ofertaId);
        return this;
    }

    public void setOfertaId(Long ofertaId) {
        this.ofertaId = ofertaId;
    }

    public TipoOferta getTipoOferta() {
        return this.tipoOferta;
    }

    public WhatsMessageBatch tipoOferta(TipoOferta tipoOferta) {
        this.setTipoOferta(tipoOferta);
        return this;
    }

    public void setTipoOferta(TipoOferta tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    public ZonedDateTime getNotificationDate() {
        return this.notificationDate;
    }

    public WhatsMessageBatch notificationDate(ZonedDateTime notificationDate) {
        this.setNotificationDate(notificationDate);
        return this;
    }

    public void setNotificationDate(ZonedDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public WhatsMessageBatch createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WhatsMessageBatch)) {
            return false;
        }
        return id != null && id.equals(((WhatsMessageBatch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WhatsMessageBatch{" +
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

    @PrePersist
    public void updateCreateDate() {
        this.createdDate = ZonedDateTime.now();
    }
}
