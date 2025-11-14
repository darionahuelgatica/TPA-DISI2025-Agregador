package ar.edu.utn.dds.k3003.bll.operacion;

import ar.edu.utn.dds.k3003.dal.mongo.MongoDataAccess;
import org.springframework.stereotype.Component;

@Component
public class PdiDeleteAllHandler implements IHandler {

    private final MongoDataAccess mongoDataAccess;

    public PdiDeleteAllHandler(MongoDataAccess mongoDataAccess) {
        this.mongoDataAccess = mongoDataAccess;
    }

    @Override
    public void handle(String mensaje) {
        System.out.println("PDI " + mensaje);
        this.mongoDataAccess.deleteAllPdi();
    }
}