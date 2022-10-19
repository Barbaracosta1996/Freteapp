package com.infocargas.freteapp.service.dto.places;

import java.util.List;

public class GooglePlacesDTO {

    private String place_id;
    private String business_status;
    private String formatted_address;
    private String name;
    private List<String> types;
    private PlaceGeometry geometry;
    private PlaceOpenNow opening_hours;
    private List<PlacePhotos> photos;

    private String vicinity;

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(String business_status) {
        this.business_status = business_status;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public PlaceGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(PlaceGeometry geometry) {
        this.geometry = geometry;
    }

    public PlaceOpenNow getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(PlaceOpenNow opening_hours) {
        this.opening_hours = opening_hours;
    }

    public List<PlacePhotos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PlacePhotos> photos) {
        this.photos = photos;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.formatted_address = vicinity;
        this.vicinity = vicinity;
    }
}
