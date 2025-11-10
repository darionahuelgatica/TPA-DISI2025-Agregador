package ar.edu.utn.dds.k3003.dal.model;

import ar.edu.utn.dds.k3003.bll.consenso.ConsensoEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "Coleccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coleccion {

    public Coleccion(String nombre) {
        this.nombre = nombre;
        consenso = ConsensoEnum.TODOS;
        fechaModificacion = LocalDateTime.now();
    }

    @Id
    private String nombre;
    private LocalDateTime fechaModificacion;
    private ConsensoEnum consenso;
}
