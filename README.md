# LorsenMarek-backend

Travail présenté par
- Marek Grommo
- Lorsen Valri Deyfker Lamour

Travail présenté à
- professeur Philip Higgins

lien vers le github du [Frontend](https://github.com/MarekGromko/LorsenMarek-frontend)

## Avancement 

### Epic 1

- [x] Initialiser le projet Spring Boot
- [x] Endpoint `/person`
- [x] Endpoint `/person/search`
- [x] CRUD sur Person
  - class PersonController
  - class PersonRepository
- [x] Transférer la base de donnée
  - voir data.sql/schema.sql
- [x] Connexion au Frontend
- [x] Améliorer le Frontend

### Epic 2

- [x] Lombok (fait dans l'Epic 1)
- [x] JUnit (fait dans l'Epic 1)
- [x] Initialiser Jenkins
- [x] Connextion à la database (MariaDB) (fait dans l'Epic 1)
- [x] Endpoint `/serie`
- [x] Endpoint `/serie/search`
- [x] Crud sur person
  - class SerieController
  - class SerieRepository
- [x] Historique de serie utilisatuer
- [x] Endpoint `/person-serie-history`
- [x] recommendation de serie
- [x] Endpoint `/serie/recommendation`

## Installation

Installer les dépendances
```bash
mvn install
```

Pour tester: 
```bash
./mvnw clean test
```

Pour executer:
```bash
./mvnw spring-boot:run
```
