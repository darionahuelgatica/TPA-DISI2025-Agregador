package ar.edu.utn.dds.k3003.bll.handlers;

import org.springframework.stereotype.Component;

@Component
public class PdiHandler implements IHandler {

    @Override
    public void handle(String mensaje) {
        System.out.println("PDI " + mensaje);
    }
}
