package net.tonick.aoc2023.day07;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main implements Runnable {
    private static final String cardValues = "23456789TJQKA";
    private static final String cardValues2 = "J23456789TQKA";
    private static final Function<String, Row> rowFromString = input -> {
        String[] split = input.split(" ");
        return new Row(new Hand(split[0]), new Hand(split[0]), Integer.valueOf(split[1]));
    };

    private static final Comparator<Row> sortCards(String cardValues) {
        return (o1, o2) -> {
            var result1 = o1.hand.value() - o2.hand.value();
            if (result1 == 0) {
                var cards1 = o1.original.cards.chars().map(c -> cardValues.indexOf(c) + 1).toArray();
                var cards2 = o2.original.cards.chars().map(c -> cardValues.indexOf(c) + 1).toArray();

                return IntStream.range(0, cards1.length)
                        .map(i -> cards1[i] - cards2[i])
                        .filter(r -> r != 0)
                        .findFirst().orElse(0);
            }
            return result1;
        };
    }

    private static final Function<List<String>, Object> solution1 = input -> {
        var result = input.stream()
                .map(rowFromString)
                .sorted(sortCards(cardValues))
                .toList();

        long sum = IntStream.range(0, result.size()).mapToLong(i -> result.get(i).bid * (i + 1)).sum();

        return sum;
    };

    private static final Function<Row, Row> optimizeJokers = row -> {
        if (row.hand.cards.contains("J")) {
            var best = IntStream.range(1, cardValues2.length())
                    .mapToObj(i -> {
                        return new Hand(row.hand.cards.replaceAll("J", cardValues2.substring(i, i + 1)));
                    })
                    .max(Comparator.comparingInt(Hand::value))
                    .get();

            return new Row(row.original, best, row.bid);
        }
        return row;
    };

    private static final Function<List<String>, Object> solution2 = input -> {
        var result = input.stream()
                .map(rowFromString)
                .map(optimizeJokers)
                .sorted(sortCards(cardValues2))
                .toList();

        long sum = IntStream.range(0, result.size()).mapToLong(i -> result.get(i).bid * (i + 1)).sum();

        return sum;
    };

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(
                InputFile.of(Main.class, "sample.txt"),
                solution1,
                Optional.of(6440L)
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution1,
                Optional.of(249748283L)
        );

        Solver.solve(
                InputFile.of(Main.class, "sample.txt"),
                solution2,
                Optional.of(5905L)
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution2,
                Optional.empty()
        );
    }

    private static record Hand(String cards) {
        public int value() {
            Map<Integer, Long> groups = cards.chars().boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            var maxNumbersInGroup = groups.values().stream().mapToLong(c -> c).max().getAsLong();
            var minNumbersInGroup = groups.values().stream().mapToLong(c -> c).min().getAsLong();

            if (maxNumbersInGroup == 5) {
                return 6;
            } else if (maxNumbersInGroup  == 4) {
                return 5;
            } else if (maxNumbersInGroup == 3 && minNumbersInGroup == 2) {
                return 4;
            } else if (maxNumbersInGroup == 3) {
                return 3;
            } else if (maxNumbersInGroup == 2 && groups.size() == 3) {
                return 2;
            } else if (maxNumbersInGroup == 2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static record Row(Hand original, Hand hand, Integer bid) {
        public int value() {
            return hand.value();
        }
    }
}
