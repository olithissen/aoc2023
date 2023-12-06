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
    private static final Function<String, List<Long>> getNumbersFromTable = getGetNumbersFromTableWithPreprocessor(
            Function.identity());
    private static final Function<String, List<Long>> getNumbersFromTableWithBadKerning = getGetNumbersFromTableWithPreprocessor(
            s -> s.replaceAll("\\s", ""));
    private static final Function<List<String>, List<Race>> getRacesAsTable = getRaces(getNumbersFromTable);
    private static final Function<List<String>, List<Race>> getRacesWithBadKerning = getRaces(
            getNumbersFromTableWithBadKerning);
    private static final BiFunction<Long, Long, Long> loadTime = (holdTime, raceTime) -> holdTime * (raceTime - holdTime);
    private static final Function<Race, List<Long>> loadTimes = race -> {
        return LongStream.range(0, race.time).map(i -> loadTime.apply(i,
                                                                      race.time
        )).filter(distance -> distance > race.distance).boxed().toList();
    };
    private static final IntBinaryOperator product = (acc, cur) -> acc > 0 ? acc * cur : cur;
    private static final Function<List<String>, Object> solution1 = input -> {
        var races = getRacesAsTable.apply(input);
        return races.stream().map(loadTimes).mapToInt(List::size).reduce(product);
    };
    private static final Function<List<String>, Object> solution2 = input -> {
        var races = getRacesWithBadKerning.apply(input);
        return races.stream().map(loadTimes).mapToInt(List::size).reduce(product);
    };

    private static Function<List<String>, List<Race>> getRaces(Function<String, List<Long>> tableReader) {
        return input -> {
            var s = input.stream().map(tableReader).toList();

            return IntStream.range(0, s.get(0).size()).mapToObj(i -> new Race(s.get(0).get(i),
                                                                              s.get(1).get(i)
            )).toList();
        };
    }

    private static Function<String, List<Long>> getGetNumbersFromTableWithPreprocessor(Function<String, String> linePreprocessor) {
        return line -> {
            var numbersPart = line.split(":")[1];
            numbersPart = linePreprocessor.apply(numbersPart);
            return Arrays.stream(numbersPart.split("\\s+")).filter(element -> !element.isEmpty()).map(Long::parseLong).toList();
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

    private record Race(Long time, Long distance) {
    }
}
