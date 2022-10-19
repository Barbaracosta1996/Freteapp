package com.infocargas.freteapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoCarga;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.TipoTransporteOferta;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Ofertas.
 */
@Entity
@Table(name = "ofertas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ofertas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_postagem", nullable = false)
    private ZonedDateTime dataPostagem;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_carga", nullable = false)
    private TipoCarga tipoCarga;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "localizacao_origem", nullable = false)
    private String localizacaoOrigem;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "localizacao_destino", nullable = false)
    private String localizacaoDestino;

    @Column(name = "data_coleta")
    private LocalDate dataColeta;

    @Column(name = "data_entrega")
    private ZonedDateTime dataEntrega;

    @Column(name = "data_modificacao")
    private ZonedDateTime dataModificacao;

    @Column(name = "data_fechamento")
    private ZonedDateTime dataFechamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusOferta status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_oferta", nullable = false)
    private TipoOferta tipoOferta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transporte")
    private TipoTransporteOferta tipoTransporte;

    @NotNull
    @Column(name = "destino", nullable = false)
    private String destino;

    @NotNull
    @Column(name = "origem", nullable = false)
    private String origem;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Perfil perfil;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ofertas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataPostagem() {
        return this.dataPostagem;
    }

    public Ofertas dataPostagem(ZonedDateTime dataPostagem) {
        this.setDataPostagem(dataPostagem);
        return this;
    }

    public void setDataPostagem(ZonedDateTime dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public Integer getQuantidade() {
        return this.quantidade;
    }

    public Ofertas quantidade(Integer quantidade) {
        this.setQuantidade(quantidade);
        return this;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public TipoCarga getTipoCarga() {
        return this.tipoCarga;
    }

    public Ofertas tipoCarga(TipoCarga tipoCarga) {
        this.setTipoCarga(tipoCarga);
        return this;
    }

    public void setTipoCarga(TipoCarga tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public String getLocalizacaoOrigem() {
        return this.localizacaoOrigem;
    }

    public Ofertas localizacaoOrigem(String localizacaoOrigem) {
        this.setLocalizacaoOrigem(localizacaoOrigem);
        return this;
    }

    public void setLocalizacaoOrigem(String localizacaoOrigem) {
        this.localizacaoOrigem = localizacaoOrigem;
    }

    public String getLocalizacaoDestino() {
        return this.localizacaoDestino;
    }

    public Ofertas localizacaoDestino(String localizacaoDestino) {
        this.setLocalizacaoDestino(localizacaoDestino);
        return this;
    }

    public void setLocalizacaoDestino(String localizacaoDestino) {
        this.localizacaoDestino = localizacaoDestino;
    }

    public LocalDate getDataColeta() {
        return this.dataColeta;
    }

    public Ofertas dataColeta(LocalDate dataColeta) {
        this.setDataColeta(dataColeta);
        return this;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }

    public ZonedDateTime getDataEntrega() {
        return this.dataEntrega;
    }

    public Ofertas dataEntrega(ZonedDateTime dataEntrega) {
        this.setDataEntrega(dataEntrega);
        return this;
    }

    public void setDataEntrega(ZonedDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public ZonedDateTime getDataModificacao() {
        return this.dataModificacao;
    }

    public Ofertas dataModificacao(ZonedDateTime dataModificacao) {
        this.setDataModificacao(dataModificacao);
        return this;
    }

    public void setDataModificacao(ZonedDateTime dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public ZonedDateTime getDataFechamento() {
        return this.dataFechamento;
    }

    public Ofertas dataFechamento(ZonedDateTime dataFechamento) {
        this.setDataFechamento(dataFechamento);
        return this;
    }

    public void setDataFechamento(ZonedDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public StatusOferta getStatus() {
        return this.status;
    }

    public Ofertas status(StatusOferta status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusOferta status) {
        this.status = status;
    }

    public TipoOferta getTipoOferta() {
        return this.tipoOferta;
    }

    public Ofertas tipoOferta(TipoOferta tipoOferta) {
        this.setTipoOferta(tipoOferta);
        return this;
    }

    public void setTipoOferta(TipoOferta tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    public TipoTransporteOferta getTipoTransporte() {
        return this.tipoTransporte;
    }

    public Ofertas tipoTransporte(TipoTransporteOferta tipoTransporte) {
        this.setTipoTransporte(tipoTransporte);
        return this;
    }

    public void setTipoTransporte(TipoTransporteOferta tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public String getDestino() {
        return this.destino;
    }

    public Ofertas destino(String destino) {
        this.setDestino(destino);
        return this;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getOrigem() {
        return this.origem;
    }

    public Ofertas origem(String origem) {
        this.setOrigem(origem);
        return this;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public Perfil getPerfil() {
        return this.perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Ofertas perfil(Perfil perfil) {
        this.setPerfil(perfil);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ofertas)) {
            return false;
        }
        return id != null && id.equals(((Ofertas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ofertas{" +
            "id=" + getId() +
            ", dataPostagem='" + getDataPostagem() + "'" +
            ", quantidade=" + getQuantidade() +
            ", tipoCarga='" + getTipoCarga() + "'" +
            ", localizacaoOrigem='" + getLocalizacaoOrigem() + "'" +
            ", localizacaoDestino='" + getLocalizacaoDestino() + "'" +
            ", dataColeta='" + getDataColeta() + "'" +
            ", dataEntrega='" + getDataEntrega() + "'" +
            ", dataModificacao='" + getDataModificacao() + "'" +
            ", dataFechamento='" + getDataFechamento() + "'" +
            ", status='" + getStatus() + "'" +
            ", tipoOferta='" + getTipoOferta() + "'" +
            ", tipoTransporte='" + getTipoTransporte() + "'" +
            ", destino='" + getDestino() + "'" +
            ", origem='" + getOrigem() + "'" +
            "}";
    }
}
