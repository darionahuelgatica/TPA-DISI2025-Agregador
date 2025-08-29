package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Fuentes")
public class Fuente {

    @Id
    @GeneratedValue
    private String id;

    private String nombre;
    private String endpoint;
    @Transient
    private List<PdI> pdis = new ArrayList<>(); // Modelo interno de PdI
    @Transient
    private List<Coleccion> colecciones = new ArrayList<>();
    @Transient
    private FachadaFuente fachadaExterna;

    public Fuente() {}

    public Fuente (String id, String nombre, String endpoint) {
        this.id = id;
        this.nombre = nombre;
        this.endpoint = endpoint;
    }

    public List<Hecho> obtenerTodosLosHechos() {
        return this.colecciones.stream().flatMap(x -> x.getHechos().stream()).collect(Collectors.toList());
    }

    public FachadaFuente getFachadaExterna() {return this.fachadaExterna;}

    public void setFachadaExterna(FachadaFuente fachadaFuente) {this.fachadaExterna = fachadaFuente;}

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
}
