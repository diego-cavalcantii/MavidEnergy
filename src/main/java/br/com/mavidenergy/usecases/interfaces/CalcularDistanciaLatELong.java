package br.com.mavidenergy.usecases.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface CalcularDistanciaLatELong {
    Double calcularDistancia(Double latitude1, Double longitude1, Double latitude2, Double longitude2);
}
