package net.tonick.aoc2023.day10;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main implements Runnable {
    private static final BiFunction<List<String>, Position, Integer> positionToStreamPosition = (input, position) -> {
        return position.y * input.get(0).length() + position.x;
    };
    private static final BiFunction<List<String>, Integer, Position> streamPositionToPosition =
            (input, streamPosition) -> {
                var y = (int) streamPosition / input.get(0).length();
                var x = streamPosition - y * input.get(0).length();
                return new Position(x, y);
            };
    private static final Function<String, List<Position>> typeToRelativePositions = (input) -> {
        switch (input) {
            case "|":
                return List.of(Position.TOP, Position.BOTTOM);
            case "-":
                return List.of(Position.LEFT, Position.RIGHT);
            case "L":
                return List.of(Position.TOP, Position.RIGHT);
            case "J":
                return List.of(Position.TOP, Position.LEFT);
            case "7":
                return List.of(Position.LEFT, Position.BOTTOM);
            case "F":
                return List.of(Position.RIGHT, Position.BOTTOM);
            default:
                return List.of();
        }
    };
    private static final Function<List<String>, Stack<Position>> walkPolygon = input -> {
        int width = input.get(0).length();
        int height = input.size();
        String[][] grid = new String[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[y][x] = input.get(y).substring(x, x + 1);
            }
        }

        var stream = String.join("", input);
        var startPosition = streamPositionToPosition.apply(input, stream.indexOf("S"));

        var next = Stream.of(Position.LEFT, Position.RIGHT, Position.TOP, Position.BOTTOM)
                .map(position -> position.add(startPosition))
                .filter(newPosition -> newPosition.x >= 0 && newPosition.y >= 0)
                .filter(newPosition -> typeToRelativePositions.apply(grid[newPosition.y][newPosition.x]).stream()
                        .map(p -> p.add(newPosition))
                        .anyMatch(p -> p.equals(startPosition)))
                .findAny()
                .get();

        Stack<Position> visited = new Stack<>();
        visited.push(startPosition);

        while (true) {
            var type = grid[next.y][next.x];
            visited.push(next);
            Position finalNext = next;
            Optional<Position> nextCandidate = typeToRelativePositions.apply(type).stream()
                    .map(p -> p.add(finalNext))
                    .filter(p -> !visited.contains(p))
                    .findFirst();

            if (nextCandidate.isPresent()) {
                next = nextCandidate.get();
            } else {
                break;
            }
        }

        return visited;
    };
    private static final Function<List<String>, Object> solution1 = input -> {
        var visited = walkPolygon.apply(input);
        return (int) (visited.size() / 2);
    };
    private static final Function<List<String>, Object> solution2 = input -> {
        var visited = walkPolygon.apply(input);
        var stream = String.join("", input);

        var insidePoly = IntStream.range(0, stream.length())
                .mapToObj(i -> streamPositionToPosition.apply(input, i))
                .filter(pos -> !visited.contains(pos))
                .filter(pos -> isInsidePolygon(visited.stream().toList(), pos))
                .count();

        return insidePoly;
    };

    public static boolean isInsidePolygon(List<Position> path, Position point) {
        int crossings = 0;
        int pathSize = path.size();

        for (int startPosition = 0; startPosition < pathSize; startPosition++) {
            int endPosition = (startPosition + 1) % pathSize;

            Position p1 = path.get(startPosition);
            Position p2 = path.get(endPosition);

            if (p1.y != p2.y
                    && ((p1.y > point.y) != (p2.y > point.y))
                    && (point.x < (p2.x - p1.x) * (point.y - p1.y) / (p2.y - p1.y) + p1.x)) {
                crossings++;
            }
        }

        return crossings % 2 != 0;
    }

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(8));

        Solver.solve(InputFile.of(Main.class, "sample3.txt"), solution1, Optional.of(2));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.of(6875));

        Solver.solve(InputFile.of(Main.class, "sample2.txt"), solution2, Optional.empty());

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.empty());
    }

    private record Position(Integer x, Integer y) {
        static Position EMPTY = new Position(0, 0);
        static Position TOP = new Position(0, -1);
        static Position BOTTOM = new Position(0, 1);
        static Position LEFT = new Position(-1, 0);
        static Position RIGHT = new Position(1, 0);

        Position add(Position relative) {
            return new Position(this.x + relative.x, this.y + relative.y);
        }
    }
}
