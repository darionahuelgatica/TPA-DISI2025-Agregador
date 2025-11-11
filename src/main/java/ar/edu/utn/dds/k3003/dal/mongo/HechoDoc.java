package ar.edu.utn.dds.k3003.dal.mongo;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "HECHOS")
public class HechoDoc {

    @Id
    private String id;

    private String fuenteId;
    private String hechoId;

    @Field("nombreColeccion")
    private String nombreColeccion;

    private String titulo;

    private List<String> etiquetas;

    private boolean eliminado;

    private Instant createdAt;
    private Instant updatedAt;

    @TextScore
    @ReadOnlyProperty
    private Float score;
}