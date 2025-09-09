package ar.edu.utn.dds.k3003.consenso;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import java.util.*;
import java.util.stream.Collectors;

public class AlMenosDos implements Consenso {
    private static final int REPETICIONES = 2;

    public List<HechoDTO> obtenerHechos(List<List<HechoDTO>> hechosPorFuente) {
        return hechosPorFuente.stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        HechoDTO::id,
                        Collectors.toList()
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= REPETICIONES)
                .map(entry -> entry.getValue().getFirst())
                .toList();
    }
}
