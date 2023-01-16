package com.infocargas.freteapp.response.facebook;

import java.util.List;

public class FacebookResponseChange {

    private String field;

    private FacebookResponseValue value;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public FacebookResponseValue getValue() {
        return value;
    }

    public void setValue(FacebookResponseValue value) {
        this.value = value;
    }
}
