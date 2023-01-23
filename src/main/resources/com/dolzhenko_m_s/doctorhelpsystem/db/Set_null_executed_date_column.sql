ALTER TABLE analysis_result
    ALTER COLUMN executed_date DROP NOT NULL;

ALTER TABLE analysis_result
    ADD possible BOOLEAN NOT NULL DEFAULT true;