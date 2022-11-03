package com.infocargas.freteapp.response.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import liquibase.pro.packaged.S;

public class FacebookResponseContacts {

    private Map<String, String> profile;

    private String input;

    @JsonProperty("wa_id")
    private String waId;

    public Map<String, String> getProfile() {
        return profile;
    }

    public void setProfile(Map<String, String> profile) {
        this.profile = profile;
    }

    public String getWaId() {
        return waId;
    }

    public void setWaId(String waId) {
        this.waId = waId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
