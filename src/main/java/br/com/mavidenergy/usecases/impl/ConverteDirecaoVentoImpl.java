package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.usecases.interfaces.ConverteDirecaoVento;
import org.springframework.stereotype.Service;

@Service
public class ConverteDirecaoVentoImpl implements ConverteDirecaoVento {
    @Override
    public String converter(int graus) {
        String[] direcoes = {"Norte", "Nordeste", "Leste", "Sudeste", "Sul", "Sudoeste", "Oeste", "Noroeste"};
        return direcoes[(int) Math.round(((double) graus % 360) / 45) % 8];
    }
}
