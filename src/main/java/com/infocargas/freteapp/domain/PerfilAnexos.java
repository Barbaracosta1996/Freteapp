package com.infocargas.freteapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infocargas.freteapp.domain.enumeration.TipoPerfilDocumento;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PerfilAnexos.
 */
@Entity
@Table(name = "perfil_anexos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PerfilAnexos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_postagem", nullable = false)
    private LocalDate dataPostagem;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TipoPerfilDocumento tipoDocumento;

    @Column(name = "descricao")
    private String descricao;

    @Lob
    @Column(name = "url_file")
    private byte[] urlFile;

    @Column(name = "url_file_content_type")
    private String urlFileContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Perfil perfil;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PerfilAnexos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataPostagem() {
        return this.dataPostagem;
    }

    public PerfilAnexos dataPostagem(LocalDate dataPostagem) {
        this.setDataPostagem(dataPostagem);
        return this;
    }

    public void setDataPostagem(LocalDate dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public TipoPerfilDocumento getTipoDocumento() {
        return this.tipoDocumento;
    }

    public PerfilAnexos tipoDocumento(TipoPerfilDocumento tipoDocumento) {
        this.setTipoDocumento(tipoDocumento);
        return this;
    }

    public void setTipoDocumento(TipoPerfilDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public PerfilAnexos descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getUrlFile() {
        return this.urlFile;
    }

    public PerfilAnexos urlFile(byte[] urlFile) {
        this.setUrlFile(urlFile);
        return this;
    }

    public void setUrlFile(byte[] urlFile) {
        this.urlFile = urlFile;
    }

    public String getUrlFileContentType() {
        return this.urlFileContentType;
    }

    public PerfilAnexos urlFileContentType(String urlFileContentType) {
        this.urlFileContentType = urlFileContentType;
        return this;
    }

    public void setUrlFileContentType(String urlFileContentType) {
        this.urlFileContentType = urlFileContentType;
    }

    public Perfil getPerfil() {
        return this.perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public PerfilAnexos perfil(Perfil perfil) {
        this.setPerfil(perfil);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PerfilAnexos)) {
            return false;
        }
        return id != null && id.equals(((PerfilAnexos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerfilAnexos{" +
            "id=" + getId() +
            ", dataPostagem='" + getDataPostagem() + "'" +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", urlFile='" + getUrlFile() + "'" +
            ", urlFileContentType='" + getUrlFileContentType() + "'" +
            "}";
    }
}
