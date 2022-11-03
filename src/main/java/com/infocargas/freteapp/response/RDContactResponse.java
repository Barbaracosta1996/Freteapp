package com.infocargas.freteapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class RDContactResponse {

    @JsonProperty("event_uuid")
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RDContactResponse)) return false;
        if (!super.equals(o)) return false;

        RDContactResponse that = (RDContactResponse) o;

        return getUuid() != null ? getUuid().equals(that.getUuid()) : that.getUuid() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getUuid() != null ? getUuid().hashCode() : 0);
        return result;
    }
}
