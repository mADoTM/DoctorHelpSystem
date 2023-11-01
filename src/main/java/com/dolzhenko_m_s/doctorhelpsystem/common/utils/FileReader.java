package com.dolzhenko_m_s.doctorhelpsystem.common.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileReader {

    public static @NotNull String read(final @NotNull String filePath) {
        final var file = new File(filePath);

        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
