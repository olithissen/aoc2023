package net.tonick.aoc2023.day02;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main extends Solver {
    /**
     * Solution 1: Apply the 'extractOnlyByDigits' extractor to the input list and sum up the results
     */
    private static final Function<List<String>, Object> solution1 = input -> input.stream()
            .findAny();

    private static final Function<List<String>, Object> solution2 = input -> input.stream()
            .findAny();


    public static void main(String... args) {
        Solver.solve(
                InputFile.of(Main.class, "sample.txt"),
                solution1,
                Optional.empty()
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution1,
                Optional.empty()
        );

        Solver.solve(
                InputFile.of(Main.class, "sample2.txt"),
                solution2,
                Optional.empty()
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution2,
                Optional.empty()
        );
    }
}
