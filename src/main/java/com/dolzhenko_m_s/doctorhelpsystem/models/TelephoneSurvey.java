package com.dolzhenko_m_s.doctorhelpsystem.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelephoneSurvey {
    @Setter(AccessLevel.NONE)
    private long id;

    private int patientId;

    private @NotNull Date executedDate;

    private int hospitalizationId;

    private boolean isIndependent;

    private boolean hasFamily;

    private boolean isDyspneaProgressive;

    private boolean isEdemaIncrease;

    private boolean hasHeartInterruptions;

    private boolean hasPainDuringPhysicalExercise;

    private boolean hasEveryDayControl;

    private double upperBloodPressure;

    private double lowerBloodPressure;

    private int heartRate;

    private double mass;

    private boolean regularlyTakePills;

    private boolean drinkingWater1500ml;

    private boolean isSaltRestricted;

    private boolean decreasedExerciseTolerance;

    private boolean isSmoking;

    private boolean isDrinkingAlcohol;

    private @NotNull String treatPlan;

    private @NotNull Date nextSurvey;
}
