package br.com.mavidenergy.usecases.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface ConverterTimesTampParaHora {
    String converter(long timestamp);
}
