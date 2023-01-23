package com.dolzhenko_m_s.doctorhelpsystem.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResult {
    @Setter(AccessLevel.NONE)
    private long id;

    private long patientId;

    private @Nullable String analysisName;

    private @NotNull String result;

    private boolean possible;

    private Date executedAnalysisDate;

    private Date nextAnalysisDate;
}
