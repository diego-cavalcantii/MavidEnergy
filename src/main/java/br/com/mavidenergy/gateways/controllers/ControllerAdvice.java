package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.gateways.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(EnderecoNotFoundException.class)
    public ResponseEntity<String> trataEnderecoNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CidadeNotFoundException.class)
    public ResponseEntity<String> trataCidadeNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FornecedorNotFoundException.class)
    public ResponseEntity<String> trataFornecedorNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PessoaNotFoundException.class)
    public ResponseEntity<String> trataPessoaNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConsultaNotFoundException.class)
    public ResponseEntity<String> trataConsultaNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro de validação: " + errors);
    }

}
