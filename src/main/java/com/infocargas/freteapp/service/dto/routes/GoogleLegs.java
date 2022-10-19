package com.infocargas.freteapp.service.dto.routes;

import com.infocargas.freteapp.service.dto.places.PlaceLocation;

import java.util.List;

public class GoogleLegs {
    private GoogleMsgReturn distance;
    private GoogleMsgReturn duration;
    private String end_address;
    private PlaceLocation end_location;
    private String start_address;
    private PlaceLocation start_location;
    private List<GoogleSteps> steps;

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

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public PlaceLocation getEnd_location() {
        return end_location;
    }

    public void setEnd_location(PlaceLocation end_location) {
        this.end_location = end_location;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public PlaceLocation getStart_location() {
        return start_location;
    }

    public void setStart_location(PlaceLocation start_location) {
        this.start_location = start_location;
    }

    public List<GoogleSteps> getSteps() {
        return steps;
    }

    public void setSteps(List<GoogleSteps> steps) {
        this.steps = steps;
    }
}
