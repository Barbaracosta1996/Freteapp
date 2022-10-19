package com.infocargas.freteapp.service.dto;

import com.infocargas.freteapp.domain.enumeration.TipoPerfilDocumento;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.PerfilAnexos} entity.
 */
public class PerfilAnexosDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dataPostagem;

    @NotNull
    private TipoPerfilDocumento tipoDocumento;

    private String descricao;

    @Lob
    private byte[] urlFile;

    private String urlFileContentType;
    private PerfilDTO perfil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataPostagem() {
        return dataPostagem;
    }

    public void setDataPostagem(LocalDate dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public TipoPerfilDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoPerfilDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(byte[] urlFile) {
        this.urlFile = urlFile;
    }

    public String getUrlFileContentType() {
        return urlFileContentType;
    }

    public void setUrlFileContentType(String urlFileContentType) {
        this.urlFileContentType = urlFileContentType;
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
        if (!(o instanceof PerfilAnexosDTO)) {
            return false;
        }

        PerfilAnexosDTO perfilAnexosDTO = (PerfilAnexosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, perfilAnexosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerfilAnexosDTO{" +
            "id=" + getId() +
            ", dataPostagem='" + getDataPostagem() + "'" +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", urlFile='" + getUrlFile() + "'" +
            ", perfil=" + getPerfil() +
            "}";
    }
}
