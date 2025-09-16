DROP TABLE if EXISTS person_serie_history;
DROP TABLE if EXISTS person;
DROP TABLE if EXISTS serie;

CREATE TABLE person(
    id INT primary key AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    gender VARCHAR(50)
);

CREATE TABLE serie(
    id int primary key AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    nb_episode  int NOT NULL,
    note int
);

CREATE TABLE `person_serie_history` (
    `person_id` int NOT NULL,
    `serie_id` int NOT NULL,
    `last_watch` timestamp NOT NULL DEFAULT current_timestamp(),
    `watch_duration_ms` int NOT NULL DEFAULT 0,
    PRIMARY KEY (`person_id`,`serie_id`),
    KEY `serie_id` (`serie_id`),
    KEY `person_id` (`person_id`),
    CONSTRAINT `person_serie_history_fk_person` FOREIGN KEY (`person_id`) REFERENCES `Person` (`id`),
    CONSTRAINT `person_serie_history_fk_serie` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`)
);


