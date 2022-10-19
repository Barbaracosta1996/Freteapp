package com.infocargas.freteapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infocargas.freteapp.domain.enumeration.AnwserStatus;
import com.infocargas.freteapp.domain.enumeration.StatusSolicitacao;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Solicitacao.
 */
@Entity
@Table(name = "solicitacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Solicitacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_proposta", nullable = false)
    private ZonedDateTime dataProposta;

    @Column(name = "data_modificacao")
    private ZonedDateTime dataModificacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "aceitar")
    private AnwserStatus aceitar;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusSolicitacao status;

    @Column(name = "obs")
    private String obs;

    @Column(name = "valor_frete")
    private Double valorFrete;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "perfil" }, allowSetters = true)
    private Ofertas ofertas;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Perfil perfil;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Solicitacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataProposta() {
        return this.dataProposta;
    }

    public Solicitacao dataProposta(ZonedDateTime dataProposta) {
        this.setDataProposta(dataProposta);
        return this;
    }

    public void setDataProposta(ZonedDateTime dataProposta) {
        this.dataProposta = dataProposta;
    }

    public ZonedDateTime getDataModificacao() {
        return this.dataModificacao;
    }

    public Solicitacao dataModificacao(ZonedDateTime dataModificacao) {
        this.setDataModificacao(dataModificacao);
        return this;
    }

    public void setDataModificacao(ZonedDateTime dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public AnwserStatus getAceitar() {
        return this.aceitar;
    }

    public Solicitacao aceitar(AnwserStatus aceitar) {
        this.setAceitar(aceitar);
        return this;
    }

    public void setAceitar(AnwserStatus aceitar) {
        this.aceitar = aceitar;
    }

    public StatusSolicitacao getStatus() {
        return this.status;
    }

    public Solicitacao status(StatusSolicitacao status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }

    public String getObs() {
        return this.obs;
    }

    public Solicitacao obs(String obs) {
        this.setObs(obs);
        return this;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Double getValorFrete() {
        return this.valorFrete;
    }

    public Solicitacao valorFrete(Double valorFrete) {
        this.setValorFrete(valorFrete);
        return this;
    }

    public void setValorFrete(Double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Ofertas getOfertas() {
        return this.ofertas;
    }

    public void setOfertas(Ofertas ofertas) {
        this.ofertas = ofertas;
    }

    public Solicitacao ofertas(Ofertas ofertas) {
        this.setOfertas(ofertas);
        return this;
    }

    public Perfil getPerfil() {
        return this.perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Solicitacao perfil(Perfil perfil) {
        this.setPerfil(perfil);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Solicitacao)) {
            return false;
        }
        return id != null && id.equals(((Solicitacao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Solicitacao{" +
            "id=" + getId() +
            ", dataProposta='" + getDataProposta() + "'" +
            ", dataModificacao='" + getDataModificacao() + "'" +
            ", aceitar='" + getAceitar() + "'" +
            ", status='" + getStatus() + "'" +
            ", obs='" + getObs() + "'" +
            ", valorFrete=" + getValorFrete() +
            "}";
    }
}
