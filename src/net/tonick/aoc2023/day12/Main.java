package net.tonick.aoc2023.day12;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main implements Runnable {
    private static Function<List<Integer>, String> generateRegexPattern = positions -> {
        return "\\.*%s\\.*".formatted(positions.stream()
                .map("#{%d}"::formatted)
                .collect(Collectors.joining("\\.+")));
    };

    private static Function<String, Row> extractRow = input -> {
        String[] elements = input.split("[\\s,]");
        List<Integer> positions = Arrays.stream(elements, 1, elements.length)
                .map(Integer::parseInt)
                .toList();
        return new Row(elements[0],
                positions,
                positions.stream().reduce(0, Integer::sum),
                generateRegexPattern.apply(positions));
    };

    private static Function<Row, Row> multiplexRow = input -> {
        String newPattern = IntStream.range(0, 5).mapToObj(i -> input.pattern).collect(Collectors.joining("?"));
        var positions = IntStream.range(0, 5).mapToObj(i -> input.positions).flatMap(Collection::stream).toList();

        return new Row(newPattern,
                positions,
                positions.stream().reduce(0, Integer::sum),
                generateRegexPattern.apply(positions));
    };

    private static Function<Row, Stream<Variation>> variate = input -> {
        List<String> variations = new ArrayList<>();
        generateVariations(input.pattern.toCharArray(), 0, variations);

        return variations.stream().map(v -> new Variation(input, v));
    };

    private static void generateVariations(char[] chars, int index, List<String> variations) {
        if (index == chars.length) {
            variations.add(new String(chars));
            return;
        }

        if (chars[index] == '?') {
            chars[index] = '#';
            generateVariations(chars, index + 1, variations);
            chars[index] = '.';
            generateVariations(chars, index + 1, variations);
            chars[index] = '?'; // Reset the character to '?'
        } else {
            generateVariations(chars, index + 1, variations);
        }
    }

    private static final Predicate<Variation> isValid = input -> {
        var numberOfHashes = input.mutation.chars().filter(ch -> ch == '#').count();
        if (numberOfHashes != input.row.numberOfHashes) {
            return false;
        }

        return input.mutation.matches(input.row.regexPattern);
    };

    private static final Function<List<String>, Object> solution1 = input -> {
        var rows = input.stream()
                .map(extractRow)
                .parallel()
                .flatMap(variate)
                .filter(isValid)
                .count();
        return rows;
    };
    private static final Function<List<String>, Object> solution2 = input -> {
        var rows = input.stream()
                .map(extractRow)
                .map(multiplexRow)
                .parallel()
                .flatMap(variate)
                .filter(isValid)
                .count();
        return rows;
    };

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(21L));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.of(6949L));

        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution2, Optional.of(525152L));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.empty());
    }

    private record Row(String pattern, List<Integer> positions, Integer numberOfHashes, String regexPattern) {}
    private record Variation(Row row, String mutation) {}
}
