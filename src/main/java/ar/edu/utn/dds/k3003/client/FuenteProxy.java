package ar.edu.utn.dds.k3003.client;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class FuenteProxy implements FachadaFuente {

    private final FuenteRetrofitClient service;
    private String url;

    public FuenteProxy(String fuenteApiUrl, ObjectMapper objectMapper) {
        if(fuenteApiUrl == null || fuenteApiUrl.isBlank()) {
            throw new IllegalArgumentException("No se puede inicializar FuenteProxy con una url vacia");
        }
        this.url = fuenteApiUrl;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.service = retrofit.create(FuenteRetrofitClient.class);
    }

    @SneakyThrows
    @Override
    public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException {
        Response<List<HechoDTO>> execute = service.get(coleccionId).execute();
        if (execute.isSuccessful())
            return execute.body();

        if(execute.code() == HttpStatus.NOT_FOUND.getCode())
            return Collections.emptyList();

        throw new RuntimeException(String.format("Error conectandose con Fuente en %s", this.url));
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