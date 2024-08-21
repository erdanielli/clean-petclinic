DROP TABLE IF EXISTS vet_specialties CASCADE;
DROP TABLE IF EXISTS specialty CASCADE;
DROP TABLE IF EXISTS vet CASCADE;

CREATE TABLE vet
(
    vet_id     SERIAL PRIMARY KEY,
    first_name VARCHAR(30),
    last_name  VARCHAR(30)
);

CREATE TABLE specialty
(
    specialty_id SERIAL PRIMARY KEY,
    name         VARCHAR(80)
);

CREATE TABLE vet_specialties
(
    vet_id       INT NOT NULL,
    specialty_id INT NOT NULL,
    CONSTRAINT pk_vet_specialties PRIMARY KEY (vet_id, specialty_id),
    FOREIGN KEY (vet_id) REFERENCES vet ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES specialty ON DELETE CASCADE
);