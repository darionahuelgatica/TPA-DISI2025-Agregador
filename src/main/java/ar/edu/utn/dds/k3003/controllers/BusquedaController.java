package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.bll.services.HechoService;
import ar.edu.utn.dds.k3003.dto.HechoDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class BusquedaController {

    private final MeterRegistry meterRegistry;
    private final HechoService service;

    @Autowired
    public BusquedaController(MeterRegistry meterRegistry, HechoService service) {
        this.meterRegistry = meterRegistry;
        this.service = service;
    }

    @GetMapping("/search")
    public Page<HechoDTO> search(
            @RequestParam(name="text", defaultValue="") String text,
            @RequestParam(name="tags", defaultValue="") String tags,
            @RequestParam(name="page", defaultValue="0") int page,
            @RequestParam(name="size", defaultValue="20") int size) {
        meterRegistry.counter("agregador.busquedaFullText").increment();
        return service.search(text, tags, page, size);
    }
}