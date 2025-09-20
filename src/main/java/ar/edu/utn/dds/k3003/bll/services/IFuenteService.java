package ar.edu.utn.dds.k3003.bll.services;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;

import java.util.List;
import java.util.NoSuchElementException;

public interface IFuenteService {
    FuenteDTO addFuente(FuenteDTO fuente);
    List<FuenteDTO> listFuentes();
    FuenteDTO getFuenteById(String fuenteId) throws NoSuchElementException;
    void borrarFuente(String fuenteId);
    void borrarTodasLasFuentes();
}
