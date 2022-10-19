package com.infocargas.freteapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infocargas.freteapp.domain.enumeration.TipoCategoria;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Frota.
 */
@Entity
@Table(name = "frota")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Frota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "marca")
    private String marca;

    @NotNull
    @Size(max = 4)
    @Column(name = "ano", length = 4, nullable = false)
    private String ano;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_categoria", nullable = false)
    private TipoCategoria tipoCategoria;

    @Column(name = "obs_categoria_outro")
    private String obsCategoriaOutro;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Perfil perfil;

    @ManyToOne(optional = false)
    @NotNull
    private CategoriaVeiculo categoriaVeiculo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Frota id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Frota nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return this.modelo;
    }

    public Frota modelo(String modelo) {
        this.setModelo(modelo);
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return this.marca;
    }

    public Frota marca(String marca) {
        this.setMarca(marca);
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getAno() {
        return this.ano;
    }

    public Frota ano(String ano) {
        this.setAno(ano);
        return this;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public TipoCategoria getTipoCategoria() {
        return this.tipoCategoria;
    }

    public Frota tipoCategoria(TipoCategoria tipoCategoria) {
        this.setTipoCategoria(tipoCategoria);
        return this;
    }

    public void setTipoCategoria(TipoCategoria tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public String getObsCategoriaOutro() {
        return this.obsCategoriaOutro;
    }

    public Frota obsCategoriaOutro(String obsCategoriaOutro) {
        this.setObsCategoriaOutro(obsCategoriaOutro);
        return this;
    }

    public void setObsCategoriaOutro(String obsCategoriaOutro) {
        this.obsCategoriaOutro = obsCategoriaOutro;
    }

    public Perfil getPerfil() {
        return this.perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Frota perfil(Perfil perfil) {
        this.setPerfil(perfil);
        return this;
    }

    public CategoriaVeiculo getCategoriaVeiculo() {
        return this.categoriaVeiculo;
    }

    public void setCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
        this.categoriaVeiculo = categoriaVeiculo;
    }

    public Frota categoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
        this.setCategoriaVeiculo(categoriaVeiculo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Frota)) {
            return false;
        }
        return id != null && id.equals(((Frota) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Frota{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", marca='" + getMarca() + "'" +
            ", ano='" + getAno() + "'" +
            ", tipoCategoria='" + getTipoCategoria() + "'" +
            ", obsCategoriaOutro='" + getObsCategoriaOutro() + "'" +
            "}";
    }
}
