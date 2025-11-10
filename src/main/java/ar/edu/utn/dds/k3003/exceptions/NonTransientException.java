package ar.edu.utn.dds.k3003.exceptions;

public class NonTransientException extends RuntimeException {
    public NonTransientException(String message) {
        super(message);
    }
}
