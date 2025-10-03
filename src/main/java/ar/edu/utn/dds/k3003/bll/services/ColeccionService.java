package ar.edu.utn.dds.k3003.bll.services;

import ar.edu.utn.dds.k3003.bll.consenso.*;
import ar.edu.utn.dds.k3003.dal.client.FuenteProxyFactory;
import ar.edu.utn.dds.k3003.dal.model.Coleccion;
import ar.edu.utn.dds.k3003.dal.repository.ColeccionRepository;
import ar.edu.utn.dds.k3003.dto.FuenteDTO;
import ar.edu.utn.dds.k3003.dto.HechoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ColeccionService implements IColeccionService {

    private final FuenteProxyFactory fuenteProxyFactory;
    private final ColeccionRepository coleccionRepository;
    private final IConsensoFactory consensoFactory;

    @Autowired
    public ColeccionService(FuenteProxyFactory fuenteProxyFactory, ColeccionRepository coleccionRepository, IConsensoFactory consensoFactory) {
        this.fuenteProxyFactory = fuenteProxyFactory;
        this.coleccionRepository = coleccionRepository;
        this.consensoFactory = consensoFactory;
    }

    @Override
    public List<HechoDTO> listarHechos(String coleccionId, List<FuenteDTO> fuentes) throws NoSuchElementException {
        var coleccionDb = this.coleccionRepository.findById(coleccionId);

        var coleccion = coleccionDb.isEmpty()
            ? this.coleccionRepository.save(new Coleccion(coleccionId))
            : coleccionDb.get();

        Consenso consenso = consensoFactory.crearConsenso(coleccion.getConsenso());

        List<List<HechoDTO>> hechosPorFuente = fuentes.stream()
                .map(fuente -> this.fuenteProxyFactory.getProxy(fuente.getEndpoint()))
                .map(fuenteProxy -> fuenteProxy.buscarHechosXColeccion(coleccionId))
                .toList();

        return consenso.obtenerHechos(hechosPorFuente);
    }

    @Override
    public void setConsenso(String coleccionId, ConsensoEnum consenso) {
        var coleccion = this.coleccionRepository.findById(coleccionId);
        if(coleccion.isEmpty())
            throw new NoSuchElementException(coleccionId + " no existe");

        coleccion.get().setConsenso(consenso);
        this.coleccionRepository.save(coleccion.get());
    }
}