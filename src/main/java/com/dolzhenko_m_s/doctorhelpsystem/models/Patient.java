package com.dolzhenko_m_s.doctorhelpsystem.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Setter(AccessLevel.NONE)
    private long id;

    private @NotNull String name;

    private @NotNull String phoneNumber;

    private @NotNull String address;

    private @NotNull String clinicalCode;

    private @NotNull String diagnosis;

    private @NotNull Date birthDate;

    private @NotNull String remark;

    private boolean hasLowAnalysis;
}
