package com.dolzhenko_m_s.doctorhelpsystem.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;

@Data
@NoArgsConstructor
public class AnalysisResult {
    @Setter(AccessLevel.NONE)
    private long id;

    private int patientId;

    private @NotNull String result;

    private @NotNull Date executedAnalysisDate;

    private @NotNull Date nextAnalysisDate;
}
