package com.infocargas.freteapp.web.rest.vm.facebook;

import java.util.Map;

public class FacebookInteractiveVM {

    private String type;
    private FacebookParameterVM header;
    private Map<String, String> body;
    private Map<String, String> footer;
    private FacebookActionsVM action;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FacebookParameterVM getHeader() {
        return header;
    }

    public void setHeader(FacebookParameterVM header) {
        this.header = header;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public Map<String, String> getFooter() {
        return footer;
    }

    public void setFooter(Map<String, String> footer) {
        this.footer = footer;
    }

    public FacebookActionsVM getAction() {
        return action;
    }

    public void setAction(FacebookActionsVM action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookInteractiveVM)) return false;

        FacebookInteractiveVM that = (FacebookInteractiveVM) o;

        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getHeader() != null ? !getHeader().equals(that.getHeader()) : that.getHeader() != null) return false;
        if (getBody() != null ? !getBody().equals(that.getBody()) : that.getBody() != null) return false;
        if (getFooter() != null ? !getFooter().equals(that.getFooter()) : that.getFooter() != null) return false;
        return getAction() != null ? getAction().equals(that.getAction()) : that.getAction() == null;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getHeader() != null ? getHeader().hashCode() : 0);
        result = 31 * result + (getBody() != null ? getBody().hashCode() : 0);
        result = 31 * result + (getFooter() != null ? getFooter().hashCode() : 0);
        result = 31 * result + (getAction() != null ? getAction().hashCode() : 0);
        return result;
    }
}
