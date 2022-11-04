package com.infocargas.freteapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SettingsContracts.
 */
@Entity
@Table(name = "settings_contracts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SettingsContracts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "terms")
    private byte[] terms;

    @Column(name = "terms_content_type")
    private String termsContentType;

    @Lob
    @Column(name = "contract_default")
    private byte[] contractDefault;

    @Column(name = "contract_default_content_type")
    private String contractDefaultContentType;

    @Lob
    @Column(name = "privacy")
    private byte[] privacy;

    @Column(name = "privacy_content_type")
    private String privacyContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SettingsContracts id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getTerms() {
        return this.terms;
    }

    public SettingsContracts terms(byte[] terms) {
        this.setTerms(terms);
        return this;
    }

    public void setTerms(byte[] terms) {
        this.terms = terms;
    }

    public String getTermsContentType() {
        return this.termsContentType;
    }

    public SettingsContracts termsContentType(String termsContentType) {
        this.termsContentType = termsContentType;
        return this;
    }

    public void setTermsContentType(String termsContentType) {
        this.termsContentType = termsContentType;
    }

    public byte[] getContractDefault() {
        return this.contractDefault;
    }

    public SettingsContracts contractDefault(byte[] contractDefault) {
        this.setContractDefault(contractDefault);
        return this;
    }

    public void setContractDefault(byte[] contractDefault) {
        this.contractDefault = contractDefault;
    }

    public String getContractDefaultContentType() {
        return this.contractDefaultContentType;
    }

    public SettingsContracts contractDefaultContentType(String contractDefaultContentType) {
        this.contractDefaultContentType = contractDefaultContentType;
        return this;
    }

    public void setContractDefaultContentType(String contractDefaultContentType) {
        this.contractDefaultContentType = contractDefaultContentType;
    }

    public byte[] getPrivacy() {
        return this.privacy;
    }

    public SettingsContracts privacy(byte[] privacy) {
        this.setPrivacy(privacy);
        return this;
    }

    public void setPrivacy(byte[] privacy) {
        this.privacy = privacy;
    }

    public String getPrivacyContentType() {
        return this.privacyContentType;
    }

    public SettingsContracts privacyContentType(String privacyContentType) {
        this.privacyContentType = privacyContentType;
        return this;
    }

    public void setPrivacyContentType(String privacyContentType) {
        this.privacyContentType = privacyContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettingsContracts)) {
            return false;
        }
        return id != null && id.equals(((SettingsContracts) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettingsContracts{" +
            "id=" + getId() +
            ", terms='" + getTerms() + "'" +
            ", termsContentType='" + getTermsContentType() + "'" +
            ", contractDefault='" + getContractDefault() + "'" +
            ", contractDefaultContentType='" + getContractDefaultContentType() + "'" +
            ", privacy='" + getPrivacy() + "'" +
            ", privacyContentType='" + getPrivacyContentType() + "'" +
            "}";
    }
}
