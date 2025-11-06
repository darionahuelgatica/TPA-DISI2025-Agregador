package ar.edu.utn.dds.k3003.web.dal.repository.Jpa;

import ar.edu.utn.dds.k3003.web.dal.model.FuenteDeHechos;
import ar.edu.utn.dds.k3003.web.dal.repository.FuenteRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface JpaFuenteRepository extends JpaRepository<FuenteDeHechos, String>, FuenteRepository {
}