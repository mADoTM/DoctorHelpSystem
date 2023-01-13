package com.dolzhenko_m_s.doctorhelpsystem.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Setter(AccessLevel.NONE)
    private long id;

    private long patientId;

    private @NotNull Date date;

    private @NotNull String action;

    private boolean executed;
}
