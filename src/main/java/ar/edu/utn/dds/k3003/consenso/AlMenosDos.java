package ar.edu.utn.dds.k3003.consenso;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;

import java.util.*;
import java.util.stream.Collectors;

public class AlMenosDos implements Consenso {

    private static final int UMBRAL = 2;

    @Override
    public List<HechoDTO> obtenerHechos(List<List<HechoDTO>> hechosPorFuente) {
        Map<String, Long> counts = new HashMap<>();

        for (List<HechoDTO> lista : hechosPorFuente) {
            Set<String> vistos = lista.stream()
                    .map(h -> h.titulo().toLowerCase())
                    .collect(Collectors.toSet());

            for (String tituloLower : vistos) {
                counts.put(tituloLower, counts.getOrDefault(tituloLower, 0L) + 1);
            }
        }

        Set<String> titulosQueCumplen = counts.entrySet().stream()
                .filter(e -> e.getValue() >= UMBRAL)
                .map(Map.Entry::getKey)        // queda el set de titles en lowercase
                .collect(Collectors.toSet());

        List<HechoDTO> resultado = new ArrayList<>();
        for (List<HechoDTO> lista : hechosPorFuente) {
            for (HechoDTO h : lista) {
                String key = h.titulo().toLowerCase();
                if (titulosQueCumplen.contains(key)) {
                    resultado.add(h);
                    titulosQueCumplen.remove(key);
                }
            }
            if (titulosQueCumplen.isEmpty()) break;
        }

        return resultado;
    }
}
