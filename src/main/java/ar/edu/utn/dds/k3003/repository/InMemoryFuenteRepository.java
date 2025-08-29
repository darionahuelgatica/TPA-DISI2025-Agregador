package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.app.Fuente;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryFuenteRepository implements FuenteRepository {

    private List<Fuente> fuentes;

    public InMemoryFuenteRepository() {
        this.fuentes = new ArrayList<>();
    }

    @Override
    public Optional<Fuente> findById(String id) {
        return this.fuentes.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public List<Fuente> findAll() {
        return this.fuentes;
    }

    @Override
    public Fuente save(Fuente col) {
        this.fuentes.add(col);
        return col;
    }
}
