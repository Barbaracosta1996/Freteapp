package com.infocargas.freteapp.web.rest.vm.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Callback to valided token in facebook to receice the webhook.
 */
public class FacebookHubVM {

    private String mode;
    private Integer challenge;

    @JsonProperty("verify_token")
    private String verifyToken;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getChallenge() {
        return challenge;
    }

    public void setChallenge(Integer challenge) {
        this.challenge = challenge;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }
}
