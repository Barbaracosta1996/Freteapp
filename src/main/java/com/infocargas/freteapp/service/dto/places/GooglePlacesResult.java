package com.infocargas.freteapp.service.dto.places;

import java.util.List;

public class GooglePlacesResult {

    private List<GooglePredictionsDTO> predictions;

    private List<Object> html_attributions;
    private List<GooglePlacesDTO> results;

    public List<Object> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<Object> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public List<GooglePlacesDTO> getResults() {
        return results;
    }

    public void setResults(List<GooglePlacesDTO> results) {
        this.results = results;
    }

    public List<GooglePredictionsDTO> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<GooglePredictionsDTO> predictions) {
        this.predictions = predictions;
    }
}
