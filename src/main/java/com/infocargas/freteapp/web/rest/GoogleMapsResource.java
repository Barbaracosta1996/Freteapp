package com.infocargas.freteapp.web.rest;

import com.infocargas.freteapp.service.GooglePlacesServices;
import com.infocargas.freteapp.service.dto.places.GooglePlacesDTO;
import com.infocargas.freteapp.service.dto.places.GooglePredictionsDTO;
import com.infocargas.freteapp.service.dto.places.PlacesSearchDTO;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GoogleMapsResource {
    private final GooglePlacesServices googlePlacesServices;

    public GoogleMapsResource(GooglePlacesServices googlePlacesServices) {
        this.googlePlacesServices = googlePlacesServices;
    }

    @PostMapping("/google-places")
    public ResponseEntity<List<GooglePlacesDTO>> getSearchPlace(@RequestBody PlacesSearchDTO placesSearchDTO) throws UnirestException {
        List<GooglePlacesDTO> list = googlePlacesServices.findPlacesApiWithParams(placesSearchDTO);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/google-places/findText/{text}")
    public ResponseEntity<List<GooglePredictionsDTO>> getSearchPlaceToComplete(@PathVariable String text) throws UnirestException {
        List<GooglePredictionsDTO> list = googlePlacesServices.autoCompleteText(text);
        return ResponseEntity.ok().body(list);
    }

}
