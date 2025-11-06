package ar.edu.utn.dds.k3003.web.bll.services;

import ar.edu.utn.dds.k3003.web.bll.consenso.ConsensoEnum;
import ar.edu.utn.dds.k3003.web.dto.FuenteDTO;
import ar.edu.utn.dds.k3003.web.dto.HechoDTO;

import java.util.List;
import java.util.NoSuchElementException;

public interface IColeccionService {
    List<HechoDTO> listarHechos(String coleccionId, List<FuenteDTO> fuentes) throws NoSuchElementException;
    void setConsenso(String coleccionId, ConsensoEnum consenso);
}
