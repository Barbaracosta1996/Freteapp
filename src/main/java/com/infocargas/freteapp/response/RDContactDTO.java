package com.infocargas.freteapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.Arrays;

public class RDContactDTO {

    @JsonProperty("conversion_identifier")
    private String conversionIdentifier;

    private String name;

    private String email;

    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("personal_phone")
    private String personalPhone;

    @JsonProperty("mobile_phone")
    private String mobile_phone;

    private String city;

    private String state;

    private String country;

    @JsonProperty("funnel_name")
    private String funnelName;

    @JsonProperty("funnel_stage")
    private String funnelStage;

    @JsonProperty("opportunity_title")
    private String opportunityTitle;

    @JsonProperty("cf_custom_field_api_identifier")
    private String apiIdentification;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @JsonProperty("traffic_source")
    private String trafficSource;

    @JsonProperty("traffic_campaign")
    private String trafficCampaign;

    @JsonProperty("traffic_value")
    private String trafficValue;

    @JsonProperty("available_for_mailing")
    private Boolean available_for_mailing;

    private String[] tags;

    @JsonProperty("legal_bases")
    private RDLegalBases[] legal_bases;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public RDLegalBases[] getLegal_bases() {
        return legal_bases;
    }

    public void setLegal_bases(RDLegalBases[] legal_bases) {
        this.legal_bases = legal_bases;
    }

    public String getConversionIdentifier() {
        return conversionIdentifier;
    }

    public void setConversionIdentifier(String conversionIdentifier) {
        this.conversionIdentifier = conversionIdentifier;
    }

    public String getFunnelName() {
        return funnelName;
    }

    public void setFunnelName(String funnelName) {
        this.funnelName = funnelName;
    }

    public String getFunnelStage() {
        return funnelStage;
    }

    public void setFunnelStage(String funnelStage) {
        this.funnelStage = funnelStage;
    }

    public String getOpportunityTitle() {
        return opportunityTitle;
    }

    public void setOpportunityTitle(String opportunityTitle) {
        this.opportunityTitle = opportunityTitle;
    }

    public String getApiIdentification() {
        return apiIdentification;
    }

    public void setApiIdentification(String apiIdentification) {
        this.apiIdentification = apiIdentification;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTrafficSource() {
        return trafficSource;
    }

    public void setTrafficSource(String trafficSource) {
        this.trafficSource = trafficSource;
    }

    public String getTrafficCampaign() {
        return trafficCampaign;
    }

    public void setTrafficCampaign(String trafficCampaign) {
        this.trafficCampaign = trafficCampaign;
    }

    public String getTrafficValue() {
        return trafficValue;
    }

    public void setTrafficValue(String trafficValue) {
        this.trafficValue = trafficValue;
    }

    public Boolean getAvailable_for_mailing() {
        return available_for_mailing;
    }

    public void setAvailable_for_mailing(Boolean available_for_mailing) {
        this.available_for_mailing = available_for_mailing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RDContactDTO)) return false;

        RDContactDTO that = (RDContactDTO) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        if (getJobTitle() != null ? !getJobTitle().equals(that.getJobTitle()) : that.getJobTitle() != null) return false;
        if (
            getPersonalPhone() != null ? !getPersonalPhone().equals(that.getPersonalPhone()) : that.getPersonalPhone() != null
        ) return false;
        if (getMobile_phone() != null ? !getMobile_phone().equals(that.getMobile_phone()) : that.getMobile_phone() != null) return false;
        if (getCity() != null ? !getCity().equals(that.getCity()) : that.getCity() != null) return false;
        if (getState() != null ? !getState().equals(that.getState()) : that.getState() != null) return false;
        if (getCountry() != null ? !getCountry().equals(that.getCountry()) : that.getCountry() != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getTags(), that.getTags())) return false;
        return getLegal_bases() != null ? getLegal_bases().equals(that.getLegal_bases()) : that.getLegal_bases() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getJobTitle() != null ? getJobTitle().hashCode() : 0);
        result = 31 * result + (getPersonalPhone() != null ? getPersonalPhone().hashCode() : 0);
        result = 31 * result + (getMobile_phone() != null ? getMobile_phone().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getTags());
        result = 31 * result + (getLegal_bases() != null ? getLegal_bases().hashCode() : 0);
        return result;
    }
}
