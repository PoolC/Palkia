DO $$
DECLARE
    project_row RECORD;
    date_parts TEXT[];
    start_parts TEXT[];
    end_parts TEXT[];
    start_date_value DATE;
    end_date_value DATE;
BEGIN
    FOR project_row IN SELECT id, duration FROM project WHERE start_date IS NULL LOOP
        start_parts := NULL;
        end_parts := NULL;

        FOR date_parts IN
            SELECT regexp_matches(
                project_row.duration,
                '([0-9]{4})(?:[.-]([0-9]{1,2}))?(?:[.-]([0-9]{1,2}))?',
                'g'
            )
        LOOP
            IF start_parts IS NULL THEN
                start_parts := date_parts;
            ELSIF end_parts IS NULL THEN
                end_parts := date_parts;
            END IF;
        END LOOP;

        IF start_parts IS NULL THEN
            RAISE EXCEPTION 'Cannot derive a start date for project %: %', project_row.id, project_row.duration;
        END IF;

        start_date_value := make_date(
            start_parts[1]::INTEGER,
            COALESCE(start_parts[2]::INTEGER, 1),
            COALESCE(start_parts[3]::INTEGER, 1)
        );

        IF end_parts IS NULL THEN
            end_date_value := NULL;
        ELSIF end_parts[3] IS NULL THEN
            end_date_value := (
                make_date(end_parts[1]::INTEGER, COALESCE(end_parts[2]::INTEGER, 12), 1)
                + INTERVAL '1 month - 1 day'
            )::DATE;
        ELSE
            end_date_value := make_date(
                end_parts[1]::INTEGER,
                COALESCE(end_parts[2]::INTEGER, 1),
                end_parts[3]::INTEGER
            );
        END IF;

        UPDATE project
        SET start_date = start_date_value,
            end_date = end_date_value
        WHERE id = project_row.id;
    END LOOP;
END $$;
