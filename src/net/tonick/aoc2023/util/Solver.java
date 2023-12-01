package net.tonick.aoc2023.util;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Solver {
    public static Result solve(Supplier<List<String>> inputData,
                               Function<List<String>, Object> algorithm,
                               Optional<Object> expectedResult
    ) {
        return solve(inputData, algorithm, expectedResult, System.out::println);
    }

    public static Result solve(Supplier<List<String>> inputData,
                               Function<List<String>, Object> algorithm,
                               Optional<Object> expectedResult,
                               Consumer<Object> writer) {

        var start = Instant.now();
        Object algorithmResult = algorithm.apply(inputData.get());
        var executionTime = Duration.between(start, Instant.now()).toNanos() * 0.000001;


        Result result = new Result(
                expectedResult.orElse(null),
                algorithmResult,
                expectedResult.map(algorithmResult::equals).orElse(true),
                executionTime);

        writer.accept(result);

        return result;
    }

    public record Result(Object expected, Object actual, boolean success, double executionTime) {
    }
}
