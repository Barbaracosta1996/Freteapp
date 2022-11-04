package com.infocargas.freteapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.SettingsContracts} entity.
 */
public class SettingsContractsDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] terms;

    private String termsContentType;

    @Lob
    private byte[] contractDefault;

    private String contractDefaultContentType;

    @Lob
    private byte[] privacy;

    private String privacyContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getTerms() {
        return terms;
    }

    public void setTerms(byte[] terms) {
        this.terms = terms;
    }

    public String getTermsContentType() {
        return termsContentType;
    }

    public void setTermsContentType(String termsContentType) {
        this.termsContentType = termsContentType;
    }

    public byte[] getContractDefault() {
        return contractDefault;
    }

    public void setContractDefault(byte[] contractDefault) {
        this.contractDefault = contractDefault;
    }

    public String getContractDefaultContentType() {
        return contractDefaultContentType;
    }

    public void setContractDefaultContentType(String contractDefaultContentType) {
        this.contractDefaultContentType = contractDefaultContentType;
    }

    public byte[] getPrivacy() {
        return privacy;
    }

    public void setPrivacy(byte[] privacy) {
        this.privacy = privacy;
    }

    public String getPrivacyContentType() {
        return privacyContentType;
    }

    public void setPrivacyContentType(String privacyContentType) {
        this.privacyContentType = privacyContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettingsContractsDTO)) {
            return false;
        }

        SettingsContractsDTO settingsContractsDTO = (SettingsContractsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, settingsContractsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettingsContractsDTO{" +
            "id=" + getId() +
            ", terms='" + getTerms() + "'" +
            ", contractDefault='" + getContractDefault() + "'" +
            ", privacy='" + getPrivacy() + "'" +
            "}";
    }
}
