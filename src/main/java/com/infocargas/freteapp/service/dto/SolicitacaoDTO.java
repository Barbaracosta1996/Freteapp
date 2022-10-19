package com.infocargas.freteapp.service.dto;

import com.infocargas.freteapp.domain.enumeration.AnwserStatus;
import com.infocargas.freteapp.domain.enumeration.StatusSolicitacao;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.Solicitacao} entity.
 */
public class SolicitacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime dataProposta;

    private ZonedDateTime dataModificacao;

    private AnwserStatus aceitar;

    @NotNull
    private StatusSolicitacao status;

    private String obs;

    private Double valorFrete;

    private OfertasDTO ofertas;

    private PerfilDTO perfil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataProposta() {
        return dataProposta;
    }

    public void setDataProposta(ZonedDateTime dataProposta) {
        this.dataProposta = dataProposta;
    }

    public ZonedDateTime getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(ZonedDateTime dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public AnwserStatus getAceitar() {
        return aceitar;
    }

    public void setAceitar(AnwserStatus aceitar) {
        this.aceitar = aceitar;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(Double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public OfertasDTO getOfertas() {
        return ofertas;
    }

    public void setOfertas(OfertasDTO ofertas) {
        this.ofertas = ofertas;
    }

    public PerfilDTO getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilDTO perfil) {
        this.perfil = perfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolicitacaoDTO)) {
            return false;
        }

        SolicitacaoDTO solicitacaoDTO = (SolicitacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, solicitacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolicitacaoDTO{" +
            "id=" + getId() +
            ", dataProposta='" + getDataProposta() + "'" +
            ", dataModificacao='" + getDataModificacao() + "'" +
            ", aceitar='" + getAceitar() + "'" +
            ", status='" + getStatus() + "'" +
            ", obs='" + getObs() + "'" +
            ", valorFrete=" + getValorFrete() +
            ", ofertas=" + getOfertas() +
            ", perfil=" + getPerfil() +
            "}";
    }
}
