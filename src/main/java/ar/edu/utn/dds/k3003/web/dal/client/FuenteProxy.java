package ar.edu.utn.dds.k3003.web.dal.client;

import ar.edu.utn.dds.k3003.web.dto.HechoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class FuenteProxy implements IFachadaFuente {

    private final FuenteRetrofitClient service;
    private final String url;

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
    public List<HechoDTO> getHechos(String coleccionId) throws NoSuchElementException {
        Response<List<HechoDTO>> execute = service.get(coleccionId).execute();
        if (execute.isSuccessful())
            return execute.body();

        if(execute.code() == HttpStatus.NOT_FOUND.value())
            return Collections.emptyList();

        throw new RuntimeException(String.format("Error conectandose con Fuente en %s", this.url));
    }
}