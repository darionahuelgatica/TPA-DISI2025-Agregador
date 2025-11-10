package ar.edu.utn.dds.k3003.bll.handlers;

import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CannotGetJdbcConnectionException.class,
            CannotCreateTransactionException.class,
            DataAccessResourceFailureException.class,
            QueryTimeoutException.class,
            NonTransientDataAccessResourceException.class,
            DataAccessException.class,
            PersistenceException.class
    })
    public ResponseEntity<String> handleDbConnectionErrors(Exception ex, HttpServletRequest request) {
        log.error("Error de acceso a BD en {} {}", request.getMethod(), request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al conectar con la base de datos.");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(Exception ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFound(Exception ex, HttpServletRequest request) {
        log.error("Error de recurso no encontrado {} {}", request.getMethod(), request.getRequestURI(), ex);
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(String.format("Recurso no encontrado: { %s %s }", request.getMethod(), request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyUnhandled(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {} {}", request.getMethod(), request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error no controlado. Por favor, reintente mas tarde o contacte a soporte.");
    }
}
