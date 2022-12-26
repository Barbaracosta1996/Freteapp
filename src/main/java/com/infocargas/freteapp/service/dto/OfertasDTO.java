package com.infocargas.freteapp.service.dto;

import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoCarga;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.TipoTransporteOferta;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.Ofertas} entity.
 */
public class OfertasDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime dataPostagem;

    @NotNull
    @Min(value = 1)
    private Integer quantidade;

    @NotNull
    private TipoCarga tipoCarga;

    @Lob
    private String localizacaoOrigem;

    @Lob
    private String localizacaoDestino;

    private LocalDate dataColeta;

    private ZonedDateTime dataEntrega;

    private ZonedDateTime dataModificacao;

    private ZonedDateTime dataFechamento;

    @NotNull
    private StatusOferta status;

    @NotNull
    private TipoOferta tipoOferta;

    private TipoTransporteOferta tipoTransporte;

    @NotNull
    private String destino;

    @NotNull
    private String origem;

    private PerfilDTO perfil;

    private PerfilDTO perfilCompleto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataPostagem() {
        return dataPostagem;
    }

    public void setDataPostagem(ZonedDateTime dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public TipoCarga getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(TipoCarga tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public String getLocalizacaoOrigem() {
        return localizacaoOrigem;
    }

    public void setLocalizacaoOrigem(String localizacaoOrigem) {
        this.localizacaoOrigem = localizacaoOrigem;
    }

    public String getLocalizacaoDestino() {
        return localizacaoDestino;
    }

    public void setLocalizacaoDestino(String localizacaoDestino) {
        this.localizacaoDestino = localizacaoDestino;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }

    public ZonedDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(ZonedDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public ZonedDateTime getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(ZonedDateTime dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public ZonedDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(ZonedDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public StatusOferta getStatus() {
        return status;
    }

    public void setStatus(StatusOferta status) {
        this.status = status;
    }

    public TipoOferta getTipoOferta() {
        return tipoOferta;
    }

    public void setTipoOferta(TipoOferta tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    public TipoTransporteOferta getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoTransporteOferta tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public PerfilDTO getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilDTO perfil) {
        this.perfil = perfil;
    }

    public PerfilDTO getPerfilCompleto() {
        return perfilCompleto;
    }

    public void setPerfilCompleto(PerfilDTO perfilCompleto) {
        this.perfilCompleto = perfilCompleto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OfertasDTO)) {
            return false;
        }

        OfertasDTO ofertasDTO = (OfertasDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ofertasDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OfertasDTO{" +
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
            ", perfil=" + getPerfil() +
            "}";
    }
}
