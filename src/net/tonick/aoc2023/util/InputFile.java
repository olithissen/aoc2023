package net.tonick.aoc2023.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
public class InputFile implements Supplier<List<String>> {
    private final Path path;

    private InputFile(Path path) {
        this.path = path;
    }

    public static InputFile of(Class clazz, String name) {
        try {
            return new InputFile(Path.of(Objects.requireNonNull(clazz.getResource(name)).toURI()));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public List<String> readAllLines() {
        try {
            return Files.readAllLines(this.path);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public List<String> get() {
        return readAllLines();
    }
}
