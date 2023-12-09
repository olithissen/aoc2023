package net.tonick.aoc2023.day09;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class Main implements Runnable {
    private static final Function<String, List<Integer>> parseLine = line -> Arrays.stream(line.split(" "))
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .toList();

    private static final Function<List<Integer>, List<Integer>> solveSingleLine = input -> {
        List<Integer> differences = new ArrayList<>();

        //noinspection ResultOfMethodCallIgnored
        input.stream()
                .reduce((previous, current) -> {
                    differences.add(current - previous);
                    return current;
                });
        return differences;
    };

    private static final BinaryOperator<List<Integer>> reduceToLastItem = (a, b) -> {
        List<Integer> list = new ArrayList<>(b);
        list.add(b.getLast() + a.getLast());
        return list;
    };

    private static final BinaryOperator<List<Integer>> reduceToFirstItem = (a, b) -> {
        List<Integer> list = new ArrayList<>();
        list.add(b.getFirst() - a.getFirst());
        list.addAll(b);
        return list;
    };

    private static final Function<List<Integer>, List<List<Integer>>> solveLine = input -> {
        List<List<Integer>> depth = new ArrayList<>();

        depth.add(input);
        do {
            depth.add(solveSingleLine.apply(depth.getLast()));
        } while (!depth.getLast().stream().allMatch(v -> v.equals(0)));
        return depth;
    };

    private static Function<List<String>, Object> createSolver(BinaryOperator<List<Integer>> reducer, ToIntFunction<List<Integer>> resultPicker) {
        return input -> input.stream()
                .map(parseLine)
                .map(solveLine)
                .map(s -> s.reversed().stream()
                        .reduce(reducer)
                        .orElse(List.of()))
                .mapToInt(resultPicker)
                .sum();
    }

    private static final Function<List<String>, Object> solution1 = createSolver(reduceToLastItem, List::getLast);
    private static final Function<List<String>, Object> solution2 = createSolver(reduceToFirstItem, List::getFirst);

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(114));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.of(1969958987));

        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution2, Optional.of(2));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.of(1068));
    }
}
