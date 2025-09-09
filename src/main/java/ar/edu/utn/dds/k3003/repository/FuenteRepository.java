package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Fuente;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuenteRepository {

    Optional<Fuente> findById(String id);
    List<Fuente> findAll();
    Fuente save(Fuente col);
}