package com.infocargas.freteapp.response.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacebookResponsePricing {

    private String category;

    @JsonProperty("pricing_model")
    private String pricingModel;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPricingModel() {
        return pricingModel;
    }

    public void setPricingModel(String pricingModel) {
        this.pricingModel = pricingModel;
    }

    @Override
    public String toString() {
        return "FacebookResponsePricing{" + "category='" + category + '\'' + ", pricingModel='" + pricingModel + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookResponsePricing)) return false;

        FacebookResponsePricing that = (FacebookResponsePricing) o;

        if (getCategory() != null ? !getCategory().equals(that.getCategory()) : that.getCategory() != null) return false;
        return getPricingModel() != null ? getPricingModel().equals(that.getPricingModel()) : that.getPricingModel() == null;
    }

    @Override
    public int hashCode() {
        int result = getCategory() != null ? getCategory().hashCode() : 0;
        result = 31 * result + (getPricingModel() != null ? getPricingModel().hashCode() : 0);
        return result;
    }
}
