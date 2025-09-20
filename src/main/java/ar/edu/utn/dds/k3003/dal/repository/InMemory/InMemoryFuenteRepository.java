package ar.edu.utn.dds.k3003.dal.repository.InMemory;

import ar.edu.utn.dds.k3003.dal.model.FuenteDeHechos;
import ar.edu.utn.dds.k3003.dal.repository.FuenteRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryFuenteRepository implements FuenteRepository {

    private List<FuenteDeHechos> fuentes;

    public InMemoryFuenteRepository() {
        this.fuentes = new ArrayList<>();
    }

    @Override
    public Optional<FuenteDeHechos> findById(String id) {
        return this.fuentes.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public List<FuenteDeHechos> findAll() {
        return this.fuentes;
    }

    @Override
    public FuenteDeHechos save(FuenteDeHechos col) {
        this.fuentes.add(col);
        return col;
    }

    @Override
    public void deleteById(String id) {
        this.fuentes.removeIf(x -> x.getId().equals(id));
    }

    @Override
    public void deleteAll() {
        this.fuentes.clear();
    }
}
