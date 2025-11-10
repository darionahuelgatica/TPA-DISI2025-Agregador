package ar.edu.utn.dds.k3003.bll.services;

import ar.edu.utn.dds.k3003.dal.client.FuenteProxyFactory;
import ar.edu.utn.dds.k3003.dal.model.Coleccion;
import ar.edu.utn.dds.k3003.dal.repository.ColeccionRepository;
import ar.edu.utn.dds.k3003.dto.FuenteDTO;
import ar.edu.utn.dds.k3003.dto.HechoDTO;
import ar.edu.utn.dds.k3003.bll.consenso.Consenso;
import ar.edu.utn.dds.k3003.bll.consenso.ConsensoEnum;
import ar.edu.utn.dds.k3003.bll.consenso.IConsensoFactory;
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

        Consenso consenso = consensoFactory.getConsenso(coleccion.getConsenso());

        List<List<HechoDTO>> hechosPorFuente = fuentes.stream()
                .map(fuente -> this.fuenteProxyFactory.getFuenteProxy(fuente.getEndpoint()))
                .map(fuenteProxy -> fuenteProxy.getHechos(coleccionId))
                .toList();

        return consenso.obtenerHechos(hechosPorFuente);
    }

    @Override
    public void setConsenso(String coleccionId, ConsensoEnum consenso) {
        var coleccionDb = this.coleccionRepository.findById(coleccionId);
        var coleccion = coleccionDb.isEmpty()
                ? new Coleccion(coleccionId)
                : coleccionDb.get();

        coleccion.setConsenso(consenso);
        this.coleccionRepository.save(coleccion);
    }
}