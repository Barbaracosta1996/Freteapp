package com.infocargas.freteapp.service.dto.routes;

import com.infocargas.freteapp.domain.enumeration.GoogleErrors;

import java.util.List;

public class GoogleDirections {
    private List<GeocodedWaypoints> geocoded_waypoints;
    private List<GoogleRoutes> routes;
    private String status;

    private GoogleErrors error;

    private Integer statusCode;

    public List<GeocodedWaypoints> getGeocoded_waypoints() {
        return geocoded_waypoints;
    }

    public void setGeocoded_waypoints(List<GeocodedWaypoints> geocoded_waypoints) {
        this.geocoded_waypoints = geocoded_waypoints;
    }

    public List<GoogleRoutes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<GoogleRoutes> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
