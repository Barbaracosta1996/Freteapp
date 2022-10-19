package com.infocargas.freteapp.service.dto.routes;

import java.util.List;

public class GoogleRoutes {
    private GoogleBounds bounds;
    private String copyrights;
    private List<GoogleLegs> legs;
    private GooglePolyline overview_polyline;
    private String summary;
    private List<String> warnings;
    private List<Integer> waypoint_order;

    public GoogleBounds getBounds() {
        return bounds;
    }

    public void setBounds(GoogleBounds bounds) {
        this.bounds = bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public List<GoogleLegs> getLegs() {
        return legs;
    }

    public void setLegs(List<GoogleLegs> legs) {
        this.legs = legs;
    }

    public GooglePolyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(GooglePolyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<Integer> getWaypoint_order() {
        return waypoint_order;
    }

    public void setWaypoint_order(List<Integer> waypoint_order) {
        this.waypoint_order = waypoint_order;
    }

}
