package ar.edu.utn.dds.k3003.web.dal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Fuente")
@Data
@NoArgsConstructor
public class FuenteDeHechos {

    @Id
    private String id;
    private String nombre;
    private String endpoint;

    public FuenteDeHechos(String id, String nombre, String endpoint) {
        this.id = id;
        this.nombre = nombre;
        this.endpoint = endpoint;
    }
}