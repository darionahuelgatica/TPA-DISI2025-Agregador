package ar.edu.utn.dds.k3003.dal.repository;

import ar.edu.utn.dds.k3003.dal.model.FuenteDeHechos;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuenteRepository {
    Optional<FuenteDeHechos> findById(String id);
    List<FuenteDeHechos> findAll();
    FuenteDeHechos save(FuenteDeHechos fuente);
    void deleteById(String id);
    void deleteAll();
}