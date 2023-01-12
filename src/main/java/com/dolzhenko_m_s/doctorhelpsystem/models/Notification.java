package com.dolzhenko_m_s.doctorhelpsystem.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;

@Data
@NoArgsConstructor
public class Notification {
    @Setter(AccessLevel.NONE)
    private long id;

    private int patientId;

    private @NotNull Date date;

    private @NotNull String action;
}
