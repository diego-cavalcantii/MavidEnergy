package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.gateways.exceptions.CidadeNotFoundException;
import br.com.mavidenergy.gateways.exceptions.EnderecoNotFoundException;
import br.com.mavidenergy.gateways.exceptions.FornecedorNotFoundException;
import br.com.mavidenergy.gateways.exceptions.PessoaNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

}
