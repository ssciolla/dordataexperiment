-- Alternative model

CREATE TABLE IF NOT EXISTS digital_object (
    id BIGSERIAL PRIMARY KEY,
    bin_identifier VARCHAR(255) NOT NULL,
    identifier VARCHAR(255) NOT NULL,
    alternate_identifier VARCHAR(255) NOT NULL,
    "type" VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS digital_object_version (
    id BIGSERIAL PRIMARY KEY,
    version_number INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    digital_object BIGSERIAL REFERENCES digital_object (id)
);


CREATE TABLE IF NOT EXISTS digital_object_file (
    id BIGSERIAL PRIMARY KEY,
    identifier VARCHAR(255) NOT NULL,
    file_format VARCHAR(255) NOT NULL,
    file_function VARCHAR(255) NOT NULL,
    "size" INTEGER NOT NULL,
    digest VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    digital_object_version BIGSERIAL REFERENCES digital_object_version (id)
);

-- Current model

CREATE TABLE IF NOT EXISTS intellectual_object (
    id BIGSERIAL PRIMARY KEY,
    bin_identifier VARCHAR(255) NOT NULL,
    identifier VARCHAR(255) NOT NULL,
    alternate_identifier VARCHAR(255) NOT NULL,
    "type" VARCHAR(255) NOT NULL,
    version_number INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS object_file (
    id BIGSERIAL PRIMARY KEY,
    identifier VARCHAR(255) NOT NULL,
    file_format VARCHAR(255) NOT NULL,
    file_function VARCHAR(255) NOT NULL,
    "size" INTEGER NOT NULL,
    digest VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    intellectual_object BIGSERIAL REFERENCES intellectual_object (id)
);

CREATE OR REPLACE VIEW current_intellectual_object AS
SELECT io.id as id,
       io.identifier AS identifier,
       io.alternate_identifier AS alternate_identifier,
       io.title AS title,
       io.type AS type,
       io.created_at AS created_at,
       io.version_number AS version_number
FROM intellectual_object io
INNER JOIN (
    SELECT identifier, MAX(version_number) as max_value
    FROM intellectual_object
    GROUP BY identifier
) max_versions
ON io.identifier = max_versions.identifier and io.version_number = max_versions.max_value;
