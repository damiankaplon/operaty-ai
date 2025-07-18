CREATE TABLE report_road_version
(
    report_number    INTEGER PRIMARY KEY NOT NULL,
    version          INTEGER             NOT NULL,
    area             TEXT,
    road_number      TEXT,
    "from"           TEXT,
    "to"             TEXT,
    detailed         TEXT,
    task             TEXT,
    report           TEXT,
    measurement_date TEXT,
    report_date      TEXT,
    length           TEXT,
    lowered_curb     TEXT,
    rim              TEXT,
    in_out           TEXT,
    flat             TEXT,
    pa               TEXT,
    slope            TEXT,
    ditch            TEXT,
    demolition       TEXT,
    surface          TEXT,
    volume           TEXT,
    "inner"          TEXT,
    odh              TEXT,
    dig              REAL,
    infill           REAL,
    bank             REAL,
    excavation       REAL
);