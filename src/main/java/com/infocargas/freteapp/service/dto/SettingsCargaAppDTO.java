package com.infocargas.freteapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.infocargas.freteapp.domain.SettingsCargaApp} entity.
 */
public class SettingsCargaAppDTO implements Serializable {

    private Long id;

    private String rdCode;

    private String waToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRdCode() {
        return rdCode;
    }

    public void setRdCode(String rdCode) {
        this.rdCode = rdCode;
    }

    public String getWaToken() {
        return waToken;
    }

    public void setWaToken(String waToken) {
        this.waToken = waToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettingsCargaAppDTO)) {
            return false;
        }

        SettingsCargaAppDTO settingsCargaAppDTO = (SettingsCargaAppDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, settingsCargaAppDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettingsCargaAppDTO{" +
            "id=" + getId() +
            ", rdCode='" + getRdCode() + "'" +
            ", waToken='" + getWaToken() + "'" +
            "}";
    }
}
