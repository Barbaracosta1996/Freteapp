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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettingsCargaApp)) {
            return false;
        }
        return id != null && id.equals(((SettingsCargaApp) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettingsCargaApp{" +
            "id=" + getId() +
            ", rdCode='" + getRdCode() + "'" +
            ", waToken='" + getWaToken() + "'" +
            "}";
    }
}
