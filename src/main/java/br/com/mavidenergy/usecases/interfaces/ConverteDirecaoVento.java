package br.com.mavidenergy.usecases.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface ConverteDirecaoVento {
    String converter(int graus);
}
