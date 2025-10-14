package edu.lorsenmarek.backend.dto;

public record SerieWithTrendingScoreResponse(
        SerieSummaryResponse serie,
        Double score
){}
