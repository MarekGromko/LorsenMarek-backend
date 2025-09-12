DROP TABLE if EXISTS person;
CREATE TABLE person(
    id INT primary key AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    gender VARCHAR(50)
);

DROP TABLE if EXISTS serie;
CREATE TABLE serie(
id int primary key AUTO_INCREMENT,
title VARCHAR(100) NOT NULL,
genre VARCHAR(100) NOT NULL,
nb_episode  int NOT null,
note int
);

