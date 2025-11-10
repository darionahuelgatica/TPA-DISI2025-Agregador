package ar.edu.utn.dds.k3003.dal.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Hecho {

    private String _id;
    private String hechoId;
    private String fuenteId;
    private String nombreColeccion;
    private String descripcion;
    private String titulo;
    private List<String> etiquetas;
    private LocalDateTime occurredAt;
}
