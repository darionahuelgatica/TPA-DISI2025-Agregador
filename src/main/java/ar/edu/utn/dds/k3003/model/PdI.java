package ar.edu.utn.dds.k3003.model;

import lombok.Data;

@Data
public class PdI {
    private String id;
    private String valor;

    public PdI(String id, String valor) {
        this.id = id;
        this.valor = valor;
    }
}
