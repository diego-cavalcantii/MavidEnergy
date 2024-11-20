package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.usecases.interfaces.CalcularDistanciaLatELong;
import org.springframework.stereotype.Service;

@Service
public class CalcularDistanciaLatELongImpl implements CalcularDistanciaLatELong {
    @Override
    public Double calcularDistancia(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        final int EARTH_RADIUS = 6371; // Raio da Terra em km

        double latDistance = Math.toRadians(latitude2 - latitude1);
        double lonDistance = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // Retorna a distância em km
    }
}
