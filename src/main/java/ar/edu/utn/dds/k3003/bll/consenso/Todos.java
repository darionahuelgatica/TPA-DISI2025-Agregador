package ar.edu.utn.dds.k3003.bll.consenso;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import java.util.List;
import java.util.stream.Collectors;

public class Todos implements Consenso {

    @Override
    public List<HechoDTO> obtenerHechos(List<List<HechoDTO>> hechosPorFuente) {
        if (hechosPorFuente.isEmpty()) return List.of();

        return hechosPorFuente.stream()
                .flatMap(List::stream)
                .collect(Collectors.toMap(
                        HechoDTO::id,
                        hecho -> hecho,
                        (h1, h2) -> h1
                ))
                .values()
                .stream()
                .toList();
    }
}