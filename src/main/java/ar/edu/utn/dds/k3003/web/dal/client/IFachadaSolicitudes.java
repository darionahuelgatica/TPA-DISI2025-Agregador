package ar.edu.utn.dds.k3003.web.dal.client;

import ar.edu.utn.dds.k3003.web.dto.SolicitudDTO;
import java.util.List;

public interface IFachadaSolicitudes {
    List<SolicitudDTO> listByHechoId(String hechoId);
}