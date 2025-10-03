package ar.edu.utn.dds.k3003.dal.client;

import ar.edu.utn.dds.k3003.dto.SolicitudDTO;
import java.util.List;

public interface IFachadaSolicitudes {
    List<SolicitudDTO> listByHechoId(String hechoId);
}