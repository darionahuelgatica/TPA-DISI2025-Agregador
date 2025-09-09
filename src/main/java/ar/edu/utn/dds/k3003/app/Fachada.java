package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.client.FuenteProxyFactory;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class Fachada implements FachadaAgregador {

    private final FuenteRepository fuenteRepository;
    private final FuenteProxyFactory fuenteProxyFactory;
    private Consenso consenso;

    @Autowired
    public Fachada(FuenteRepository fuenteRepository, FuenteProxyFactory fuenteProxyFactory) {
        this.fuenteRepository = fuenteRepository;
        this.fuenteProxyFactory = fuenteProxyFactory;
        this.consenso = new Todos();
    }

    @Override
    public FuenteDTO agregar(FuenteDTO fuenteDTO) {
        String id = fuenteDTO.id() == null || fuenteDTO.id().isBlank()
            ? UUID.randomUUID().toString()
            : fuenteDTO.id();

        if (!Pattern.matches("^(https?://).+", fuenteDTO.endpoint())) {
            throw new IllegalArgumentException("El endpoint no es una URL válida");
        }

        Fuente fuente = new Fuente(id, fuenteDTO.nombre(), fuenteDTO.endpoint());
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
    public List<HechoDTO> hechos(String coleccion) throws NoSuchElementException {
        List<Fuente> fuentes = this.fuenteRepository.findAll();

        List<List<HechoDTO>> hechosPorFuente = fuentes.stream()
                .map(fuente -> this.fuenteProxyFactory.getProxy(fuente.getEndpoint()))
                .map(fuenteProxy -> fuenteProxy.buscarHechosXColeccion(coleccion))
                .toList();

        return this.consenso.obtenerHechos(hechosPorFuente);
    }

    @Override
    public void addFachadaFuentes(String idFuente, FachadaFuente fachadaFuente) {}

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