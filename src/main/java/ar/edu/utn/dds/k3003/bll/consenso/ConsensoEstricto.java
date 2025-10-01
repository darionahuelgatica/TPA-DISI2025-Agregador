package ar.edu.utn.dds.k3003.bll.consenso;

import ar.edu.utn.dds.k3003.dal.client.IFachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import java.util.List;
import java.util.stream.Collectors;

public class ConsensoEstricto implements Consenso {

    private final IFachadaSolicitudes solicitudes;

    public ConsensoEstricto(IFachadaSolicitudes solicitudes) {
        this.solicitudes = solicitudes;
    }

    @Override
    public List<HechoDTO> obtenerHechos(List<List<HechoDTO>> hechosPorFuente) {
        return hechosPorFuente.stream()
                .flatMap(List::stream)
                .filter(h -> !solicitudes.listByHechoId(h.id()).isEmpty())
                .collect(Collectors.toList());
    }
}
