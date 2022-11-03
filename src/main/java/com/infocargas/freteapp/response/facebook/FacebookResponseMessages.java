package com.infocargas.freteapp.response.facebook;

import java.util.Map;

public class FacebookResponseMessages {

    private Map<String, String> context;

    private String from;

    private String id;

    private String timestamp;

    private String type;

    private FacebookResponseInteractive interactive;

    private FacebookButton button;

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FacebookResponseInteractive getInteractive() {
        return interactive;
    }

    public void setInteractive(FacebookResponseInteractive interactive) {
        this.interactive = interactive;
    }

    public FacebookButton getButton() {
        return button;
    }

    public void setButton(FacebookButton button) {
        this.button = button;
    }
}
