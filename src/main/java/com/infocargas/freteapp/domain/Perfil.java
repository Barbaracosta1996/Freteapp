package com.infocargas.freteapp.domain;

import com.infocargas.freteapp.domain.enumeration.TipoConta;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Perfil.
 */
@Entity
@Table(name = "perfil")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Perfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", nullable = false)
    private TipoConta tipoConta;

    @Size(min = 11)
    @Column(name = "cpf")
    private String cpf;

    @Size(min = 14)
    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "rua")
    private String rua;

    @Column(name = "numero")
    private String numero;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cep")
    private String cep;

    @Column(name = "pais")
    private String pais;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "razaosocial")
    private String razaosocial;

    @Column(name = "telefone_comercial")
    private String telefoneComercial;

    @Column(name = "uuid_rd")
    private UUID uuidRD;

    @Column(name = "wa_id")
    private String waId;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Perfil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoConta getTipoConta() {
        return this.tipoConta;
    }

    public Perfil tipoConta(TipoConta tipoConta) {
        this.setTipoConta(tipoConta);
        return this;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Perfil cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public Perfil cnpj(String cnpj) {
        this.setCnpj(cnpj);
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRua() {
        return this.rua;
    }

    public Perfil rua(String rua) {
        this.setRua(rua);
        return this;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return this.numero;
    }

    public Perfil numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Perfil bairro(String bairro) {
        this.setBairro(bairro);
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return this.cidade;
    }

    public Perfil cidade(String cidade) {
        this.setCidade(cidade);
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return this.estado;
    }

    public Perfil estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return this.cep;
    }

    public Perfil cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return this.pais;
    }

    public Perfil pais(String pais) {
        this.setPais(pais);
        return this;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNome() {
        return this.nome;
    }

    public Perfil nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaosocial() {
        return this.razaosocial;
    }

    public Perfil razaosocial(String razaosocial) {
        this.setRazaosocial(razaosocial);
        return this;
    }

    public void setRazaosocial(String razaosocial) {
        this.razaosocial = razaosocial;
    }

    public String getTelefoneComercial() {
        return this.telefoneComercial;
    }

    public Perfil telefoneComercial(String telefoneComercial) {
        this.setTelefoneComercial(telefoneComercial);
        return this;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Perfil user(User user) {
        this.setUser(user);
        return this;
    }

    public UUID getUuidRD() {
        return uuidRD;
    }

    public void setUuidRD(UUID uuidRD) {
        this.uuidRD = uuidRD;
    }

    public String getWaId() {
        return waId;
    }

    public void setWaId(String waId) {
        this.waId = waId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Perfil)) {
            return false;
        }
        return id != null && id.equals(((Perfil) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Perfil{" +
            "id=" + getId() +
            ", tipoConta='" + getTipoConta() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", rua='" + getRua() + "'" +
            ", numero='" + getNumero() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", estado='" + getEstado() + "'" +
            ", cep='" + getCep() + "'" +
            ", pais='" + getPais() + "'" +
            ", nome='" + getNome() + "'" +
            ", razaosocial='" + getRazaosocial() + "'" +
            ", telefoneComercial='" + getTelefoneComercial() + "'" +
            "}";
    }
}
