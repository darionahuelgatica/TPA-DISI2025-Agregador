package ar.edu.utn.dds.k3003.bll.services;

import ar.edu.utn.dds.k3003.dal.mongo.HechoDoc;
import ar.edu.utn.dds.k3003.dto.HechoDTO;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${app.mongo.collection}")
    private String hechosCol;

    public HechoService(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    public Page<HechoDTO> search(String text, String tagsCsv, int page, int size) {
        boolean hasText = text != null && !text.isBlank();
        List<String> tags = parseCsv(tagsCsv);

        if (page < 0)  page = 0;
        if (size <= 0) size = 5;

        Query baseQ = buildQuery(hasText, text, tags);

        long total = mongo.count(baseQ, HechoDoc.class, this.hechosCol);

        if (hasText && baseQ instanceof org.springframework.data.mongodb.core.query.TextQuery) {
            ((org.springframework.data.mongodb.core.query.TextQuery) baseQ).sortByScore();
        } else {
            baseQ.with(Sort.by(Sort.Direction.ASC, "id"));
        }
        baseQ.with(PageRequest.of(page, size));

        List<HechoDTO> content = mongo.find(baseQ, HechoDoc.class, this.hechosCol)
                .stream().map(HechoDTO::fromDoc).toList();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

    private Query buildQuery(boolean hasText, String text, List<String> tags) {
        Query q = new Query();

        if (hasText) {
            // $text: { $search: text }
            TextCriteria tc = TextCriteria.forDefaultLanguage().matching(text);
            q = TextQuery.queryText(tc);
        }

        if (tags != null && !tags.isEmpty()) {
            q.addCriteria(criteriaTagsAND(tags));
        }

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