package com.infocargas.freteapp.service;

import com.google.gson.Gson;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.places.GooglePlacesDTO;
import com.infocargas.freteapp.service.dto.places.GooglePlacesResult;
import com.infocargas.freteapp.service.dto.places.GooglePredictionsDTO;
import com.infocargas.freteapp.service.dto.places.PlacesSearchDTO;
import com.infocargas.freteapp.service.dto.routes.GoogleDirections;
import com.infocargas.freteapp.web.rest.errors.BadRequestAlertException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GooglePlacesServices {

    private final Gson g = new Gson();

    GooglePlacesServices() {}

    public List<GooglePlacesDTO> findPlacesApiWithParams(PlacesSearchDTO searchDTO) throws UnirestException {
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json";

        Unirest.setTimeouts(0, 0);

        Map<String, Object> query = new HashMap<>();

        if (searchDTO.getLat() != null && searchDTO.getLng() != null) {
            query.put("location", searchDTO.getLat() + "," + searchDTO.getLng());
            query.put("types", searchDTO.getType());
            query.put("radius", "10000");
        }

        query.put("query", searchDTO.getQuery());
        query.put("language", "pt-br");
        query.put("key", "AIzaSyAmLy3h851I75mLOfS0CyxOWYkQan82nnk");

        HttpResponse<String> response = Unirest.get(url).queryString(query).asString();

        GooglePlacesResult googleResult = g.fromJson(response.getBody(), GooglePlacesResult.class);

        if (googleResult == null && googleResult.getResults().isEmpty()) {
            return new ArrayList<>();
        } else {
            return googleResult.getResults();
        }
    }

    public List<GooglePlacesDTO> findPlacesTypesWithParams(PlacesSearchDTO searchDTO)
        throws UnirestException, UnsupportedEncodingException {
        if (searchDTO.getType() == null && searchDTO.getType().length() <= 0) {
            throw new BadRequestAlertException(
                "Não foi encontrado o tipo o estabelecimento, que é obrigatório",
                "GooglePlacesServices",
                "idexists"
            );
        }

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest
            .get(url)
            .queryString("location", searchDTO.getLat() + "," + searchDTO.getLng())
            .queryString("radius", "10000")
            .queryString("types", searchDTO.getType())
            .queryString("language", "pt-br")
            .queryString("key", "AIzaSyAmLy3h851I75mLOfS0CyxOWYkQan82nnk")
            .asString();

        GooglePlacesResult googleResult = g.fromJson(response.getBody(), GooglePlacesResult.class);

        if (googleResult == null && googleResult.getResults().isEmpty()) {
            return new ArrayList<>();
        } else {
            return googleResult.getResults();
        }
    }

    public List<GooglePlacesDTO> findPlacesText(PlacesSearchDTO searchDTO) throws UnirestException, UnsupportedEncodingException {
        if (searchDTO.getQuery() == null && searchDTO.getQuery().length() <= 0) {
            throw new BadRequestAlertException("O campo de pesquisa precisar ser preenchido.", "GooglePlacesServices", "idexists");
        }

        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json";

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest
            .get(url)
            .queryString("query", searchDTO.getQuery())
            .queryString("language", "pt-br")
            .queryString("key", "AIzaSyAmLy3h851I75mLOfS0CyxOWYkQan82nnk")
            .asString();

        GooglePlacesResult googleResult = g.fromJson(response.getBody(), GooglePlacesResult.class);

        if (googleResult == null || googleResult.getResults().isEmpty()) {
            return new ArrayList<>();
        } else {
            return googleResult.getResults();
        }
    }

    public List<GooglePredictionsDTO> autoCompleteText(String text) throws UnirestException {
        if (text == null || text.isEmpty()) {
            throw new BadRequestAlertException("O campo de pesquisa precisar ser preenchido.", "GooglePlacesServices", "idexists");
        }

        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json";

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest
            .get(url)
            .queryString("input", text)
            .queryString("language", "pt-br")
            .queryString("types", "(cities)")
            .queryString("key", "AIzaSyDuthyaF457_q_aXb9HD8at_dMdPqX4fcM")
            .asString();

        if (response.getStatus() == 200) {
            GooglePlacesResult googleResult = g.fromJson(response.getBody(), GooglePlacesResult.class);

            if (googleResult == null) {
                return new ArrayList<>();
            } else {
                return googleResult.getPredictions();
            }
        } else {
            return new ArrayList<>();
        }

    }

    public String getRouteDirections(OfertasDTO ofertasDTO) throws UnirestException {
        if (ofertasDTO == null) {
            throw new BadRequestAlertException("A pesquisa precisa de uma oferta existente", "GooglePlacesServices", "idexists");
        }

        GooglePlacesDTO origem = g.fromJson(ofertasDTO.getLocalizacaoOrigem(), GooglePlacesDTO.class);
        GooglePlacesDTO destino = g.fromJson(ofertasDTO.getLocalizacaoDestino(), GooglePlacesDTO.class);

        String url = "https://maps.googleapis.com/maps/api/directions/json";

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest
            .get(url)
            .queryString("origin", "place_id:"+origem.getPlace_id())
            .queryString("destination", "place_id:"+destino.getPlace_id())
            .queryString("language", "pt-br")
            .queryString("key", "AIzaSyDuthyaF457_q_aXb9HD8at_dMdPqX4fcM")
            .asString();

        if (response.getStatus() == 200) {
            GoogleDirections googleDirections = g.fromJson(response.getBody(), GoogleDirections.class);

            if (googleDirections == null) {
                return "";
            } else {
                if ((googleDirections.getRoutes() == null || googleDirections.getRoutes().isEmpty()))
                    return "null";
                return g.toJson(googleDirections.getRoutes());
            }
        } else {
            return response.getStatusText();
        }
    }
}
