package com.infocargas.freteapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A RotasOfertas.
 */
@Entity
@Table(name = "rotas_ofertas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RotasOfertas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "rotas", nullable = false)
    private String rotas;

    @JsonIgnoreProperties(value = { "perfil" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Ofertas ofertas;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RotasOfertas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRotas() {
        return this.rotas;
    }

    public RotasOfertas rotas(String rotas) {
        this.setRotas(rotas);
        return this;
    }

    public void setRotas(String rotas) {
        this.rotas = rotas;
    }

    public Ofertas getOfertas() {
        return this.ofertas;
    }

    public void setOfertas(Ofertas ofertas) {
        this.ofertas = ofertas;
    }

    public RotasOfertas ofertas(Ofertas ofertas) {
        this.setOfertas(ofertas);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RotasOfertas)) {
            return false;
        }
        return id != null && id.equals(((RotasOfertas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RotasOfertas{" +
            "id=" + getId() +
            ", rotas='" + getRotas() + "'" +
            "}";
    }
}
