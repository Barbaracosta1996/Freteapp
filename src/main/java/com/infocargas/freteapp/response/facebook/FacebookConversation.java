package com.infocargas.freteapp.response.facebook;

import java.sql.Timestamp;
import java.util.Map;

public class FacebookConversation {

    private String id;
    private Timestamp expiration_timestamp;
    private Map<String, String> origin;
    private FacebookResponsePricing pricing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getExpiration_timestamp() {
        return expiration_timestamp;
    }

    public void setExpiration_timestamp(Timestamp expiration_timestamp) {
        this.expiration_timestamp = expiration_timestamp;
    }

    public Map<String, String> getOrigin() {
        return origin;
    }

    public void setOrigin(Map<String, String> origin) {
        this.origin = origin;
    }

    public FacebookResponsePricing getPricing() {
        return pricing;
    }

    public void setPricing(FacebookResponsePricing pricing) {
        this.pricing = pricing;
    }

    @Override
    public String toString() {
        return (
            "FacebookConversation{" +
            "id='" +
            id +
            '\'' +
            ", expiration_timestamp=" +
            expiration_timestamp +
            ", origin=" +
            origin +
            ", pricing=" +
            pricing +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookConversation)) return false;

        FacebookConversation that = (FacebookConversation) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (
            getExpiration_timestamp() != null
                ? !getExpiration_timestamp().equals(that.getExpiration_timestamp())
                : that.getExpiration_timestamp() != null
        ) return false;
        if (getOrigin() != null ? !getOrigin().equals(that.getOrigin()) : that.getOrigin() != null) return false;
        return getPricing() != null ? getPricing().equals(that.getPricing()) : that.getPricing() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getExpiration_timestamp() != null ? getExpiration_timestamp().hashCode() : 0);
        result = 31 * result + (getOrigin() != null ? getOrigin().hashCode() : 0);
        result = 31 * result + (getPricing() != null ? getPricing().hashCode() : 0);
        return result;
    }
}
