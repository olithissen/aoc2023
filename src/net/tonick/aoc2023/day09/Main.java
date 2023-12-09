package net.tonick.aoc2023.day09;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class Main implements Runnable {
    /**
     * Takes one input line and return individual Integers as List.
     */
    private static final Function<String, List<Integer>> parseLine = line -> Arrays.stream(line.split(" "))
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .toList();

    /**
     * A Function to calculate differences between numbers. It takes a List of Integers and uses reduce to
     * create a new List representing the differences between each consecutive number
     */
    private static final Function<List<Integer>, List<Integer>> createDifferences = input -> {
        List<Integer> differences = new ArrayList<>();

        //noinspection ResultOfMethodCallIgnored
        input.stream().reduce((previous, current) -> {
            differences.add(current - previous);
            return current;
        });
        return differences;
    };

    /**
     * A solver for a single Line. It applies the createDifferences Function to add further Lines in depth, until
     * all Items of the last list are 0. It returns the full List of Lists.
     */
    private static final Function<List<Integer>, List<List<Integer>>> solveLine = input -> {
        List<List<Integer>> depth = new ArrayList<>();

        depth.add(input);
        do {
            depth.add(createDifferences.apply(depth.getLast()));
        } while (!depth.getLast().stream().allMatch(v -> v.equals(0)));
        return depth;
    };

    /**
     * A reducer that calculates the missing value based on the last values of List A and List A+1
     */
    private static final BinaryOperator<List<Integer>> reduceToLastItem = (a, b) -> {
        List<Integer> list = new ArrayList<>(b);
        list.add(b.getLast() + a.getLast());
        return list;
    };

    /**
     * A reducer that calculates the missing value based on the first values of List A and List A+1
     */
    private static final BinaryOperator<List<Integer>> reduceToFirstItem = (a, b) -> {
        List<Integer> list = new ArrayList<>();
        list.add(b.getFirst() - a.getFirst());
        list.addAll(b);
        return list;
    };

    /**
     * A factory method to create a solver
     *
     * @param reducer The reducer to be applied to calculate missing values
     * @param resultPicker The mapper used to pick a value from the final List of Integers
     * @return A function that takes a List of Strings and returns the result as an Object
     */
    private static Function<List<String>, Object> createSolver(
            BinaryOperator<List<Integer>> reducer, ToIntFunction<List<Integer>> resultPicker) {
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
