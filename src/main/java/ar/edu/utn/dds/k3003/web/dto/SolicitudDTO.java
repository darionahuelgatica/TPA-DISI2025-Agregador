package ar.edu.utn.dds.k3003.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolicitudDTO {
    private String id;
    private String descripcion;
    private String estado;
    private String hechoId;
}