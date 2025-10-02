DROP TABLE if EXISTS person_serie_history;
DROP TABLE if EXISTS person;
DROP TABLE if EXISTS serie;

CREATE TABLE `user`(
    `id` bigint AUTO_INCREMENT,
    `first_name` varchar,
    `last_name` varchar,
    `email` varchar,
    `tile` varchar,
    `pwd_digest` varchar,
    PRIMARY KEY (`id`),
    KEY `email` (`email`)
);

CREATE TABLE `serie`(
    `id` bigint AUTO_INCREMENT,
    `title` varchar,
    `release_ts` timestamp,
    PRIMARY KEY (`id`)
);

CREATE TABLE `season` {
    `id` bigint AUTO_INCREMENT,
    `serie_id` bigint,
    `title` varchar,
    `duration` int,
    `release_ts` TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `season_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE CASCADE
};

CREATE TABLE `episode` {
    `id` bigint AUTO_INCREMENT,
    `season_id` bigint,
    `title` varchar,
    `duration` int,
    `release_ts` timestamp,
    PRIMARY KEY (`id`),
    CONSTRAINT `episode_fk_season` FOREIGN KEY (`season_id`) REFERENCES `Season` (`id`) ON DELETE CASCADE
};

CREATE TABLE `genre` {
    `id` bigint AUTO_INCREMENT,
    `label` varchar
};

CREATE TABLE `serie_genre` {
    `serie_id` bigint,
    `genre_id` bigint,
    PRIMARY KEY (`serie_id`, `genre_id`),
    CONSTRAINT `serie_genre_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE CASCADE,
    CONSTRAINT `serie_genre_fk_genre` FOREIGN KEY (`genre_id`) REFERENCES `Genre` (`id`) ON DELETE CASCADE
};

CREATE TABLE `user_serie_rating` {
    `user_id` bigint,
    `serie_id` bigint,
    `create_ts` timestamp,
    `last_modifier_ts` timestamp,
    `rating` int,
    PRIMARY KEY (`user_id`, `serie_id`),
    CONSTRAINT `user_serie_rating_fk_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
    CONSTRAINT `user_serie_rating_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`)
};

CREATE TABLE `user_episode_rating` {
    `user_id` bigint,
    `episode_id` bigint,
    `create_ts` timestamp,
    `last_modifier_ts` timestamp,
    `rating` int,
    PRIMARY KEY (`user_id`, `serie_id`),
    CONSTRAINT `user_episode_rating_fk_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
    CONSTRAINT `user_episode_rating_fk_episode` FOREIGN KEY (`serie_id`) REFERENCES `Episode` (`id`)
};

CREATE TABLE `user_episode_history` (
    `person_id` bigint NOT NULL,
    `serie_id` bigint NOT NULL,
    `last_watch` timestamp NOT NULL DEFAULT current_timestamp(),
    `instance_watch` int NOT NULL DEFAULT 1,
    PRIMARY KEY (`person_id`,`serie_id`),
    KEY `serie_id` (`serie_id`),
    KEY `person_id` (`person_id`),
    CONSTRAINT `person_serie_history_fk_person` FOREIGN KEY (`person_id`) REFERENCES `Person` (`id`) ON DELETE CASCADE,
    CONSTRAINT `person_serie_history_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE CASCADE
);

