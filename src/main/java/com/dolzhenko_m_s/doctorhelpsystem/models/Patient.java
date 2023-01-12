package com.dolzhenko_m_s.doctorhelpsystem.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public class Patient {
    @Setter(AccessLevel.NONE)
    private long id;

    private @NotNull String name;

    private @NotNull String phoneNumber;

    private @NotNull String address;

    private @NotNull String diagnosis;
}
