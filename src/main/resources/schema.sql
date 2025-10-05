DROP TABLE IF EXISTS `user_serie_rating`;
DROP TABLE IF EXISTS `user_episode_rating`;
DROP TABLE IF EXISTS `user_episode_history`;
DROP TABLE IF EXISTS `serie_genre`;
DROP TABLE IF EXISTS `genre`;
DROP TABLE IF EXISTS `episode`;
DROP TABLE IF EXISTS `season`;
DROP TABLE if EXISTS `serie`;
DROP TABLE if EXISTS `user`;

CREATE TABLE `user`(
    `id` bigint AUTO_INCREMENT,
    `first_name` varchar,
    `last_name` varchar,
    `email` varchar,
    `title` varchar,
    `pwd_digest` varchar,
    PRIMARY KEY (`id`),
    KEY `email` (`email`)
);

CREATE TABLE `serie`(
    `id` bigint AUTO_INCREMENT,
    `title` varchar,
    `released_at` timestamp,
    PRIMARY KEY (`id`)
);

CREATE TABLE `season` {
    `id` bigint AUTO_INCREMENT,
    `serie_id` bigint,
    `title` varchar,
    `duration` int,
    `released_at` TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `season_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE CASCADE
};

CREATE TABLE `episode` {
    `id` bigint AUTO_INCREMENT,
    `season_id` bigint,
    `title` varchar,
    `duration` int,
    `released_at` timestamp,
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
    `created_at` timestamp,
    `modified_at` timestamp,
    `rating` int,
    PRIMARY KEY (`user_id`, `serie_id`),
    CONSTRAINT `user_serie_rating_fk_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE SET NULL,
    CONSTRAINT `user_serie_rating_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE CASCADE
};

CREATE TABLE `user_episode_rating` {
    `user_id` bigint,
    `episode_id` bigint,
    `created_at` timestamp,
    `modified_at` timestamp,
    `rating` int,
    PRIMARY KEY (`user_id`, `serie_id`),
    CONSTRAINT `user_episode_rating_fk_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE SET NULL,
    CONSTRAINT `user_episode_rating_fk_episode` FOREIGN KEY (`serie_id`) REFERENCES `Episode` (`id`) ON DELETE CASCADE
};

CREATE TABLE `user_episode_history` (
    `user_id` bigint NOT NULL,
    `serie_id` bigint NOT NULL,
    `last_watch` timestamp NOT NULL DEFAULT current_timestamp(),
    `instance_watch` int NOT NULL DEFAULT 1,
    PRIMARY KEY (`user_id`,`serie_id`),
    KEY `serie_id` (`serie_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `user_serie_history_fk_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE,
    CONSTRAINT `user_serie_history_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE CASCADE
);

