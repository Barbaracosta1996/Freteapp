package com.infocargas.freteapp.service.criteria;

import com.infocargas.freteapp.domain.enumeration.TipoPerfilDocumento;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.infocargas.freteapp.domain.PerfilAnexos} entity. This class is used
 * in {@link com.infocargas.freteapp.web.rest.PerfilAnexosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /perfil-anexos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class PerfilAnexosCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoPerfilDocumento
     */
    public static class TipoPerfilDocumentoFilter extends Filter<TipoPerfilDocumento> {

        public TipoPerfilDocumentoFilter() {}

        public TipoPerfilDocumentoFilter(TipoPerfilDocumentoFilter filter) {
            super(filter);
        }

        @Override
        public TipoPerfilDocumentoFilter copy() {
            return new TipoPerfilDocumentoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dataPostagem;

    private TipoPerfilDocumentoFilter tipoDocumento;

    private StringFilter descricao;

    private LongFilter perfilId;

    private Boolean distinct;

    public PerfilAnexosCriteria() {}

    public PerfilAnexosCriteria(PerfilAnexosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dataPostagem = other.dataPostagem == null ? null : other.dataPostagem.copy();
        this.tipoDocumento = other.tipoDocumento == null ? null : other.tipoDocumento.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.perfilId = other.perfilId == null ? null : other.perfilId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PerfilAnexosCriteria copy() {
        return new PerfilAnexosCriteria(this);
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

    public LocalDateFilter getDataPostagem() {
        return dataPostagem;
    }

    public LocalDateFilter dataPostagem() {
        if (dataPostagem == null) {
            dataPostagem = new LocalDateFilter();
        }
        return dataPostagem;
    }

    public void setDataPostagem(LocalDateFilter dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public TipoPerfilDocumentoFilter getTipoDocumento() {
        return tipoDocumento;
    }

    public TipoPerfilDocumentoFilter tipoDocumento() {
        if (tipoDocumento == null) {
            tipoDocumento = new TipoPerfilDocumentoFilter();
        }
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoPerfilDocumentoFilter tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public StringFilter descricao() {
        if (descricao == null) {
            descricao = new StringFilter();
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public LongFilter getPerfilId() {
        return perfilId;
    }

    public LongFilter perfilId() {
        if (perfilId == null) {
            perfilId = new LongFilter();
        }
        return perfilId;
    }

    public void setPerfilId(LongFilter perfilId) {
        this.perfilId = perfilId;
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
        final PerfilAnexosCriteria that = (PerfilAnexosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataPostagem, that.dataPostagem) &&
            Objects.equals(tipoDocumento, that.tipoDocumento) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(perfilId, that.perfilId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataPostagem, tipoDocumento, descricao, perfilId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerfilAnexosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataPostagem != null ? "dataPostagem=" + dataPostagem + ", " : "") +
            (tipoDocumento != null ? "tipoDocumento=" + tipoDocumento + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (perfilId != null ? "perfilId=" + perfilId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
