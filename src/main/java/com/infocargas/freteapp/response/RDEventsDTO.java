package com.infocargas.freteapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RDEventsDTO {

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("event_family")
    private String eventFamily;

    private RDContactDTO payload;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventFamily() {
        return eventFamily;
    }

    public void setEventFamily(String eventFamily) {
        this.eventFamily = eventFamily;
    }

    public RDContactDTO getPayload() {
        return payload;
    }

    public void setPayload(RDContactDTO payload) {
        this.payload = payload;
    }
}
