package com.infocargas.freteapp.web.rest.vm.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FacebookComponetsVM {

    /**
     * Componet type to use in WhatsApp.
     * Exemple: header, body, footer, button,
     */
    private String type;

    /**
     * Button Type created.
     * quick_reply, url
     */
    @JsonProperty("sub_type")
    private String subType;

    /**
     * Position index of buttons
     * [0, 1, 3] max three buttons
     */
    private Integer index;

    /**
     * Matrix have content type of message
     * see https://developers.facebook.com/docs/whatsapp/on-premises/reference/messages
     */
    private List<FacebookParameterVM> parameters;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<FacebookParameterVM> getParameters() {
        return parameters;
    }

    public void setParameters(List<FacebookParameterVM> parameters) {
        this.parameters = parameters;
    }
}
