CREATE TABLE report_road_fine_tuned_model
(
    id          uuid PRIMARY KEY NOT NULL,
    provider_id TEXT UNIQUE      NOT NULL,
    sources     JSONB            NOT NULL,
    accuracy    FLOAT,
    created_on  TIMESTAMP        NOT NULL DEFAULT NOW()
);
