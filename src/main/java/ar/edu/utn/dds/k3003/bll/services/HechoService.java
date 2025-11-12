package ar.edu.utn.dds.k3003.bll.services;

import ar.edu.utn.dds.k3003.dal.mongo.HechoDoc;
import ar.edu.utn.dds.k3003.dto.HechoDTO;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechoService {
    private final MongoTemplate mongo;

    public HechoService(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    public Page<HechoDTO> search(String text, String etiquetasCsv, int page, int size) {
        String queryText = text == null ? "" : text.trim();
        Pageable pageable = PageRequest.of(page, size);
        Criteria base = Criteria.where("eliminado").is(false);

        List<String> etiquetas = parseCsv(etiquetasCsv);
        if (!etiquetas.isEmpty()) {
            base = new Criteria().andOperator(
                base,
                Criteria.where("etiquetas").all(etiquetas)
            );
        }

        boolean hasText = !queryText.isBlank();

        if (hasText) {
            TextCriteria txt = TextCriteria.forLanguage("spanish").matching(queryText);

            Query tq = TextQuery.queryText(txt)
                    .sortByScore()
                    .with(Sort.by(Sort.Direction.DESC, "updatedAt"))
                    .with(pageable);

            tq.addCriteria(base);

            var list = mongo.find(tq, HechoDoc.class);
            Query countQ = new Query().addCriteria(base).addCriteria(txt);
            long total = mongo.count(countQ, HechoDoc.class);

            var dtos = list.stream().map(HechoDTO::from).toList();
            return new PageImpl<>(dtos, pageable, total);
        } else {
            Query qy = new Query(base)
                    .with(Sort.by(Sort.Direction.DESC, "updatedAt"))
                    .with(pageable);

            var list = mongo.find(qy, HechoDoc.class);
            long total = mongo.count(new Query(base), HechoDoc.class);

            var dtos = list.stream().map(HechoDTO::from).toList();
            return new PageImpl<>(dtos, pageable, total);
        }
    }

    private static List<String> parseCsv(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
    }
}