package com.infocargas.freteapp.response.facebook;

import java.util.List;

public class FacebookResponse {

    /**
     * The specific webhook a business is subscribed to. The webhook is whatsapp_business_account.
     */
    private String object;

    private List<FacebookResponseEntry> entry;

    private FacebookException error;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<FacebookResponseEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<FacebookResponseEntry> entry) {
        this.entry = entry;
    }

    public FacebookException getError() {
        return error;
    }

    public void setError(FacebookException error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookResponse)) return false;

        FacebookResponse that = (FacebookResponse) o;

        if (getObject() != null ? !getObject().equals(that.getObject()) : that.getObject() != null) return false;
        if (getEntry() != null ? !getEntry().equals(that.getEntry()) : that.getEntry() != null) return false;
        return getError() != null ? getError().equals(that.getError()) : that.getError() == null;
    }

    @Override
    public int hashCode() {
        int result = getObject() != null ? getObject().hashCode() : 0;
        result = 31 * result + (getEntry() != null ? getEntry().hashCode() : 0);
        result = 31 * result + (getError() != null ? getError().hashCode() : 0);
        return result;
    }
}
