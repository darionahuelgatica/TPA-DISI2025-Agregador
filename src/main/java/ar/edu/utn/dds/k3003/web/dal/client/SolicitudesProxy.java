package ar.edu.utn.dds.k3003.web.dal.client;

import ar.edu.utn.dds.k3003.web.dto.SolicitudDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.hibernate.query.IllegalQueryOperationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.net.SocketTimeoutException;
import java.util.List;

@Primary
@Component
public class SolicitudesProxy implements IFachadaSolicitudes {

    private SolicitudesRetrofitClient service;
    private final ObjectMapper objectMapper;

    @Value("${solicitudes.url}")
    private String url;

    public SolicitudesProxy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IllegalArgumentException {
        if(url == null || url.isBlank())
            throw new IllegalArgumentException("No se configuro la url de Solicitudes");

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        var retrofit = new Retrofit.Builder()
                .baseUrl(this.url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.service = retrofit.create(SolicitudesRetrofitClient.class);
    }

    @SneakyThrows
    @Override
    public List<SolicitudDTO> listByHechoId(String hechoId) {
        try{
            Response<List<SolicitudDTO>> result = service.listByHechoId(hechoId).execute();
            if (result.isSuccessful())
                return result.body();

            if (result.code() == HttpStatus.BAD_REQUEST.value())
                throw new IllegalQueryOperationException(result.message());

            throw new RuntimeException("Error conectándose con el componente solicitudes");
        }
        catch (SocketTimeoutException ex){
            throw new RuntimeException("Error de timeout al conectar con solicitudes " + this.url);
        }
    }
}
