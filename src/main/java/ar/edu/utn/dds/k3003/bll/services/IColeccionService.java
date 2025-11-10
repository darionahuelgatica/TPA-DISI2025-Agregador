package ar.edu.utn.dds.k3003.bll.services;

import ar.edu.utn.dds.k3003.bll.consenso.ConsensoEnum;
import ar.edu.utn.dds.k3003.dto.FuenteDTO;
import ar.edu.utn.dds.k3003.dto.HechoDTO;

import java.util.List;
import java.util.NoSuchElementException;

public interface IColeccionService {
    List<HechoDTO> listarHechos(String coleccionId, List<FuenteDTO> fuentes) throws NoSuchElementException;
    void setConsenso(String coleccionId, ConsensoEnum consenso);
}
