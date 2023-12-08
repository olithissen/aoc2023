package net.tonick.aoc2023.day06;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main implements Runnable {
    /**
     * Gets numbers from the input line without preprocessing
     */
    private static final Function<String, List<Long>> getNumberFromTableSplit = getGetNumbers(Function.identity());

    /**
     * Gets numbers from the input line with replacing with removing all whitespaces beforehand
     */
    private static final Function<String, List<Long>> getNumbersFromTableJoined =
            getGetNumbers(s -> s.replaceAll("\\s", ""));

    /**
     * Gets all races using split numbers
     */
    private static final Function<List<String>, List<Race>> getRacesSplit = getRaces(getNumberFromTableSplit);

    /**
     * Gets all races using joined number
     */
    private static final Function<List<String>, List<Race>> getRacesJoined = getRaces(getNumbersFromTableJoined);

    /**
     * A BiFunction that calculates the race distance based on hold time and maximum race time
     */
    private static final BiFunction<Long, Long, Long> distance =
            (holdTime, raceTime) -> holdTime * (raceTime - holdTime);

    /**
     * A Function that gets the list of winning scenarios for each race
     */
    private static final Function<Race, List<Long>> distances = race -> LongStream.range(0, race.time)
            .map(i -> distance.apply(i, race.time))
            .filter(distance -> distance > race.distance)
            .boxed()
            .toList();
    /**
     * A Reducer for calculating a product
     */
    private static final IntBinaryOperator product = (acc, cur) -> acc > 0 ? acc * cur : cur;
    /**
     * Solution 1
     */
    private static final Function<List<String>, Object> solution1 = input -> {
        var races = getRacesSplit.apply(input);
        return races.stream().map(distances).mapToInt(List::size).reduce(product).getAsInt();
    };
    /**
     * Solution 2
     */
    private static final Function<List<String>, Object> solution2 = input -> {
        var races = getRacesJoined.apply(input);
        return races.stream().map(distances).mapToInt(List::size).reduce(product).getAsInt();
    };

    /**
     * Get all races from the input using a table reading strategy
     *
     * @param tableReader A function for reading and interpreting the input line
     * @return A list of all races
     */
    private static Function<List<String>, List<Race>> getRaces(Function<String, List<Long>> tableReader) {
        return input -> {
            var s = input.stream().map(tableReader).toList();

            return IntStream.range(0, s.get(0).size())
                    .mapToObj(i -> new Race(s.get(0).get(i), s.get(1).get(i)))
                    .toList();
        };
    }

    /**
     * A function to get the numbers from the input line with a predefined line preprocessor
     *
     * @param linePreprocessor A function to prepare the number segment of the input table
     * @return A list of numbers from the input tables
     */
    private static Function<String, List<Long>> getGetNumbers(Function<String, String> linePreprocessor) {
        return line -> {
            var numbersPart = line.split(":")[1];
            numbersPart = linePreprocessor.apply(numbersPart);
            return Arrays.stream(numbersPart.split("\\s+"))
                    .filter(element -> !element.isEmpty())
                    .map(Long::parseLong)
                    .toList();
        };
    }

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(288));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.of(2269432));

        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution2, Optional.of(71503));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.of(35865985));
    }

    private record Race(Long time, Long distance) {}
}
