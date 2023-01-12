CREATE TABLE patient
(
    patient_id SERIAL,
    name VARCHAR NOT NULL,
    phone VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    diagnosis VARCHAR NOT NULL,
    CONSTRAINT patient_pk PRIMARY KEY (patient_id)
);

CREATE TABLE notification
(
    notification_id SERIAL,
    date DATE NOT NULL,
    patient_id INT REFERENCES patient (patient_id) ON UPDATE CASCADE ON DELETE CASCADE,
    address VARCHAR NOT NULL,
    executed BOOL NOT NULL,
    CONSTRAINT notification_pk PRIMARY KEY (notification_id)
);

CREATE TABLE hospitalization
(
    hospitalization_id SERIAL,
    is_emergency BOOL NOT NULL,
    reason VARCHAR NOT NULL,
    CONSTRAINT hospitalization_pk PRIMARY KEY (hospitalization_id)
);

CREATE TABLE analysis_result
(
    analysis_result_id SERIAL,
    executed_date DATE NOT NULL,
    patient_id INT REFERENCES patient (patient_id) ON UPDATE CASCADE ON DELETE CASCADE,
    result VARCHAR,
    next_date DATE,
    CONSTRAINT analysis_result_pk PRIMARY KEY (analysis_result_id)
);

CREATE TABLE telephone_survey
(
    telephone_survey_id SERIAL,
    patient_id INT REFERENCES patient (patient_id) ON UPDATE CASCADE ON DELETE CASCADE,
    executed_date DATE NOT NULL,
    next_date DATE,
    hospitalization_id INT REFERENCES hospitalization (hospitalization_id) ON UPDATE CASCADE ON DELETE CASCADE,
    is_independent BOOL,
    has_family BOOL,
    is_dyspnea_progressive BOOL,
    is_idema_increase BOOL,
    has_heart_interruptions BOOL,
    has_paint_during_physical BOOL,
    upper_blood_pressure NUMERIC,
    lower_blood_pressure NUMERIC,
    heart_rate NUMERIC,
    mass NUMERIC,
    regularly_take_pills BOOL,
    drinking_water_1500ml BOOL,
    is_salt_restricted BOOL,
    decreased_exercise_tolerance BOOL,
    is_smoking BOOL,
    is_drinking_alcohol BOOL,
    treat_plan VARCHAR NOT NULL,
    next_survey_date DATE NOT NULL,
    CONSTRAINT telephone_survey_pk PRIMARY KEY (telephone_survey_id)
);
