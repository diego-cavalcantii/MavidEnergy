package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.usecases.interfaces.ConverterTimesTampParaHora;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class ConveterTimesTampParaHoraImpl implements ConverterTimesTampParaHora {
    @Override
    public String converter(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(-3)); // Offset UTC-3 para o Brasil
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }
}
