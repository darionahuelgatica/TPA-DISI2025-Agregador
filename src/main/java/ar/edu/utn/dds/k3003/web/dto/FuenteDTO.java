package ar.edu.utn.dds.k3003.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FuenteDTO {
    private String id;
    private String nombre;
    private String endpoint;
}
