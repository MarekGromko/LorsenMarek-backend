package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.service.SerieRecommendationService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/serie/recommendation")
public class SerieRecommendationController {
    final private SerieRecommendationService recomService;
    final private ObjectMapper mapper;
    public SerieRecommendationController(final SerieRecommendationService recomService, final ObjectMapper mapper) {
        this.recomService = recomService;
        this.mapper = mapper;
    }
    @GetMapping("/{id}")
    ResponseEntity<JsonNode> getRecommendation(
            @PathVariable Integer id
    ){
        var arr = mapper.createArrayNode();
        for(var pair : recomService.computeRecommendation(id)) {
            var obj = arr.addObject();
            obj.put("weight", pair.getRight());
            obj.set("serie", mapper.valueToTree(pair.getLeft()));
        }
        return ResponseEntity.ok(arr);
    }
}
