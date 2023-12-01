package net.tonick.aoc2023.util;

import net.tonick.aoc2023.day01.Main3;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class InputFile {
    private final Path path;

    public static InputFile of(String name) {
        var res = Main3.class.getResource(name);
        try {
            return new InputFile(Path.of(res.toURI()));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private InputFile(Path path) {
        this.path = path;
    }

    public List<String> readAllLines() {
        try {
            return Files.readAllLines(this.path);
        } catch (IOException e) {
            return null;
        }
    }
}
