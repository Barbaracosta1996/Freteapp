package com.infocargas.freteapp.service.criteria;

import com.infocargas.freteapp.domain.enumeration.TipoConta;
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
 * Criteria class for the {@link com.infocargas.freteapp.domain.Perfil} entity. This class is used
 * in {@link com.infocargas.freteapp.web.rest.PerfilResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /perfils?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class PerfilCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoConta
     */
    public static class TipoContaFilter extends Filter<TipoConta> {

        public TipoContaFilter() {}

        public TipoContaFilter(TipoContaFilter filter) {
            super(filter);
        }

        @Override
        public TipoContaFilter copy() {
            return new TipoContaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoContaFilter tipoConta;

    private StringFilter cpf;

    private StringFilter cnpj;

    private StringFilter rua;

    private StringFilter numero;

    private StringFilter bairro;

    private StringFilter cidade;

    private StringFilter estado;

    private StringFilter cep;

    private StringFilter pais;

    private StringFilter nome;

    private StringFilter razaosocial;

    private StringFilter telefoneComercial;

    private LongFilter userId;

    private Boolean distinct;

    public PerfilCriteria() {}

    public PerfilCriteria(PerfilCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipoConta = other.tipoConta == null ? null : other.tipoConta.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.cnpj = other.cnpj == null ? null : other.cnpj.copy();
        this.rua = other.rua == null ? null : other.rua.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.bairro = other.bairro == null ? null : other.bairro.copy();
        this.cidade = other.cidade == null ? null : other.cidade.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.cep = other.cep == null ? null : other.cep.copy();
        this.pais = other.pais == null ? null : other.pais.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.razaosocial = other.razaosocial == null ? null : other.razaosocial.copy();
        this.telefoneComercial = other.telefoneComercial == null ? null : other.telefoneComercial.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PerfilCriteria copy() {
        return new PerfilCriteria(this);
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

    public TipoContaFilter getTipoConta() {
        return tipoConta;
    }

    public TipoContaFilter tipoConta() {
        if (tipoConta == null) {
            tipoConta = new TipoContaFilter();
        }
        return tipoConta;
    }

    public void setTipoConta(TipoContaFilter tipoConta) {
        this.tipoConta = tipoConta;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public StringFilter cpf() {
        if (cpf == null) {
            cpf = new StringFilter();
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public StringFilter getCnpj() {
        return cnpj;
    }

    public StringFilter cnpj() {
        if (cnpj == null) {
            cnpj = new StringFilter();
        }
        return cnpj;
    }

    public void setCnpj(StringFilter cnpj) {
        this.cnpj = cnpj;
    }

    public StringFilter getRua() {
        return rua;
    }

    public StringFilter rua() {
        if (rua == null) {
            rua = new StringFilter();
        }
        return rua;
    }

    public void setRua(StringFilter rua) {
        this.rua = rua;
    }

    public StringFilter getNumero() {
        return numero;
    }

    public StringFilter numero() {
        if (numero == null) {
            numero = new StringFilter();
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public StringFilter getBairro() {
        return bairro;
    }

    public StringFilter bairro() {
        if (bairro == null) {
            bairro = new StringFilter();
        }
        return bairro;
    }

    public void setBairro(StringFilter bairro) {
        this.bairro = bairro;
    }

    public StringFilter getCidade() {
        return cidade;
    }

    public StringFilter cidade() {
        if (cidade == null) {
            cidade = new StringFilter();
        }
        return cidade;
    }

    public void setCidade(StringFilter cidade) {
        this.cidade = cidade;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public StringFilter estado() {
        if (estado == null) {
            estado = new StringFilter();
        }
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public StringFilter getCep() {
        return cep;
    }

    public StringFilter cep() {
        if (cep == null) {
            cep = new StringFilter();
        }
        return cep;
    }

    public void setCep(StringFilter cep) {
        this.cep = cep;
    }

    public StringFilter getPais() {
        return pais;
    }

    public StringFilter pais() {
        if (pais == null) {
            pais = new StringFilter();
        }
        return pais;
    }

    public void setPais(StringFilter pais) {
        this.pais = pais;
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

    public StringFilter getRazaosocial() {
        return razaosocial;
    }

    public StringFilter razaosocial() {
        if (razaosocial == null) {
            razaosocial = new StringFilter();
        }
        return razaosocial;
    }

    public void setRazaosocial(StringFilter razaosocial) {
        this.razaosocial = razaosocial;
    }

    public StringFilter getTelefoneComercial() {
        return telefoneComercial;
    }

    public StringFilter telefoneComercial() {
        if (telefoneComercial == null) {
            telefoneComercial = new StringFilter();
        }
        return telefoneComercial;
    }

    public void setTelefoneComercial(StringFilter telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final PerfilCriteria that = (PerfilCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipoConta, that.tipoConta) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(cnpj, that.cnpj) &&
            Objects.equals(rua, that.rua) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(bairro, that.bairro) &&
            Objects.equals(cidade, that.cidade) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(cep, that.cep) &&
            Objects.equals(pais, that.pais) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(razaosocial, that.razaosocial) &&
            Objects.equals(telefoneComercial, that.telefoneComercial) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tipoConta,
            cpf,
            cnpj,
            rua,
            numero,
            bairro,
            cidade,
            estado,
            cep,
            pais,
            nome,
            razaosocial,
            telefoneComercial,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerfilCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipoConta != null ? "tipoConta=" + tipoConta + ", " : "") +
            (cpf != null ? "cpf=" + cpf + ", " : "") +
            (cnpj != null ? "cnpj=" + cnpj + ", " : "") +
            (rua != null ? "rua=" + rua + ", " : "") +
            (numero != null ? "numero=" + numero + ", " : "") +
            (bairro != null ? "bairro=" + bairro + ", " : "") +
            (cidade != null ? "cidade=" + cidade + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (cep != null ? "cep=" + cep + ", " : "") +
            (pais != null ? "pais=" + pais + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (razaosocial != null ? "razaosocial=" + razaosocial + ", " : "") +
            (telefoneComercial != null ? "telefoneComercial=" + telefoneComercial + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
