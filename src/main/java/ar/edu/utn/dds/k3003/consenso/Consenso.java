package ar.edu.utn.dds.k3003.consenso;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import java.util.List;

public interface Consenso {
    List<HechoDTO> obtenerHechos(List<List<HechoDTO>> hechosPorFuente);
}
