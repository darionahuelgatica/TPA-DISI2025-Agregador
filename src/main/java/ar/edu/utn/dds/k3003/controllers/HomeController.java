package ar.edu.utn.dds.k3003.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "TP DDSI 2025 - Agregador funcionando 🚀";
    }
}
