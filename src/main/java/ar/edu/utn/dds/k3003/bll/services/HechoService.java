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

    public Page<HechoDTO> search(String text, String tagsCsv, int skip, int limit) {

        boolean hasText = text != null && !text.isBlank();
        List<String> tags = parseCsv(tagsCsv);

        if (limit <= 0) limit = 10;
        if (skip < 0)  skip = 0;
        int page = skip / limit;
        Pageable pageable = PageRequest.of(page, limit);

        Query dataQ = buildQuery(hasText, text, tags);
        dataQ.with(Sort.by(Sort.Direction.DESC, "updatedAt"));
        dataQ.with(pageable);

        List<HechoDTO> content = mongo.find(dataQ, HechoDoc.class)
                .stream()
                .map(HechoDTO::fromDoc)
                .toList();

        Query countQ = buildQuery(hasText, text, tags);
        long total = mongo.count(countQ, HechoDoc.class);

        return new PageImpl<>(content, pageable, total);
    }

    private Query buildQuery(boolean hasText, String text, List<String> tags) {
        Query q;

        if (hasText) {
            // $text: { $search: text }
            TextCriteria tc = TextCriteria.forDefaultLanguage().matching(text);
            q = TextQuery.queryText(tc);
        } else {
            q = new Query();
        }

        if (tags != null && !tags.isEmpty()) {
            q.addCriteria(criteriaTagsAND(tags));
        }

        q.addCriteria(Criteria.where("eliminado").is(false));
        return q;
    }

    private Criteria criteriaTagsAND(List<String> tags) {
        Criteria[] perTag = tags.stream()
                .map(tag -> Criteria.where("pdis.etiquetas").is(tag))
                .toArray(Criteria[]::new);

        return new Criteria().andOperator(perTag);
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