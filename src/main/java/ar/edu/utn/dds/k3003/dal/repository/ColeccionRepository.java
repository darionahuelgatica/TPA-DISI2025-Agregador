package ar.edu.utn.dds.k3003.dal.repository;

import ar.edu.utn.dds.k3003.dal.model.Coleccion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColeccionRepository {
    Optional<Coleccion> findById(String id);
    List<Coleccion> findAll();
    Coleccion save(Coleccion col);
}