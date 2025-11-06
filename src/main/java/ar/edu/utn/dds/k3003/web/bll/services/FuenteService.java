package ar.edu.utn.dds.k3003.web.bll.services;

import ar.edu.utn.dds.k3003.web.dal.model.FuenteDeHechos;
import ar.edu.utn.dds.k3003.web.dal.repository.FuenteRepository;
import ar.edu.utn.dds.k3003.web.dto.FuenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FuenteService implements IFuenteService {

    private FuenteRepository fuenteRepository;

    @Autowired
    public FuenteService(FuenteRepository fuenteRepository) {
        this.fuenteRepository = fuenteRepository;
    }

    @Override
    public FuenteDTO addFuente(FuenteDTO fuenteDTO) {
        if(fuenteDTO.getId() == null || fuenteDTO.getId().isBlank())
            throw new IllegalArgumentException("Debe especificar un identificador de Fuente");

        if (!Pattern.matches("^(https?://).+", fuenteDTO.getEndpoint())) {
            throw new IllegalArgumentException("El endpoint no es una URL válida");
        }

        FuenteDeHechos fuente = new FuenteDeHechos(fuenteDTO.getId(), fuenteDTO.getNombre(), fuenteDTO.getEndpoint());
        fuenteRepository.save(fuente);
        return new FuenteDTO(fuenteDTO.getId(), fuenteDTO.getNombre(), fuenteDTO.getEndpoint());
    }

    @Override
    public List<FuenteDTO> listFuentes() {
        return this.fuenteRepository.findAll()
                .stream()
                .map(x -> new FuenteDTO(x.getId(), x.getNombre(), x.getEndpoint()))
                .collect(Collectors.toList());
    }

    @Override
    public FuenteDTO getFuenteById(String fuenteId) throws NoSuchElementException {
        var fuenteDb = this.fuenteRepository.findById(fuenteId);

        if(fuenteDb.isEmpty())
            throw new NoSuchElementException(fuenteId + " no existe");

        var fuente = fuenteDb.get();
        return new FuenteDTO(fuente.getId(), fuente.getNombre(), fuente.getEndpoint());
    }

    @Override
    public void borrarFuente(String fuenteId){
        if(fuenteRepository.findById(fuenteId).isEmpty())
            throw new NoSuchElementException(fuenteId + " no existe");

        fuenteRepository.deleteById(fuenteId);
    }

    @Override
    public void borrarTodasLasFuentes(){
        this.fuenteRepository.deleteAll();
    }
}