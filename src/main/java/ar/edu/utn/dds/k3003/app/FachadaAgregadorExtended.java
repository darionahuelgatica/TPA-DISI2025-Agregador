package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;

public interface FachadaAgregadorExtended extends FachadaAgregador {
    void borrarFuente(String fuenteId);
    void borrarTodasLasFuentes();
}
