CREATE TABLE report_road_fine_tuned_model_jsonl_file_reference
(
    fine_tuned_model_id TEXT NOT NULL,
    file_reference      TEXT NOT NULL,
    PRIMARY KEY (fine_tuned_model_id, file_reference)
);
