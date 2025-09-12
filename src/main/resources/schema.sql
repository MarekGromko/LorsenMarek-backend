DROP TABLE if EXISTS person;
CREATE TABLE person(
    id INT primary key AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    gender VARCHAR(50)
);
