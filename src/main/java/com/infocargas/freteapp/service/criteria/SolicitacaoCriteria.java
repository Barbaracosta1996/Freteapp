package com.infocargas.freteapp.service.criteria;

import com.infocargas.freteapp.domain.enumeration.AnwserStatus;
import com.infocargas.freteapp.domain.enumeration.StatusSolicitacao;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.infocargas.freteapp.domain.Solicitacao} entity. This class is used
 * in {@link com.infocargas.freteapp.web.rest.SolicitacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /solicitacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SolicitacaoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AnwserStatus
     */
    public static class AnwserStatusFilter extends Filter<AnwserStatus> {

        public AnwserStatusFilter() {}

        public AnwserStatusFilter(AnwserStatusFilter filter) {
            super(filter);
        }

        @Override
        public AnwserStatusFilter copy() {
            return new AnwserStatusFilter(this);
        }
    }

    /**
     * Class for filtering StatusSolicitacao
     */
    public static class StatusSolicitacaoFilter extends Filter<StatusSolicitacao> {

        public StatusSolicitacaoFilter() {}

        public StatusSolicitacaoFilter(StatusSolicitacaoFilter filter) {
            super(filter);
        }

        @Override
        public StatusSolicitacaoFilter copy() {
            return new StatusSolicitacaoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter dataProposta;

    private ZonedDateTimeFilter dataModificacao;

    private AnwserStatusFilter aceitar;

    private StatusSolicitacaoFilter status;

    private StringFilter obs;

    private DoubleFilter valorFrete;

    private LongFilter ofertasId;

    private LongFilter perfilId;

    private LongFilter ofertasUserId;

    private Boolean distinct;

    public SolicitacaoCriteria() {}

    public SolicitacaoCriteria(SolicitacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dataProposta = other.dataProposta == null ? null : other.dataProposta.copy();
        this.dataModificacao = other.dataModificacao == null ? null : other.dataModificacao.copy();
        this.aceitar = other.aceitar == null ? null : other.aceitar.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.obs = other.obs == null ? null : other.obs.copy();
        this.valorFrete = other.valorFrete == null ? null : other.valorFrete.copy();
        this.ofertasId = other.ofertasId == null ? null : other.ofertasId.copy();
        this.perfilId = other.perfilId == null ? null : other.perfilId.copy();
        this.distinct = other.distinct;
        this.ofertasUserId = other.ofertasUserId == null ? null : other.ofertasUserId.copy();
    }

    @Override
    public SolicitacaoCriteria copy() {
        return new SolicitacaoCriteria(this);
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

    public ZonedDateTimeFilter getDataProposta() {
        return dataProposta;
    }

    public ZonedDateTimeFilter dataProposta() {
        if (dataProposta == null) {
            dataProposta = new ZonedDateTimeFilter();
        }
        return dataProposta;
    }

    public void setDataProposta(ZonedDateTimeFilter dataProposta) {
        this.dataProposta = dataProposta;
    }

    public ZonedDateTimeFilter getDataModificacao() {
        return dataModificacao;
    }

    public ZonedDateTimeFilter dataModificacao() {
        if (dataModificacao == null) {
            dataModificacao = new ZonedDateTimeFilter();
        }
        return dataModificacao;
    }

    public void setDataModificacao(ZonedDateTimeFilter dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public AnwserStatusFilter getAceitar() {
        return aceitar;
    }

    public AnwserStatusFilter aceitar() {
        if (aceitar == null) {
            aceitar = new AnwserStatusFilter();
        }
        return aceitar;
    }

    public void setAceitar(AnwserStatusFilter aceitar) {
        this.aceitar = aceitar;
    }

    public StatusSolicitacaoFilter getStatus() {
        return status;
    }

    public StatusSolicitacaoFilter status() {
        if (status == null) {
            status = new StatusSolicitacaoFilter();
        }
        return status;
    }

    public void setStatus(StatusSolicitacaoFilter status) {
        this.status = status;
    }

    public StringFilter getObs() {
        return obs;
    }

    public StringFilter obs() {
        if (obs == null) {
            obs = new StringFilter();
        }
        return obs;
    }

    public void setObs(StringFilter obs) {
        this.obs = obs;
    }

    public DoubleFilter getValorFrete() {
        return valorFrete;
    }

    public DoubleFilter valorFrete() {
        if (valorFrete == null) {
            valorFrete = new DoubleFilter();
        }
        return valorFrete;
    }

    public void setValorFrete(DoubleFilter valorFrete) {
        this.valorFrete = valorFrete;
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

    public LongFilter getOfertasUserId() {
        return ofertasUserId;
    }

    public void setOfertasUserId(LongFilter ofertasUserId) {
        this.ofertasUserId = ofertasUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SolicitacaoCriteria that = (SolicitacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataProposta, that.dataProposta) &&
            Objects.equals(dataModificacao, that.dataModificacao) &&
            Objects.equals(aceitar, that.aceitar) &&
            Objects.equals(status, that.status) &&
            Objects.equals(obs, that.obs) &&
            Objects.equals(valorFrete, that.valorFrete) &&
            Objects.equals(ofertasId, that.ofertasId) &&
            Objects.equals(perfilId, that.perfilId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataProposta, dataModificacao, aceitar, status, obs, valorFrete, ofertasId, perfilId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolicitacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataProposta != null ? "dataProposta=" + dataProposta + ", " : "") +
            (dataModificacao != null ? "dataModificacao=" + dataModificacao + ", " : "") +
            (aceitar != null ? "aceitar=" + aceitar + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (obs != null ? "obs=" + obs + ", " : "") +
            (valorFrete != null ? "valorFrete=" + valorFrete + ", " : "") +
            (ofertasId != null ? "ofertasId=" + ofertasId + ", " : "") +
            (perfilId != null ? "perfilId=" + perfilId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
