package ar.edu.utn.dds.k3003.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Coleccion {
    List<Hecho> hechos = new ArrayList<>();

    public Coleccion(String nombre, String descripcion,List<Hecho> hechos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.hechos = hechos;
    }

    private String nombre;
    private String descripcion;
    private LocalDateTime fechaModificacion;

    public List<Hecho> getHechos() {return this.hechos;}

}
