package net.tonick.aoc2023.day11;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main implements Runnable {
    private static final Predicate<String> isAllSpace = s -> s.chars().allMatch(i -> i == '.');

    private static final Function<List<String>, List<String>> transpose = (inputList) -> {
        List<String> outputList = new ArrayList<>();
        int maxLength = inputList.get(0).length();

        for (int i = 0; i < maxLength; i++) {
            StringBuilder sb = new StringBuilder(inputList.size());
            for (String s : inputList) {
                if (i < s.length()) {
                    sb.append(s.charAt(i));
                }
            }
            outputList.add(sb.toString());
        }

        return outputList;
    };

    record Galaxy(Integer x, Integer y){}
    record Pair(Galaxy a, Galaxy b){
        Integer manhattanDistance() {
            return Math.max(a.x, b.x) - Math.min(a.x, b.x) + Math.max(a.y, b.y) - Math.min(a.y, b.y);
        }
    }

    private static final Function<List<String>, List<Galaxy>> getGalaxies = input -> {
        List<Galaxy> galaxies = new ArrayList<>();
        for (int row = 0; row < input.get(0).length(); row++) {
            for (int col = 0; col < input.size(); col++) {
                var tile = input.get(col).charAt(row);
                if (tile == '#') {
                    galaxies.add(new Galaxy(row, col));
                }
            }
        }

        return galaxies;
    };

    private static final Function<List<Galaxy>, List<Pair>> createUniquePairs = galaxies -> {
        return IntStream.range(0, galaxies.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, galaxies.size())
                        .mapToObj(j -> new Pair(galaxies.get(i), galaxies.get(j))))
                .toList();
    };

    private static Function<List<String>, Object> createSolution(int expansion) {
        return input -> {
            var emptyRows = IntStream.range(0, input.size()).filter(i -> isAllSpace.test(input.get(i))).boxed().toList();
            var transposed = transpose.apply(input);
            var emptyCols = IntStream.range(0, transposed.size()).filter(i -> isAllSpace.test(transposed.get(i))).boxed().toList();
            var galaxies = getGalaxies.apply(input);

            Function<Galaxy, Galaxy> redShift = galaxy -> {
                var additionalRows = (int) emptyRows.stream().filter(row -> row < galaxy.y).count();
                var additionalCols = (int) emptyCols.stream().filter(col -> col < galaxy.x).count();
                return new Galaxy(
                        galaxy.x + additionalCols * (expansion - 1),
                        galaxy.y + additionalRows * (expansion - 1)
                );
            };

            galaxies = galaxies.stream().map(redShift).toList();
            List<Pair> pairs = createUniquePairs.apply(galaxies);

            return pairs.stream().mapToLong(Pair::manhattanDistance).sum();
        };
    }

    private static final Function<List<String>, Object> solution1 = createSolution(2);
    private static final Function<List<String>, Object> solution2 = createSolution(1000000);

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(374L));
        Solver.solve(InputFile.of(Main.class, "sample2.txt"), solution1, Optional.of(6L));
        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.of(9214785L));

        Solver.solve(InputFile.of(Main.class, "sample.txt"), createSolution(10), Optional.of(1030L));
        Solver.solve(InputFile.of(Main.class, "sample.txt"), createSolution(100), Optional.of(8410L));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.of(613686987427L));
    }
}
