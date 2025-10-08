package edu.lorsenmarek.backend.model;

import org.springframework.data.annotation.Id;

import java.time.Instant;

public class Episode {
    @Id
    private Long id;
    private Long seasonId;
    private String title;
    private Integer duration;
    private Instant releasedAt;
}
