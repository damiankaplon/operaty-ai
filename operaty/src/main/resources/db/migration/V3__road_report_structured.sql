CREATE TABLE report_road
(
    id uuid PRIMARY KEY NOT NULL
);

INSERT INTO report_road (id)
SELECT DISTINCT (id)
FROM report_road_version;

ALTER TABLE report_road_version
    RENAME COLUMN id TO report_id;

CREATE TABLE report_road_pdf_content
(
    report_id   uuid PRIMARY KEY REFERENCES report_road (id) NOT NULL,
    pdf_content TEXT                                         NOT NULL
);