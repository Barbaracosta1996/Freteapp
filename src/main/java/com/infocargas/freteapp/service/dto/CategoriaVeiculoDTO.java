package com.infocargas.freteapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.CategoriaVeiculo} entity.
 */
public class CategoriaVeiculoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaVeiculoDTO)) {
            return false;
        }

        CategoriaVeiculoDTO categoriaVeiculoDTO = (CategoriaVeiculoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoriaVeiculoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaVeiculoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
