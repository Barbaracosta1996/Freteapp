package com.infocargas.freteapp.service.dto;

import com.infocargas.freteapp.domain.enumeration.TipoCategoria;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.Frota} entity.
 */
public class FrotaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String modelo;

    private String marca;

    @NotNull
    @Size(max = 4)
    private String ano;

    @NotNull
    private TipoCategoria tipoCategoria;

    private String obsCategoriaOutro;

    private PerfilDTO perfil;

    private CategoriaVeiculoDTO categoriaVeiculo;

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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public TipoCategoria getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(TipoCategoria tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public String getObsCategoriaOutro() {
        return obsCategoriaOutro;
    }

    public void setObsCategoriaOutro(String obsCategoriaOutro) {
        this.obsCategoriaOutro = obsCategoriaOutro;
    }

    public PerfilDTO getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilDTO perfil) {
        this.perfil = perfil;
    }

    public CategoriaVeiculoDTO getCategoriaVeiculo() {
        return categoriaVeiculo;
    }

    public void setCategoriaVeiculo(CategoriaVeiculoDTO categoriaVeiculo) {
        this.categoriaVeiculo = categoriaVeiculo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FrotaDTO)) {
            return false;
        }

        FrotaDTO frotaDTO = (FrotaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, frotaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrotaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", marca='" + getMarca() + "'" +
            ", ano='" + getAno() + "'" +
            ", tipoCategoria='" + getTipoCategoria() + "'" +
            ", obsCategoriaOutro='" + getObsCategoriaOutro() + "'" +
            ", perfil=" + getPerfil() +
            ", categoriaVeiculo=" + getCategoriaVeiculo() +
            "}";
    }
}
