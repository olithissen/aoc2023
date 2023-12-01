package net.tonick.aoc2023.day01;

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
    private static final Map<String, Integer> NUMBERS = Map.ofEntries(
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

    /**
     * Creates an extractor that finds the positions of all keywords within a String
     *
     * @param filter a RegEx to be applied to the keyword list before searching
     * @return A 2-digit number consting of the first and last number in the String
     */
    private static Function<String, Integer> createExtractor(String filter) {
        return string -> {
            var containedNumbers = NUMBERS.entrySet().stream()
                    .filter(number -> number.getKey().matches(filter))
                    .flatMap(number -> indicesOfAll(string, number.getKey()).stream()
                            .map(index -> new Location(
                                    number.getKey(),
                                    number.getValue(),
                                    index
                            )))
                    .sorted(Comparator.comparingInt(location -> location.position))
                    .toList();

            return containedNumbers.getFirst().value() * 10 + containedNumbers.getLast().value();
        };
    }

    /**
     * Finds all positions of needle within haystack
     *
     * @param haystack The String to be searched
     * @param needle The String to be found
     * @return A List of Integers describing all positions of needle within haystack
     */
    private static List<Integer> indicesOfAll(String haystack, String needle) {
        return IntStream.range(0, haystack.length() - needle.length() + 1)
                .filter(index -> haystack.startsWith(needle, index))
                .boxed()
                .collect(Collectors.toList());
    }

    private static Function<String, List<Integer>> indicesOfAll2(String needle) {
        return haystack -> IntStream.range(0, haystack.length() - needle.length() + 1)
                .filter(index -> haystack.startsWith(needle, index))
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Create an extractor with a filter that only allows digits from NUMBERS
     */
    private static final Function<String, Integer> extractOnlyByDigits = createExtractor("\\d");

    /**
     * Create an extractor with a filter that allows all values from NUMBERS
     */
    private static final Function<String, Integer> extractByDigitsAndNumberWords = createExtractor(".*");

    /**
     * Solution 1: Apply the 'extractOnlyByDigits' extractor to the input list and sum up the results
     */
    private static final Function<List<String>, Object> solution1 = input -> input.stream()
            .map(extractOnlyByDigits)
            .mapToInt(Integer::intValue)
            .sum();

    /**
     * Solution 2: Apply the 'extractByDigitsAndNumberWords' extractor to the input list and sum up the results
     */
    private static final Function<List<String>, Object> solution2 = input -> input.stream()
            .map(extractByDigitsAndNumberWords)
            .mapToInt(Integer::intValue)
            .sum();

    public static void main(String... args) {
        Solver.solve(
                InputFile.of(Main.class, "sample.txt"),
                solution1,
                Optional.of(142)
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution1,
                Optional.of(54159)
        );

        Solver.solve(
                InputFile.of(Main.class, "sample2.txt"),
                solution2,
                Optional.of(281)
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution2,
                Optional.of(53866)
        );
    }

    record Location(String name, Integer value, Integer position) {
    }
}
