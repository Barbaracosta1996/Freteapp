package com.infocargas.freteapp.service.criteria;

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
 * Criteria class for the {@link com.infocargas.freteapp.domain.RotasOfertas} entity. This class is used
 * in {@link com.infocargas.freteapp.web.rest.RotasOfertasResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rotas-ofertas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class RotasOfertasCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter ofertasId;

    private Boolean distinct;

    public RotasOfertasCriteria() {}

    public RotasOfertasCriteria(RotasOfertasCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ofertasId = other.ofertasId == null ? null : other.ofertasId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RotasOfertasCriteria copy() {
        return new RotasOfertasCriteria(this);
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

    public LongFilter getOfertasId() {
        return ofertasId;
    }

    public LongFilter ofertasId() {
        if (ofertasId == null) {
            ofertasId = new LongFilter();
        }
        return ofertasId;
    }

    public void setOfertasId(LongFilter ofertasId) {
        this.ofertasId = ofertasId;
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
        final RotasOfertasCriteria that = (RotasOfertasCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(ofertasId, that.ofertasId) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ofertasId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RotasOfertasCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ofertasId != null ? "ofertasId=" + ofertasId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
