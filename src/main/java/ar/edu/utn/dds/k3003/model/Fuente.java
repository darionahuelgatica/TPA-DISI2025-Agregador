package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Fuente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fuente {

    @Id
    private String id;
    private String nombre;
    private String endpoint;
    private boolean activo;

    @Transient
    private List<PdI> pdis = new ArrayList<>();
    @Transient
    private List<Coleccion> colecciones = new ArrayList<>();

    public Fuente (String id, String nombre, String endpoint) {
        this.id = id;
        this.nombre = nombre;
        this.endpoint = endpoint;
        this.activo = true;
    }

    public List<Hecho> obtenerTodosLosHechos() {
        return this.colecciones.stream().flatMap(x -> x.getHechos().stream()).collect(Collectors.toList());
    }

    public List<Coleccion> getColecciones() {return this.colecciones;}

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
