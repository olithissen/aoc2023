package net.tonick.aoc2023.day01;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main extends Solver {
    public static final Map<String, Integer> NUMBERS = Map.ofEntries(
            Map.entry("one", 1),
            Map.entry("1", 1),
            Map.entry("two", 2),
            Map.entry("2", 2),
            Map.entry("three", 3),
            Map.entry("3", 3),
            Map.entry("four", 4),
            Map.entry("4", 4),
            Map.entry("five", 5),
            Map.entry("5", 5),
            Map.entry("six", 6),
            Map.entry("6", 6),
            Map.entry("seven", 7),
            Map.entry("7", 7),
            Map.entry("eight", 8),
            Map.entry("8", 8),
            Map.entry("nine", 9),
            Map.entry("9", 9)
    );
    static Function<String, Integer> extractOnlyByDigits = s -> {
        var containedNumbers = NUMBERS.entrySet().stream()
                .filter(n -> n.getKey().matches("\\d"))
                .flatMap(n -> indicesOfAll(s, n.getKey()).stream()
                        .map(i -> new Location(
                                n.getKey(),
                                n.getValue(),
                                i
                        )))
                .filter(l -> l.position >= 0)
                .sorted(Comparator.comparingInt(o -> o.position))
                .toList();

        return containedNumbers.getFirst().value() * 10 + containedNumbers.getLast().value();
    };

    static Function<String, Integer> extractByDigitsAndNumberWords = s -> {
        var containedNumbers = NUMBERS.entrySet().stream()
                .flatMap(n -> indicesOfAll(s, n.getKey()).stream()
                        .map(i -> new Location(
                                n.getKey(),
                                n.getValue(),
                                i
                        )))
                .filter(l -> l.position >= 0)
                .sorted(Comparator.comparingInt(o -> o.position))
                .toList();

        return containedNumbers.getFirst().value() * 10 + containedNumbers.getLast().value();
    };
    private static final Function<List<String>, Object> solution1 = input -> input.stream()
            .map(extractOnlyByDigits)
            .mapToInt(Integer::intValue)
            .sum();
    private static final Function<List<String>, Object> solution2 = input -> input.stream()
            .map(extractByDigitsAndNumberWords)
            .mapToInt(Integer::intValue)
            .sum();

    public static void main(String... args) {
        Solver.solve(
                InputFile.of(Main.class, "sample.txt"),
                solution1,
                Optional.of(142),
                System.out::println
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution1,
                Optional.of(54159),
                System.out::println
        );

        Solver.solve(
                InputFile.of(Main.class, "sample2.txt"),
                solution2,
                Optional.of(281),
                System.out::println
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution2,
                Optional.of(53866),
                System.out::println
        );
    }

    private static List<Integer> indicesOfAll(String haystack, String needle) {
        return IntStream.range(0, haystack.length() - needle.length() + 1)
                .filter(index -> haystack.startsWith(needle, index))
                .boxed()
                .collect(Collectors.toList());
    }

    record Location(String name, Integer value, Integer position) {
    }
}
