package net.tonick.aoc2023.day03;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Main implements Runnable {
    private record Position(int x1, int y1, int x2, int y2, String value) {
        public boolean intersects(Position other) {
            return x2 > other.x1 && other.x2 > x1 && y2 > other.y1 && other.y2 > y1;
        }
    };
    private static Function<List<String>, List<Position>> getStringPositions(String pattern)  {
        Pattern p = Pattern.compile(pattern);

        return input -> IntStream.range(0, input.size())
                .mapToObj(idx -> {
                    var line = input.get(idx);
                    Matcher m = p.matcher(line);
                    List<Position> hits = new ArrayList<>();
                    while (m.find()) {
                        hits.add(new Position(m.start(), idx, m.end() - 1, idx, m.group()));
                    }
                    return hits;
                })
                .flatMap(Collection::stream)
                .toList();
    }

    private static final Function<List<String>, List<Position>> extractNumberPositions = getStringPositions("\\d+");
    private static final Function<List<String>, List<Position>> extractSymbolPositions = getStringPositions("[^\\d\\.]+");
    private static final Function<List<String>, List<Position>> extractGearPositions = getStringPositions("\\*");
    private static final Function<Position, Position> extendRange = position -> new Position(
            position.x1 - 1,
            position.y1 - 1,
            position.x2 + 1,
            position.y1 + 1,
            position.value
    );

    /**
     * Solution 1: Apply the 'extractOnlyByDigits' extractor to the input list and sum up the results
     */
    private static final Function<List<String>, Object> solution1 = input -> {
        List<Position> numbers = extractNumberPositions.apply(input).stream().map(extendRange).toList();
        List<Position> symbols = extractSymbolPositions.apply(input).stream().map(extendRange).toList();

        return numbers.stream()
                .filter(number -> symbols.stream().anyMatch(number::intersects))
                .map(Position::value)
                .mapToInt(Integer::parseInt)
                .sum();
    };

    private static final Function<List<String>, Object> solution2 = input -> {
        List<Position> numbers = extractNumberPositions.apply(input).stream().map(extendRange).toList();
        List<Position> gears = extractGearPositions.apply(input).stream().map(extendRange).toList();

        return gears.stream()
                .mapToInt(number -> {
                    var hits = numbers.stream().filter(number::intersects).toList();
                    return hits.size() == 2 ? Integer.parseInt(hits.get(0).value) * Integer.parseInt(hits.get(1).value) : 0;
                })
                .sum();
    };

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(
                InputFile.of(Main.class, "sample.txt"),
                solution1,
                Optional.of(4361)
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution1,
                Optional.of(527364)
        );

        Solver.solve(
                InputFile.of(Main.class, "sample.txt"),
                solution2,
                Optional.of(467835)
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution2,
                Optional.of(79026871)
        );
    }
}
