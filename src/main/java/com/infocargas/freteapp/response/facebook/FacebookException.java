package com.infocargas.freteapp.response.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class FacebookException {

    private String message;

    private String type;

    private String code;

    @JsonProperty("error_data")
    private Map<String, String> errorData;

    @JsonProperty("error_subcode")
    private Integer errorSubcode;

    @JsonProperty("fbtrace_id")
    private String fbtraceId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getErrorData() {
        return errorData;
    }

    public void setErrorData(Map<String, String> errorData) {
        this.errorData = errorData;
    }

    public Integer getErrorSubcode() {
        return errorSubcode;
    }

    public void setErrorSubcode(Integer errorSubcode) {
        this.errorSubcode = errorSubcode;
    }

    public String getFbtraceId() {
        return fbtraceId;
    }

    public void setFbtraceId(String fbtraceId) {
        this.fbtraceId = fbtraceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookException)) return false;

        FacebookException that = (FacebookException) o;

        if (getMessage() != null ? !getMessage().equals(that.getMessage()) : that.getMessage() != null) return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) return false;
        if (getErrorData() != null ? !getErrorData().equals(that.getErrorData()) : that.getErrorData() != null) return false;
        if (getErrorSubcode() != null ? !getErrorSubcode().equals(that.getErrorSubcode()) : that.getErrorSubcode() != null) return false;
        return getFbtraceId() != null ? getFbtraceId().equals(that.getFbtraceId()) : that.getFbtraceId() == null;
    }

    @Override
    public int hashCode() {
        int result = getMessage() != null ? getMessage().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + (getErrorData() != null ? getErrorData().hashCode() : 0);
        result = 31 * result + (getErrorSubcode() != null ? getErrorSubcode().hashCode() : 0);
        result = 31 * result + (getFbtraceId() != null ? getFbtraceId().hashCode() : 0);
        return result;
    }
}
