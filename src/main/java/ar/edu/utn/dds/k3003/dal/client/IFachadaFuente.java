package ar.edu.utn.dds.k3003.dal.client;

import ar.edu.utn.dds.k3003.dto.HechoDTO;

import java.util.List;
import java.util.NoSuchElementException;

public interface IFachadaFuente {
    List<HechoDTO> getHechos(String coleccionId) throws NoSuchElementException;
}