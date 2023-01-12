package com.dolzhenko_m_s.doctorhelpsystem.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hospitalization {
    @Setter(AccessLevel.NONE)
    private long id;

    private boolean isEmergency;

    private @NotNull String reason;
}
