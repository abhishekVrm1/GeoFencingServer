DROP DATABASE IF EXISTS geofencing_db;
CREATE DATABASE geofencing_db;

USE geofencing_db;

CREATE TABLE geofencing_configuration (
    country_code VARCHAR(2) NOT NULL PRIMARY KEY,
    rule_type ENUM('ALLOWED', 'DENIED') NOT NULL
);

CREATE TABLE geofencing_status (
    id INT PRIMARY KEY,
    active BOOLEAN NOT NULL
);

INSERT INTO geofencing_status VALUES (1000, TRUE);
