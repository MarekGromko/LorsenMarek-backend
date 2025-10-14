package edu.lorsenmarek.backend.dto;

import java.time.Instant;

public record SerieSummaryResponse(
        Long id,
        String title,
        Instant releasedAt
) {}
