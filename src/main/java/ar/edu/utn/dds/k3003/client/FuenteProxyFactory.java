package ar.edu.utn.dds.k3003.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FuenteProxyFactory {

    private final ObjectMapper objectMapper;
    private final Map<String, FuenteProxy> cache = new ConcurrentHashMap<>();

    public FuenteProxyFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public FuenteProxy getProxy(String endpoint) {
        return cache.computeIfAbsent(endpoint, ep -> new FuenteProxy(ep, objectMapper));
    }
}