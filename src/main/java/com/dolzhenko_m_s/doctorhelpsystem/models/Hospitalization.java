package com.dolzhenko_m_s.doctorhelpsystem.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public class Hospitalization {
    @Setter(AccessLevel.NONE)
    private long id;

    private boolean isEmergency;

    private @NotNull String reason;
}
