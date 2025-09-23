package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.bll.consenso.ConsensoEnum;
import ar.edu.utn.dds.k3003.bll.services.IColeccionService;
import ar.edu.utn.dds.k3003.bll.services.IFuenteService;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coleccion")
public class ColeccionController {

    private final IColeccionService coleccionService;
    private final IFuenteService fuenteService;
    private final MeterRegistry meterRegistry;

    @Autowired
    public ColeccionController(IColeccionService coleccionService, IFuenteService fuenteService, MeterRegistry meterRegistry) {
        this.coleccionService = coleccionService;
        this.fuenteService = fuenteService;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/{coleccionId}/hechos")
    public List<HechoDTO> getHechosDeColeccion(@PathVariable String coleccionId) {
        meterRegistry.counter("agregador.hechos.listados").increment();

        var fuentes = fuenteService.listFuentes();
        return coleccionService.listarHechos(coleccionId, fuentes);
    }

    @PatchMapping("/{coleccionId}/consenso/{consenso}")
    public ResponseEntity<Void> cambiarConsenso(@PathVariable String coleccionId, @PathVariable String consenso) {
        meterRegistry.counter("agregador.consenso.actualizado").increment();

        ConsensoEnum consensoEnum = switch (consenso.toLowerCase()) {
            case "todos" -> ConsensoEnum.TODOS;
            case "almenos2" -> ConsensoEnum.AL_MENOS_2;
            default -> throw new IllegalArgumentException("Tipo de consenso no válido");
        };

        if(coleccionId == null || coleccionId.isBlank())
            throw new IllegalArgumentException("Nombre de coleccion invalido");

        coleccionService.setConsenso(coleccionId, consensoEnum);
        return ResponseEntity.ok().build();
    }
}
