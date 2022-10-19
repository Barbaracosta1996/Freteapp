package com.infocargas.freteapp.service.dto.routes;

import com.infocargas.freteapp.service.dto.places.PlaceLocation;

public class GoogleSteps {
    private GoogleMsgReturn distance;
    private GoogleMsgReturn duration;
    private PlaceLocation end_location;
    private String html_instructions;
    private GooglePolyline polyline;
    private PlaceLocation start_location;
    private String travel_mode;

    private String maneuver;

    public GoogleMsgReturn getDistance() {
        return distance;
    }

    public void setDistance(GoogleMsgReturn distance) {
        this.distance = distance;
    }

    public GoogleMsgReturn getDuration() {
        return duration;
    }

    public void setDuration(GoogleMsgReturn duration) {
        this.duration = duration;
    }

    public PlaceLocation getEnd_location() {
        return end_location;
    }

    public void setEnd_location(PlaceLocation end_location) {
        this.end_location = end_location;
    }

    public String getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }

    public GooglePolyline getPolyline() {
        return polyline;
    }

    public void setPolyline(GooglePolyline polyline) {
        this.polyline = polyline;
    }

    public PlaceLocation getStart_location() {
        return start_location;
    }

    public void setStart_location(PlaceLocation start_location) {
        this.start_location = start_location;
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }
}
