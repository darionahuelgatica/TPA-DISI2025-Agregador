package ar.edu.utn.dds.k3003.dal.client;

import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import java.util.List;

public interface IFachadaSolicitudes {
    List<SolicitudDTO> listByHechoId(String hechoId);
}