package com.infocargas.freteapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.RotasOfertas} entity.
 */
public class RotasOfertasDTO implements Serializable {

    private Long id;

    @Lob
    private String rotas;

    private OfertasDTO ofertas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRotas() {
        return rotas;
    }

    public void setRotas(String rotas) {
        this.rotas = rotas;
    }

    public OfertasDTO getOfertas() {
        return ofertas;
    }

    public void setOfertas(OfertasDTO ofertas) {
        this.ofertas = ofertas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RotasOfertasDTO)) {
            return false;
        }

        RotasOfertasDTO rotasOfertasDTO = (RotasOfertasDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rotasOfertasDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RotasOfertasDTO{" +
            "id=" + getId() +
            ", rotas='" + getRotas() + "'" +
            ", ofertas=" + getOfertas() +
            "}";
    }
}
