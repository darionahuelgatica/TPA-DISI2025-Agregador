package ar.edu.utn.dds.k3003.bll.operacion;

import org.springframework.stereotype.Component;

@Component
public class PdiDeleteAllHandler implements IHandler {

    @Override
    public void handle(String mensaje) {
        System.out.println("PDI " + mensaje);
    }
}