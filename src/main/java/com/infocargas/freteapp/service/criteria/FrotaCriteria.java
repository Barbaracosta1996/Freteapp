package com.infocargas.freteapp.service.criteria;

import com.infocargas.freteapp.domain.enumeration.TipoCategoria;
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
 * Criteria class for the {@link com.infocargas.freteapp.domain.Frota} entity. This class is used
 * in {@link com.infocargas.freteapp.web.rest.FrotaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /frotas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class FrotaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoCategoria
     */
    public static class TipoCategoriaFilter extends Filter<TipoCategoria> {

        public TipoCategoriaFilter() {}

        public TipoCategoriaFilter(TipoCategoriaFilter filter) {
            super(filter);
        }

        @Override
        public TipoCategoriaFilter copy() {
            return new TipoCategoriaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter modelo;

    private StringFilter marca;

    private StringFilter ano;

    private TipoCategoriaFilter tipoCategoria;

    private StringFilter obsCategoriaOutro;

    private LongFilter perfilId;

    private LongFilter categoriaVeiculoId;

    private Boolean distinct;

    public FrotaCriteria() {}

    public FrotaCriteria(FrotaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.modelo = other.modelo == null ? null : other.modelo.copy();
        this.marca = other.marca == null ? null : other.marca.copy();
        this.ano = other.ano == null ? null : other.ano.copy();
        this.tipoCategoria = other.tipoCategoria == null ? null : other.tipoCategoria.copy();
        this.obsCategoriaOutro = other.obsCategoriaOutro == null ? null : other.obsCategoriaOutro.copy();
        this.perfilId = other.perfilId == null ? null : other.perfilId.copy();
        this.categoriaVeiculoId = other.categoriaVeiculoId == null ? null : other.categoriaVeiculoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FrotaCriteria copy() {
        return new FrotaCriteria(this);
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

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getModelo() {
        return modelo;
    }

    public StringFilter modelo() {
        if (modelo == null) {
            modelo = new StringFilter();
        }
        return modelo;
    }

    public void setModelo(StringFilter modelo) {
        this.modelo = modelo;
    }

    public StringFilter getMarca() {
        return marca;
    }

    public StringFilter marca() {
        if (marca == null) {
            marca = new StringFilter();
        }
        return marca;
    }

    public void setMarca(StringFilter marca) {
        this.marca = marca;
    }

    public StringFilter getAno() {
        return ano;
    }

    public StringFilter ano() {
        if (ano == null) {
            ano = new StringFilter();
        }
        return ano;
    }

    public void setAno(StringFilter ano) {
        this.ano = ano;
    }

    public TipoCategoriaFilter getTipoCategoria() {
        return tipoCategoria;
    }

    public TipoCategoriaFilter tipoCategoria() {
        if (tipoCategoria == null) {
            tipoCategoria = new TipoCategoriaFilter();
        }
        return tipoCategoria;
    }

    public void setTipoCategoria(TipoCategoriaFilter tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public StringFilter getObsCategoriaOutro() {
        return obsCategoriaOutro;
    }

    public StringFilter obsCategoriaOutro() {
        if (obsCategoriaOutro == null) {
            obsCategoriaOutro = new StringFilter();
        }
        return obsCategoriaOutro;
    }

    public void setObsCategoriaOutro(StringFilter obsCategoriaOutro) {
        this.obsCategoriaOutro = obsCategoriaOutro;
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

    public LongFilter getCategoriaVeiculoId() {
        return categoriaVeiculoId;
    }

    public LongFilter categoriaVeiculoId() {
        if (categoriaVeiculoId == null) {
            categoriaVeiculoId = new LongFilter();
        }
        return categoriaVeiculoId;
    }

    public void setCategoriaVeiculoId(LongFilter categoriaVeiculoId) {
        this.categoriaVeiculoId = categoriaVeiculoId;
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
        final FrotaCriteria that = (FrotaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(modelo, that.modelo) &&
            Objects.equals(marca, that.marca) &&
            Objects.equals(ano, that.ano) &&
            Objects.equals(tipoCategoria, that.tipoCategoria) &&
            Objects.equals(obsCategoriaOutro, that.obsCategoriaOutro) &&
            Objects.equals(perfilId, that.perfilId) &&
            Objects.equals(categoriaVeiculoId, that.categoriaVeiculoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, modelo, marca, ano, tipoCategoria, obsCategoriaOutro, perfilId, categoriaVeiculoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrotaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (modelo != null ? "modelo=" + modelo + ", " : "") +
            (marca != null ? "marca=" + marca + ", " : "") +
            (ano != null ? "ano=" + ano + ", " : "") +
            (tipoCategoria != null ? "tipoCategoria=" + tipoCategoria + ", " : "") +
            (obsCategoriaOutro != null ? "obsCategoriaOutro=" + obsCategoriaOutro + ", " : "") +
            (perfilId != null ? "perfilId=" + perfilId + ", " : "") +
            (categoriaVeiculoId != null ? "categoriaVeiculoId=" + categoriaVeiculoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
