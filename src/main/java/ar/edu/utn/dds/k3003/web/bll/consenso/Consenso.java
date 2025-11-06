package ar.edu.utn.dds.k3003.web.bll.consenso;

import ar.edu.utn.dds.k3003.web.dto.HechoDTO;
import java.util.List;

public interface Consenso {
    List<HechoDTO> obtenerHechos(List<List<HechoDTO>> hechosPorFuente);
}