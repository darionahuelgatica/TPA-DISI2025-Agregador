package ar.edu.utn.dds.k3003.bll.operacion;

import ar.edu.utn.dds.k3003.dal.mongo.MongoDataAccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DeleteAllHandler implements IHandler {

    private final MongoDataAccess mongoDataAccess;

    public DeleteAllHandler(MongoDataAccess mongoDataAccess) {
        this.mongoDataAccess = mongoDataAccess;
    }

    @Override
    public void handle(String mensaje) {
        System.out.println("DELETE ALL " + mensaje);

        this.mongoDataAccess.deleteAll();
    }
}