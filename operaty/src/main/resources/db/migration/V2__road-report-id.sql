ALTER TABLE report_road_version
    DROP CONSTRAINT report_road_version_pkey;
ALTER TABLE report_road_version
    ADD COLUMN id uuid DEFAULT gen_random_uuid();
UPDATE report_road_version
SET id = gen_random_uuid()
WHERE id IS NULL;
ALTER TABLE report_road_version
    ALTER COLUMN id SET NOT NULL;
ALTER TABLE report_road_version
    ADD CONSTRAINT report_road_version_pkey PRIMARY KEY (id, version);
ALTER TABLE report_road_version
    ALTER COLUMN id DROP DEFAULT;
