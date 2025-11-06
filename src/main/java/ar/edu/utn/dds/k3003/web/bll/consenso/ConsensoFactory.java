package ar.edu.utn.dds.k3003.web.bll.consenso;

import ar.edu.utn.dds.k3003.web.dal.client.IFachadaSolicitudes;
import org.springframework.stereotype.Component;

@Component
public class ConsensoFactory implements IConsensoFactory{
    private final IFachadaSolicitudes solicitudes;

    public ConsensoFactory (IFachadaSolicitudes solicitudes){
        this.solicitudes = solicitudes;
    }

    public Consenso getConsenso(ConsensoEnum consensoEnum){
        switch (consensoEnum) {
            case AL_MENOS_2: return new AlMenosDos();
            case TODOS: return new Todos();
            case ESTRICTO: return new Estricto(solicitudes);
            default: throw new IllegalArgumentException("No existe el consenso " + this);
        }
    }
}
