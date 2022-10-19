package com.infocargas.freteapp.service.dto.places;

import java.util.List;
import java.util.Map;

public class GooglePredictionsDTO {
    private String description;
    private List<Map<String, Object>> matched_substrings;

    private GoogleStructuredFormatting structured_formatting;
    public String place_id;

    public String reference;
    public List<String> types;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Map<String, Object>> getMatched_substrings() {
        return matched_substrings;
    }

    public void setMatched_substrings(List<Map<String, Object>> matched_substrings) {
        this.matched_substrings = matched_substrings;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public GoogleStructuredFormatting getStructured_formatting() {
        return structured_formatting;
    }

    public void setStructured_formatting(GoogleStructuredFormatting structured_formatting) {
        this.structured_formatting = structured_formatting;
    }
}
