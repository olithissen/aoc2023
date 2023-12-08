package net.tonick.aoc2023.day02;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Main implements Runnable {
    private static final Function<String, Draw> createDraw = line -> {
        Map<String, Integer> x = Arrays.stream(line.split(","))
                .map(String::trim)
                .map(s -> s.split(" "))
                .collect(Collectors.toMap(strings -> strings[1], strings -> Integer.valueOf(strings[0])));
        return new Draw(
                Optional.ofNullable(x.get("red")).orElse(0),
                Optional.ofNullable(x.get("green")).orElse(0),
                Optional.ofNullable(x.get("blue")).orElse(0));
    };
    private static final Function<String, List<Draw>> createDraws = line -> {
        var draws = line.substring(line.indexOf(":") + 1).split(";");
        var x = Arrays.stream(draws).map(createDraw).toList();
        return x;
    };
    private static final Function<String, Game> createGame = line -> {
        var gameId = line.substring(line.indexOf(" ") + 1, line.indexOf(":"));
        var draws = createDraws.apply(line);
        return new Game(Integer.parseInt(gameId), draws);
    };
    private static final Predicate<Game> validateGame =
            game -> game.draws.stream().allMatch(draw -> draw.red <= 12 && draw.green <= 13 && draw.blue <= 14);
    /**
     * Solution 1
     */
    private static final Function<List<String>, Object> solution1 = input -> input.stream()
            .map(createGame)
            .filter(validateGame)
            .mapToInt(game -> game.id)
            .sum();
    private static final Function<Game, Draw> createMetaDraw = game -> {
        var maxRed = game.draws.stream().mapToInt((Draw d) -> d.red).max().orElse(0);
        var maxGreen = game.draws.stream().mapToInt((Draw d) -> d.green).max().orElse(0);
        var maxBlue = game.draws.stream().mapToInt((Draw d) -> d.blue).max().orElse(0);

        return new Draw(maxRed, maxGreen, maxBlue);
    };
    private static final Function<Game, Draw> createMetaDraw2 =
            game -> game.draws.stream().reduce(Draw.EMPTY, (acc, draw) -> {
                int maxRed = Math.max(acc.red, draw.red);
                int maxGreen = Math.max(acc.green, draw.green);
                int maxBlue = Math.max(acc.blue, draw.blue);
                return new Draw(maxRed, maxGreen, maxBlue);
            });
    private static final ToIntFunction<Draw> calculateChecksum = draw -> draw.red * draw.green * draw.blue;
    /**
     * Solution 2
     */
    private static final Function<List<String>, Object> solution2 = input -> input.stream()
            .map(createGame)
            .map(createMetaDraw)
            .mapToInt(calculateChecksum)
            .sum();

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(8));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.of(2476));

        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution2, Optional.of(2286));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.of(54911));
    }

    private record Game(int id, List<Draw> draws) {}

    private record Draw(int red, int green, int blue) {

        public static final Draw EMPTY = new Draw(0, 0, 0);
    }
}
