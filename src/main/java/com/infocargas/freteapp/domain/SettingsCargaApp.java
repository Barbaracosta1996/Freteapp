package com.infocargas.freteapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SettingsCargaApp.
 */
@Entity
@Table(name = "settings_carga_app")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SettingsCargaApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rd_code")
    private String rdCode;

    @Lob
    @Column(name = "rd_app_token")
    private String rdAppToken;

    @Lob
    @Column(name = "rd_app_refresh_token")
    private String rdAppRefreshToken;

    @Column(name = "wa_token")
    private String waToken;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SettingsCargaApp id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRdCode() {
        return this.rdCode;
    }

    public SettingsCargaApp rdCode(String rdCode) {
        this.setRdCode(rdCode);
        return this;
    }

    public void setRdCode(String rdCode) {
        this.rdCode = rdCode;
    }

    public String getRdAppToken() {
        return rdAppToken;
    }

    public void setRdAppToken(String rdAppToken) {
        this.rdAppToken = rdAppToken;
    }

    public String getRdAppRefreshToken() {
        return rdAppRefreshToken;
    }

    public void setRdAppRefreshToken(String rdAppRefreshToken) {
        this.rdAppRefreshToken = rdAppRefreshToken;
    }

    public String getWaToken() {
        return this.waToken;
    }

    public SettingsCargaApp waToken(String waToken) {
        this.setWaToken(waToken);
        return this;
    }

    public void setWaToken(String waToken) {
        this.waToken = waToken;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    // prettier-ignore

    @Override
    public String toString() {
        return "SettingsCargaApp{" +
            "id=" + id +
            ", rdCode='" + rdCode + '\'' +
            ", rdAppToken='" + rdAppToken + '\'' +
            ", rdAppRefreshToken='" + rdAppRefreshToken + '\'' +
            ", waToken='" + waToken + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SettingsCargaApp)) return false;

        SettingsCargaApp that = (SettingsCargaApp) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getRdCode() != null ? !getRdCode().equals(that.getRdCode()) : that.getRdCode() != null) return false;
        if (getRdAppToken() != null ? !getRdAppToken().equals(that.getRdAppToken()) : that.getRdAppToken() != null) return false;
        if (
            getRdAppRefreshToken() != null
                ? !getRdAppRefreshToken().equals(that.getRdAppRefreshToken())
                : that.getRdAppRefreshToken() != null
        ) return false;
        return getWaToken() != null ? getWaToken().equals(that.getWaToken()) : that.getWaToken() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getRdCode() != null ? getRdCode().hashCode() : 0);
        result = 31 * result + (getRdAppToken() != null ? getRdAppToken().hashCode() : 0);
        result = 31 * result + (getRdAppRefreshToken() != null ? getRdAppRefreshToken().hashCode() : 0);
        result = 31 * result + (getWaToken() != null ? getWaToken().hashCode() : 0);
        return result;
    }
}
