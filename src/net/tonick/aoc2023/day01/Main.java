package net.tonick.aoc2023.day01;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    BiFunction<String, String, List<Integer>> allIndicesOf = (haystack, needle) -> {
        List<Integer> result = new ArrayList<>();

        int index = 0;
        do {
            index = haystack.indexOf(needle, index);
            if (index >= 0) {
                result.add(index);
                index++;
            } else {
                break;
            }
        } while (true);

        return result;
    };
    Function<String, Integer> extractByDigitsAndNumberWords = s -> {

        var containedNumbers = NUMBERS.entrySet().stream()
                .flatMap(n -> allIndicesOf.apply(s, n.getKey()).stream().map(i -> new Locator(
                        n.getKey(),
                        n.getValue(),
                        i
                )))
                .filter(l -> l.position >= 0)
                .sorted(Comparator.comparingInt(o -> o.position))
                .toList();

        return containedNumbers.getFirst().value() * 10 + containedNumbers.getLast().value();
    };
    Function<String, Integer> extractOnlyByDigits = s -> {

        var containedNumbers = NUMBERS.entrySet().stream()
                .filter(n -> n.getKey().matches("\\d"))
                .flatMap(n -> allIndicesOf.apply(s, n.getKey()).stream()
                        .map(i -> new Locator(
                                n.getKey(),
                                n.getValue(),
                                i
                        )))
                .filter(l -> l.position >= 0)
                .sorted(Comparator.comparingInt(o -> o.position))
                .toList();

        return containedNumbers.getFirst().value() * 10 + containedNumbers.getLast().value();
    };

    public static void main(String... args) {
        var m = new Main();

        var r = m.solution1("sample.txt");
        System.out.println(r);
        var r2 = m.solution1("input.txt");
        System.out.println(r2);

        var r3 = m.solution2("sample2.txt");
        System.out.println(r3);
        var r4 = m.solution2("input.txt");
        System.out.println(r4);
    }

    @Override
    public Object solution1(String file) {
        var x = InputFile.of(file).readAllLines();
        var r = x.stream()
                .map(extractOnlyByDigits)
                .mapToInt(Integer::intValue)
                .sum();
        return r;
    }

    @Override
    public Object solution2(String file) {
        var x = InputFile.of(file).readAllLines();
        var r = x.stream()
                .map(extractByDigitsAndNumberWords)
                .mapToInt(Integer::intValue)
                .sum();
        return r;
    }

    record Locator(String name, Integer value, Integer position) {
    }
}
