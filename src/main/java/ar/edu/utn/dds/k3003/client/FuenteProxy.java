package ar.edu.utn.dds.k3003.client;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;
import lombok.SneakyThrows;

import java.util.List;
import java.util.NoSuchElementException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class FuenteProxy implements FachadaFuente {

    private final String endpoint;
    private final FuenteRetrofitClient service;

    public FuenteProxy(ObjectMapper objectMapper) {

        var env = System.getenv();
        this.endpoint = env.getOrDefault("https://two025-tp-entrega-2-stephieortiz.onrender.com/", "http://localhost:8081/");

        var retrofit =
                new Retrofit.Builder()
                        .baseUrl(this.endpoint)
                        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                        .build();

        this.service = retrofit.create(FuenteRetrofitClient.class);
    }

    public FuenteProxy(String endpoint, FuenteRetrofitClient service) {
        this.endpoint = endpoint;
        this.service = service;
    }


    @Override
    public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
        return null;
    }

    @Override
    public ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException {
        return null;
    }

    @Override
    public HechoDTO agregar(HechoDTO hechoDTO) {
        return null;
    }

    @Override
    public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
        return null;
    }

    @SneakyThrows
    @Override
    public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException {
        Response<List<HechoDTO>> execute = service.get(coleccionId).execute();
        if (execute.isSuccessful()) {
            return execute.body();
        }
        if(execute.code() == HttpStatus.NOT_FOUND.getCode()) {
            throw new NoSuchElementException("no est activo la solicitud");
        }
        throw new RuntimeException("Error conectandose con fuente");
    }

    @Override
    public void setProcesadorPdI(FachadaProcesadorPdI procesador) {
        return;
    }

    @Override
    public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
        return null;
    }

    @Override
    public List<ColeccionDTO> colecciones() {
        return null;
    }
}