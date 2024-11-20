package br.com.mavidenergy.gateways.exceptions;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }
}
