package ar.edu.utn.dds.k3003.bll.operacion;

import org.springframework.stereotype.Component;

@Component
public class DeleteAllHandler implements IHandler {

    @Override
    public void handle(String mensaje) {
        System.out.println("DELETE ALL " + mensaje);
    }
}