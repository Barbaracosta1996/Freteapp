package com.infocargas.freteapp.service.dto.routes;

import com.infocargas.freteapp.service.dto.places.PlaceLocation;

public class GoogleBounds {
    private PlaceLocation northeast;
    private PlaceLocation southwest;

    public PlaceLocation getNortheast() {
        return northeast;
    }

    public void setNortheast(PlaceLocation northeast) {
        this.northeast = northeast;
    }

    public PlaceLocation getSouthwest() {
        return southwest;
    }

    public void setSouthwest(PlaceLocation southwest) {
        this.southwest = southwest;
    }
}
