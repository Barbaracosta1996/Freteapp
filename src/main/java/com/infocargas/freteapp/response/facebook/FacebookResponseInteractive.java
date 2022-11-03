package com.infocargas.freteapp.response.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class FacebookResponseInteractive {

    private String type;

    @JsonProperty("list_reply")
    private Map<String, String> listReply;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getListReply() {
        return listReply;
    }

    public void setListReply(Map<String, String> listReply) {
        this.listReply = listReply;
    }
}
