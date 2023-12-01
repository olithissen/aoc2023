package net.tonick.aoc2023.util;

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
        Result result = new Result(
                expectedResult.orElse(null),
                algorithm.apply(inputData.get()),
                expectedResult.map(algorithm.apply(inputData.get())::equals).orElse(true)
        );

        writer.accept(result);

        return result;
    }

    public record Result(Object expected, Object actual, boolean success) {
    }
}
