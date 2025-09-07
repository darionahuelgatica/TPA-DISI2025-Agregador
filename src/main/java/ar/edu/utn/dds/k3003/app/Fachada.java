package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.consenso.AlMenosDos;
import ar.edu.utn.dds.k3003.consenso.Consenso;
import ar.edu.utn.dds.k3003.consenso.Todos;
import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Fachada implements FachadaAgregador {

    private final FuenteRepository fuenteRepository;
    private final Map<String, FachadaFuente> fachadasExternas = new HashMap<>();
    private Consenso consenso;

    @Autowired
    public Fachada(FuenteRepository fuenteRepository) {
        this.fuenteRepository = fuenteRepository;
        this.consenso = new Todos();
    }

//    //@Autowired
//    public Fachada (/*FachadaFuente fachadaFuente*/) {
//        this.fuenteRepository = new InMemoryFuenteRepository();
//        this.consenso = new Todos();
//        //this.fachadaFuente = fachadaFuente;
//    }

    @Override
    public FuenteDTO agregar(FuenteDTO fuenteDTO) {
        String id = fuenteDTO.id() == null || fuenteDTO.id().isBlank()
            ? UUID.randomUUID().toString()
            : fuenteDTO.id();
        Fuente fuente = new Fuente(id, fuenteDTO.nombre(), fuenteDTO.endpoint());
        //fuente.setFachadaExterna(this.fachadaFuente);
        fuenteRepository.save(fuente);
        return new FuenteDTO(id, fuenteDTO.nombre(), fuenteDTO.endpoint());
    }

    @Override
    public List<FuenteDTO> fuentes() {
        return this.fuenteRepository.findAll()
                .stream()
                .map(x -> new FuenteDTO(x.getId(), x.getNombre(), x.getEndpoint()))
                .collect(Collectors.toList());
    }

    @Override
    public FuenteDTO buscarFuenteXId(String s) throws NoSuchElementException {
        return this.fuenteRepository.findById(s).map(f -> new FuenteDTO(f.getId(), f.getNombre(), f.getEndpoint())).orElseThrow( () -> new NoSuchElementException(s + " no existe"));
    }

    @Override
    public List<HechoDTO> hechos(String s) throws NoSuchElementException {
        List<Fuente> fuentes = this.fuenteRepository.findAll();
        //List<List<HechoDTO>> hechosPorFuente = fuentes.stream().map(f -> f.getFachadaExterna().buscarHechosXColeccion(s)).toList();
        fuentes.forEach(fuente -> {
            FachadaFuente fachadaAsociada = this.fachadasExternas.get(fuente.getId());
            fuente.setFachadaExterna(fachadaAsociada);
        });

        List<List<HechoDTO>> hechosPorFuente = fuentes.stream()
                .map(Fuente::getFachadaExterna)
                .filter(Objects::nonNull)
                .map(fachada -> fachada.buscarHechosXColeccion(s))
                .toList();
        return this.consenso.obtenerHechos(hechosPorFuente);
    }

    @Override
    public void addFachadaFuentes(String idFuente, FachadaFuente fachadaFuente) {
        fuenteRepository.findById(idFuente)
                .ifPresentOrElse(
                        f -> this.fachadasExternas.put(idFuente, fachadaFuente),
                        () -> { throw new NoSuchElementException("No se encontró la fuente con id: " + idFuente); }
                );
    }

    @Override
    public void setConsensoStrategy(ConsensosEnum consensoEnum, String fuenteId) {
        setConsenso(consensoEnum);
    }

    public void setConsenso(ConsensosEnum consensoEnum) {
        this.consenso = castearConsenso(consensoEnum);
    }

    private Consenso castearConsenso(ConsensosEnum consensoEnum) {
        return switch (consensoEnum) {
            case TODOS -> new Todos();
            case AL_MENOS_2 -> new AlMenosDos();
            default -> throw new IllegalArgumentException("ConsensoEnum no reconocido: " + consensoEnum);
        };
    }
}