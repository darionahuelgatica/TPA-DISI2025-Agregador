package ar.edu.utn.dds.k3003.web.bll.consenso;

import ar.edu.utn.dds.k3003.web.dal.client.IFachadaSolicitudes;
import ar.edu.utn.dds.k3003.web.dto.HechoDTO;
import java.util.List;
import java.util.stream.Collectors;

public class Estricto implements Consenso {

    private final IFachadaSolicitudes solicitudes;

    public Estricto(IFachadaSolicitudes solicitudes) {
        this.solicitudes = solicitudes;
    }

    @Override
    public List<HechoDTO> obtenerHechos(List<List<HechoDTO>> hechosPorFuente) {
        return hechosPorFuente.stream()
            .flatMap(List::stream)
            .collect(Collectors.toMap(
                    HechoDTO::getId,
                    hecho -> hecho,
                    (h1, h2) -> h1
            ))
            .values()
            .stream()
            .filter(h -> solicitudes.listByHechoId(h.getId()).isEmpty())
            .toList();
    }
}
