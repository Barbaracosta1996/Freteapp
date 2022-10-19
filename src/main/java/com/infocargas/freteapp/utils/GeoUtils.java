package com.infocargas.freteapp.utils;

import com.infocargas.freteapp.service.dto.places.PlaceLocation;

public class GeoUtils {

    public static int EARTH_RADIUS_KM = 6371;

    /**
     * Distância entre dois pontos geográficos. Os valores devem ser informados
     * em graus.
     *
     * @param firstLatitude
     *            Latitude do primeiro ponto
     * @param firstLongitude
     *            Longitude do primeiro ponto
     * @param secondLatitude
     *            Latitude do segundo ponto
     * @param secondLongitude
     *            Longitude do segundo ponto
     *
     * @return Distância em quilometros entre os dois pontos
     */
    public static double geoDistanceInKm(double firstLatitude,
                                         double firstLongitude, double secondLatitude, double secondLongitude) {

        // Conversão de graus para radianos das latitudes
        double firstLatToRad = Math.toRadians(firstLatitude);
        double secondLatToRad = Math.toRadians(secondLatitude);

        // Diferença das longitudes
        double deltaLongitudeInRad = Math.toRadians(secondLongitude
            - firstLongitude);

        // Cálcula da distância entre os pontos
        return Math.acos(Math.cos(firstLatToRad) * Math.cos(secondLatToRad)
            * Math.cos(deltaLongitudeInRad) + Math.sin(firstLatToRad)
            * Math.sin(secondLatToRad))
            * EARTH_RADIUS_KM;
    }

    /**
     * Distância entre dois pontos geográficos.
     *
     * @param first
     *            Primeira coordenada geográfica
     * @param second
     *            Segunda coordenada geográfica
     * @return Distância em quilometros entre os dois pontos
     */
    public static double geoDistanceInKm(PlaceLocation first,
                                         PlaceLocation second) {
        return geoDistanceInKm(first.getLat(), first.getLng(),
            second.getLat(), second.getLng());
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
}
