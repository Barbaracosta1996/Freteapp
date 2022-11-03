package com.infocargas.freteapp.service.criteria;

import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.WhatsAppType;
import com.infocargas.freteapp.domain.enumeration.WhatsStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.infocargas.freteapp.domain.WhatsMessageBatch} entity. This class is used
 * in {@link com.infocargas.freteapp.web.rest.WhatsMessageBatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /whats-message-batches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class WhatsMessageBatchCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WhatsAppType
     */
    public static class WhatsAppTypeFilter extends Filter<WhatsAppType> {

        public WhatsAppTypeFilter() {}

        public WhatsAppTypeFilter(WhatsAppTypeFilter filter) {
            super(filter);
        }

        @Override
        public WhatsAppTypeFilter copy() {
            return new WhatsAppTypeFilter(this);
        }
    }

    /**
     * Class for filtering WhatsStatus
     */
    public static class WhatsStatusFilter extends Filter<WhatsStatus> {

        public WhatsStatusFilter() {}

        public WhatsStatusFilter(WhatsStatusFilter filter) {
            super(filter);
        }

        @Override
        public WhatsStatusFilter copy() {
            return new WhatsStatusFilter(this);
        }
    }

    /**
     * Class for filtering TipoOferta
     */
    public static class TipoOfertaFilter extends Filter<TipoOferta> {

        public TipoOfertaFilter() {}

        public TipoOfertaFilter(TipoOfertaFilter filter) {
            super(filter);
        }

        @Override
        public TipoOfertaFilter copy() {
            return new TipoOfertaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private WhatsAppTypeFilter tipo;

    private StringFilter waidTo;

    private IntegerFilter perfilID;

    private WhatsStatusFilter status;

    private LongFilter ofertaId;

    private TipoOfertaFilter tipoOferta;

    private Boolean distinct;

    public WhatsMessageBatchCriteria() {}

    public WhatsMessageBatchCriteria(WhatsMessageBatchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.waidTo = other.waidTo == null ? null : other.waidTo.copy();
        this.perfilID = other.perfilID == null ? null : other.perfilID.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.ofertaId = other.ofertaId == null ? null : other.ofertaId.copy();
        this.tipoOferta = other.tipoOferta == null ? null : other.tipoOferta.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WhatsMessageBatchCriteria copy() {
        return new WhatsMessageBatchCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public WhatsAppTypeFilter getTipo() {
        return tipo;
    }

    public WhatsAppTypeFilter tipo() {
        if (tipo == null) {
            tipo = new WhatsAppTypeFilter();
        }
        return tipo;
    }

    public void setTipo(WhatsAppTypeFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getWaidTo() {
        return waidTo;
    }

    public StringFilter waidTo() {
        if (waidTo == null) {
            waidTo = new StringFilter();
        }
        return waidTo;
    }

    public void setWaidTo(StringFilter waidTo) {
        this.waidTo = waidTo;
    }

    public IntegerFilter getPerfilID() {
        return perfilID;
    }

    public IntegerFilter perfilID() {
        if (perfilID == null) {
            perfilID = new IntegerFilter();
        }
        return perfilID;
    }

    public void setPerfilID(IntegerFilter perfilID) {
        this.perfilID = perfilID;
    }

    public WhatsStatusFilter getStatus() {
        return status;
    }

    public WhatsStatusFilter status() {
        if (status == null) {
            status = new WhatsStatusFilter();
        }
        return status;
    }

    public void setStatus(WhatsStatusFilter status) {
        this.status = status;
    }

    public LongFilter getOfertaId() {
        return ofertaId;
    }

    public LongFilter ofertaId() {
        if (ofertaId == null) {
            ofertaId = new LongFilter();
        }
        return ofertaId;
    }

    public void setOfertaId(LongFilter ofertaId) {
        this.ofertaId = ofertaId;
    }

    public TipoOfertaFilter getTipoOferta() {
        return tipoOferta;
    }

    public TipoOfertaFilter tipoOferta() {
        if (tipoOferta == null) {
            tipoOferta = new TipoOfertaFilter();
        }
        return tipoOferta;
    }

    public void setTipoOferta(TipoOfertaFilter tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WhatsMessageBatchCriteria that = (WhatsMessageBatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(waidTo, that.waidTo) &&
            Objects.equals(perfilID, that.perfilID) &&
            Objects.equals(status, that.status) &&
            Objects.equals(ofertaId, that.ofertaId) &&
            Objects.equals(tipoOferta, that.tipoOferta) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, waidTo, perfilID, status, ofertaId, tipoOferta, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WhatsMessageBatchCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (waidTo != null ? "waidTo=" + waidTo + ", " : "") +
            (perfilID != null ? "perfilID=" + perfilID + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (ofertaId != null ? "ofertaId=" + ofertaId + ", " : "") +
            (tipoOferta != null ? "tipoOferta=" + tipoOferta + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
