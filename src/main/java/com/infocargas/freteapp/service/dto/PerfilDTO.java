package com.infocargas.freteapp.service.dto;

import com.infocargas.freteapp.domain.enumeration.TipoConta;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.Perfil} entity.
 */
public class PerfilDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoConta tipoConta;

    @Size(min = 11)
    private String cpf;

    @Size(min = 14)
    private String cnpj;

    private String rua;

    private String numero;

    private String bairro;

    private String cidade;

    private String estado;

    private String cep;

    private String pais;

    @NotNull
    private String nome;

    private String razaosocial;

    private String telefoneComercial;

    private UserDTO user;

    private UUID uuidRD;

    private String waId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaosocial() {
        return razaosocial;
    }

    public void setRazaosocial(String razaosocial) {
        this.razaosocial = razaosocial;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PerfilDTO)) return false;

        PerfilDTO perfilDTO = (PerfilDTO) o;

        if (getId() != null ? !getId().equals(perfilDTO.getId()) : perfilDTO.getId() != null) return false;
        if (getTipoConta() != perfilDTO.getTipoConta()) return false;
        if (getCpf() != null ? !getCpf().equals(perfilDTO.getCpf()) : perfilDTO.getCpf() != null) return false;
        if (getCnpj() != null ? !getCnpj().equals(perfilDTO.getCnpj()) : perfilDTO.getCnpj() != null) return false;
        if (getRua() != null ? !getRua().equals(perfilDTO.getRua()) : perfilDTO.getRua() != null) return false;
        if (getNumero() != null ? !getNumero().equals(perfilDTO.getNumero()) : perfilDTO.getNumero() != null) return false;
        if (getBairro() != null ? !getBairro().equals(perfilDTO.getBairro()) : perfilDTO.getBairro() != null) return false;
        if (getCidade() != null ? !getCidade().equals(perfilDTO.getCidade()) : perfilDTO.getCidade() != null) return false;
        if (getEstado() != null ? !getEstado().equals(perfilDTO.getEstado()) : perfilDTO.getEstado() != null) return false;
        if (getCep() != null ? !getCep().equals(perfilDTO.getCep()) : perfilDTO.getCep() != null) return false;
        if (getPais() != null ? !getPais().equals(perfilDTO.getPais()) : perfilDTO.getPais() != null) return false;
        if (getNome() != null ? !getNome().equals(perfilDTO.getNome()) : perfilDTO.getNome() != null) return false;
        if (
            getRazaosocial() != null ? !getRazaosocial().equals(perfilDTO.getRazaosocial()) : perfilDTO.getRazaosocial() != null
        ) return false;
        if (
            getTelefoneComercial() != null
                ? !getTelefoneComercial().equals(perfilDTO.getTelefoneComercial())
                : perfilDTO.getTelefoneComercial() != null
        ) return false;
        if (getUser() != null ? !getUser().equals(perfilDTO.getUser()) : perfilDTO.getUser() != null) return false;
        if (getUuidRD() != null ? !getUuidRD().equals(perfilDTO.getUuidRD()) : perfilDTO.getUuidRD() != null) return false;
        return getWaId() != null ? getWaId().equals(perfilDTO.getWaId()) : perfilDTO.getWaId() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getTipoConta() != null ? getTipoConta().hashCode() : 0);
        result = 31 * result + (getCpf() != null ? getCpf().hashCode() : 0);
        result = 31 * result + (getCnpj() != null ? getCnpj().hashCode() : 0);
        result = 31 * result + (getRua() != null ? getRua().hashCode() : 0);
        result = 31 * result + (getNumero() != null ? getNumero().hashCode() : 0);
        result = 31 * result + (getBairro() != null ? getBairro().hashCode() : 0);
        result = 31 * result + (getCidade() != null ? getCidade().hashCode() : 0);
        result = 31 * result + (getEstado() != null ? getEstado().hashCode() : 0);
        result = 31 * result + (getCep() != null ? getCep().hashCode() : 0);
        result = 31 * result + (getPais() != null ? getPais().hashCode() : 0);
        result = 31 * result + (getNome() != null ? getNome().hashCode() : 0);
        result = 31 * result + (getRazaosocial() != null ? getRazaosocial().hashCode() : 0);
        result = 31 * result + (getTelefoneComercial() != null ? getTelefoneComercial().hashCode() : 0);
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        result = 31 * result + (getUuidRD() != null ? getUuidRD().hashCode() : 0);
        result = 31 * result + (getWaId() != null ? getWaId().hashCode() : 0);
        return result;
    }
}
