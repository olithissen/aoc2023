package net.tonick.aoc2023.day08;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongBinaryOperator;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

public class Main implements Runnable {
    private static final Function<String, Direction> extractDirection = s -> {
        return new Direction(s.substring(0, 3), s.substring(7, 10), s.substring(12, 15));
    };
    public static LongBinaryOperator leastCommonMultiple = (a, b) -> (a * b) / greatestCommonDivisor(a, b);

    private static final Function<List<String>, Object> solution1 = solution("AAA", "ZZZ");
    private static final Function<List<String>, Object> solution2 = solution("A", "Z");

    private static Function<List<String>, Object> solution(String start, String end) {
        return input -> {
            var loop = input.get(0);
            var lookup = input.subList(2, input.size()).stream()
                    .map(extractDirection)
                    .collect(Collectors.toMap(Direction::from, Function.identity()));

            var startPositions = lookup.entrySet().stream()
                    .filter(k -> k.getKey().endsWith(start))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            ToLongFunction<Direction> numberOfSteps = k -> {
                var stepCounter = 0;
                var currentPosition = lookup.get(k.from);
                do {
                    var posInLoop = stepCounter % loop.length();
                    String directionChooser = loop.substring(posInLoop, posInLoop + 1);
                    currentPosition = directionChooser.equals("L")
                            ? lookup.get(currentPosition.left)
                            : lookup.get(currentPosition.right);
                    stepCounter++;
                } while (!currentPosition.from.endsWith(end));

                return stepCounter;
            };

            return startPositions.values().stream()
                    .mapToLong(numberOfSteps)
                    .reduce(1, leastCommonMultiple);
        };
    }
    private static long greatestCommonDivisor(long a, long b) {
        if (b == 0) return a;
        return greatestCommonDivisor(b, a % b);
    }

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(2L));

        Solver.solve(InputFile.of(Main.class, "sample2.txt"), solution1, Optional.of(6L));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.of(19951L));

        Solver.solve(InputFile.of(Main.class, "sample3.txt"), solution2, Optional.of(6L));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.of(16342438708751L));
    }

    private record Direction(String from, String left, String right) {}
}
