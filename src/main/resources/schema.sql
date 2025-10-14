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
    `first_name` varchar(255),
    `last_name` varchar(255),
    `email` varchar(255),
    `title` varchar(255),
    `pwd_digest` varchar(255),
    `pwd_attempts` int,
    `pwd_last_attempted_at` timestamp,
    PRIMARY KEY (`id`),
    KEY `email` (`email`)
);

CREATE TABLE `serie`(
    `id` bigint AUTO_INCREMENT,
    `title` varchar(255),
    `released_at` timestamp,
    PRIMARY KEY (`id`)
);

CREATE TABLE `season` (
    `id` bigint AUTO_INCREMENT,
    `serie_id` bigint,
    `title` varchar(255),
    `duration` int,
    `released_at` TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `season_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE CASCADE
);

CREATE TABLE `episode` (
    `id` bigint AUTO_INCREMENT,
    `season_id` bigint,
    `title` varchar(255),
    `duration` int,
    `released_at` timestamp,
    PRIMARY KEY (`id`),
    CONSTRAINT `episode_fk_season` FOREIGN KEY (`season_id`) REFERENCES `Season` (`id`) ON DELETE CASCADE
);

CREATE TABLE `genre` (
    `id` bigint AUTO_INCREMENT,
    `label` varchar(255),
    PRIMARY KEY (`id`)
);

CREATE TABLE `serie_genre` (
    `serie_id` bigint,
    `genre_id` bigint,
    PRIMARY KEY (`serie_id`, `genre_id`),
    CONSTRAINT `serie_genre_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE CASCADE,
    CONSTRAINT `serie_genre_fk_genre` FOREIGN KEY (`genre_id`) REFERENCES `Genre` (`id`) ON DELETE CASCADE
);

CREATE TABLE `user_serie_rating` (
    `user_id` bigint,
    `serie_id` bigint,
    `created_at` timestamp,
    `modified_at` timestamp,
    `rating` int,
    PRIMARY KEY (`user_id`, `serie_id`),
    CONSTRAINT `user_serie_rating_fk_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE,
    CONSTRAINT `user_serie_rating_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE CASCADE
);

CREATE TABLE `user_episode_rating` (
    `user_id` bigint,
    `episode_id` bigint,
    `created_at` timestamp,
    `modified_at` timestamp,
    `rating` int,
    PRIMARY KEY (`user_id`, `episode_id`),
    CONSTRAINT `user_episode_rating_fk_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE,
    CONSTRAINT `user_episode_rating_fk_episode` FOREIGN KEY (`episode_id`) REFERENCES `Episode` (`id`) ON DELETE CASCADE
);

CREATE TABLE `user_episode_history` (
    `user_id` bigint NOT NULL,
    `episode_id` bigint NOT NULL,
    `watched_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `watched_duration` int NOT NULL DEFAULT 1,
    PRIMARY KEY (`user_id`,`episode_id`),
    KEY `episode_id` (`episode_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `user_episode_history_fk_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE,
    CONSTRAINT `user_episode_history_fk_episode` FOREIGN KEY (`episode_id`) REFERENCES `Episode` (`id`) ON DELETE CASCADE
);

-- view creation
CREATE VIEW `user_serie_history` AS (
    SELECT
        `serie`.`id`                AS `serie_id`,
        `history`.`user_id`         AS `user_id`,
        MAX(`history`.`watched_at`) AS `watched_at`
    FROM
        `user_episode_history`  as `history`
        INNER JOIN `episode`    ON `history`.`episode_id` = `episode`.`id`
        INNER JOIN `season`     ON `episode`.`season_id` = `season`.`id`
        INNER JOIN `serie`      ON `season`.`serie_id` = `serie`.`id`
    GROUP BY
        `serie`.`id`,
        `history`.`user_id`
);

