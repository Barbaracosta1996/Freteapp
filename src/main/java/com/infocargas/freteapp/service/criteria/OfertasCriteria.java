package com.infocargas.freteapp.service.criteria;

import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoCarga;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.TipoTransporteOferta;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.infocargas.freteapp.domain.Ofertas} entity. This class is used
 * in {@link com.infocargas.freteapp.web.rest.OfertasResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ofertas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class OfertasCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoCarga
     */
    public static class TipoCargaFilter extends Filter<TipoCarga> {

        public TipoCargaFilter() {}

        public TipoCargaFilter(TipoCargaFilter filter) {
            super(filter);
        }

        @Override
        public TipoCargaFilter copy() {
            return new TipoCargaFilter(this);
        }
    }

    /**
     * Class for filtering StatusOferta
     */
    public static class StatusOfertaFilter extends Filter<StatusOferta> {

        public StatusOfertaFilter() {}

        public StatusOfertaFilter(StatusOfertaFilter filter) {
            super(filter);
        }

        @Override
        public StatusOfertaFilter copy() {
            return new StatusOfertaFilter(this);
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

    /**
     * Class for filtering TipoTransporteOferta
     */
    public static class TipoTransporteOfertaFilter extends Filter<TipoTransporteOferta> {

        public TipoTransporteOfertaFilter() {}

        public TipoTransporteOfertaFilter(TipoTransporteOfertaFilter filter) {
            super(filter);
        }

        @Override
        public TipoTransporteOfertaFilter copy() {
            return new TipoTransporteOfertaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter dataPostagem;

    private IntegerFilter quantidade;

    private TipoCargaFilter tipoCarga;

    private LocalDateFilter dataColeta;

    private ZonedDateTimeFilter dataEntrega;

    private ZonedDateTimeFilter dataModificacao;

    private ZonedDateTimeFilter dataFechamento;

    private StatusOfertaFilter status;

    private TipoOfertaFilter tipoOferta;

    private TipoTransporteOfertaFilter tipoTransporte;

    private StringFilter destino;

    private StringFilter origem;

    private LongFilter perfilId;

    private LongFilter userId;

    private Boolean distinct;

    public OfertasCriteria() {}

    public OfertasCriteria(OfertasCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dataPostagem = other.dataPostagem == null ? null : other.dataPostagem.copy();
        this.quantidade = other.quantidade == null ? null : other.quantidade.copy();
        this.tipoCarga = other.tipoCarga == null ? null : other.tipoCarga.copy();
        this.dataColeta = other.dataColeta == null ? null : other.dataColeta.copy();
        this.dataEntrega = other.dataEntrega == null ? null : other.dataEntrega.copy();
        this.dataModificacao = other.dataModificacao == null ? null : other.dataModificacao.copy();
        this.dataFechamento = other.dataFechamento == null ? null : other.dataFechamento.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.tipoOferta = other.tipoOferta == null ? null : other.tipoOferta.copy();
        this.tipoTransporte = other.tipoTransporte == null ? null : other.tipoTransporte.copy();
        this.destino = other.destino == null ? null : other.destino.copy();
        this.origem = other.origem == null ? null : other.origem.copy();
        this.perfilId = other.perfilId == null ? null : other.perfilId.copy();
        this.distinct = other.distinct;
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public OfertasCriteria copy() {
        return new OfertasCriteria(this);
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

    public ZonedDateTimeFilter getDataPostagem() {
        return dataPostagem;
    }

    public ZonedDateTimeFilter dataPostagem() {
        if (dataPostagem == null) {
            dataPostagem = new ZonedDateTimeFilter();
        }
        return dataPostagem;
    }

    public void setDataPostagem(ZonedDateTimeFilter dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public IntegerFilter getQuantidade() {
        return quantidade;
    }

    public IntegerFilter quantidade() {
        if (quantidade == null) {
            quantidade = new IntegerFilter();
        }
        return quantidade;
    }

    public void setQuantidade(IntegerFilter quantidade) {
        this.quantidade = quantidade;
    }

    public TipoCargaFilter getTipoCarga() {
        return tipoCarga;
    }

    public TipoCargaFilter tipoCarga() {
        if (tipoCarga == null) {
            tipoCarga = new TipoCargaFilter();
        }
        return tipoCarga;
    }

    public void setTipoCarga(TipoCargaFilter tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public LocalDateFilter getDataColeta() {
        return dataColeta;
    }

    public LocalDateFilter dataColeta() {
        if (dataColeta == null) {
            dataColeta = new LocalDateFilter();
        }
        return dataColeta;
    }

    public void setDataColeta(LocalDateFilter dataColeta) {
        this.dataColeta = dataColeta;
    }

    public ZonedDateTimeFilter getDataEntrega() {
        return dataEntrega;
    }

    public ZonedDateTimeFilter dataEntrega() {
        if (dataEntrega == null) {
            dataEntrega = new ZonedDateTimeFilter();
        }
        return dataEntrega;
    }

    public void setDataEntrega(ZonedDateTimeFilter dataEntrega) {
        this.dataEntrega = dataEntrega;
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

    public ZonedDateTimeFilter getDataFechamento() {
        return dataFechamento;
    }

    public ZonedDateTimeFilter dataFechamento() {
        if (dataFechamento == null) {
            dataFechamento = new ZonedDateTimeFilter();
        }
        return dataFechamento;
    }

    public void setDataFechamento(ZonedDateTimeFilter dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public StatusOfertaFilter getStatus() {
        return status;
    }

    public StatusOfertaFilter status() {
        if (status == null) {
            status = new StatusOfertaFilter();
        }
        return status;
    }

    public void setStatus(StatusOfertaFilter status) {
        this.status = status;
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

    public TipoTransporteOfertaFilter getTipoTransporte() {
        return tipoTransporte;
    }

    public TipoTransporteOfertaFilter tipoTransporte() {
        if (tipoTransporte == null) {
            tipoTransporte = new TipoTransporteOfertaFilter();
        }
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoTransporteOfertaFilter tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public StringFilter getDestino() {
        return destino;
    }

    public StringFilter destino() {
        if (destino == null) {
            destino = new StringFilter();
        }
        return destino;
    }

    public void setDestino(StringFilter destino) {
        this.destino = destino;
    }

    public StringFilter getOrigem() {
        return origem;
    }

    public StringFilter origem() {
        if (origem == null) {
            origem = new StringFilter();
        }
        return origem;
    }

    public void setOrigem(StringFilter origem) {
        this.origem = origem;
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

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OfertasCriteria that = (OfertasCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataPostagem, that.dataPostagem) &&
            Objects.equals(quantidade, that.quantidade) &&
            Objects.equals(tipoCarga, that.tipoCarga) &&
            Objects.equals(dataColeta, that.dataColeta) &&
            Objects.equals(dataEntrega, that.dataEntrega) &&
            Objects.equals(dataModificacao, that.dataModificacao) &&
            Objects.equals(dataFechamento, that.dataFechamento) &&
            Objects.equals(status, that.status) &&
            Objects.equals(tipoOferta, that.tipoOferta) &&
            Objects.equals(tipoTransporte, that.tipoTransporte) &&
            Objects.equals(destino, that.destino) &&
            Objects.equals(origem, that.origem) &&
            Objects.equals(perfilId, that.perfilId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataPostagem,
            quantidade,
            tipoCarga,
            dataColeta,
            dataEntrega,
            dataModificacao,
            dataFechamento,
            status,
            tipoOferta,
            tipoTransporte,
            destino,
            origem,
            perfilId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OfertasCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataPostagem != null ? "dataPostagem=" + dataPostagem + ", " : "") +
            (quantidade != null ? "quantidade=" + quantidade + ", " : "") +
            (tipoCarga != null ? "tipoCarga=" + tipoCarga + ", " : "") +
            (dataColeta != null ? "dataColeta=" + dataColeta + ", " : "") +
            (dataEntrega != null ? "dataEntrega=" + dataEntrega + ", " : "") +
            (dataModificacao != null ? "dataModificacao=" + dataModificacao + ", " : "") +
            (dataFechamento != null ? "dataFechamento=" + dataFechamento + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (tipoOferta != null ? "tipoOferta=" + tipoOferta + ", " : "") +
            (tipoTransporte != null ? "tipoTransporte=" + tipoTransporte + ", " : "") +
            (destino != null ? "destino=" + destino + ", " : "") +
            (origem != null ? "origem=" + origem + ", " : "") +
            (perfilId != null ? "perfilId=" + perfilId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
