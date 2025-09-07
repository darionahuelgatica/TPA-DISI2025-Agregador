package ar.edu.utn.dds.k3003.consenso;

import com.fasterxml.jackson.annotation.JsonProperty; // Importar para snake_case
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsensoRequest {

    @JsonProperty("tipo") // Mapea el campo JSON 'tipo' a esta propiedad
    private String tipo;

    @JsonProperty("coleccion")
    private String Coleccion;

}