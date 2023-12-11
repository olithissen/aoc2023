package net.tonick.aoc2023.day04;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main implements Runnable {
    private static final Function<String, Integer> getCardId =
            string -> Integer.valueOf(string.replaceAll("Card", "").trim());
    private static final Function<String, List<Integer>> splitNumbers = string -> Arrays.stream(string.split(" "))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Integer::valueOf)
            .toList();
    private static final Function<String, Card> extractCards = line -> {
        var elements = line.split(":");
        var numbers = elements[1].split("\\|");

        return new Card(
                getCardId.apply(elements[0]), splitNumbers.apply(numbers[0]), splitNumbers.apply(numbers[1]), 0);
    };
    private static final ToIntFunction<Card> countIntersections = card -> {
        return (int)
                card.ownNumbers.stream().filter(card.winningNumbers::contains).count();
    };
    private static final IntUnaryOperator calculateValue = operand -> (int) Math.pow(2, operand - 1);
    private static final Function<List<String>, Object> solution1 = input -> {
        return input.stream()
                .map(extractCards)
                .mapToInt(countIntersections)
                .map(calculateValue)
                .sum();
    };
    private static final Predicate<Card> hasWins = card -> card.wins > 0;
    private static final Function<List<String>, Object> solution2 = input -> {
        var cardInventory = input.stream()
                .map(extractCards)
                .map(card ->
                        new Card(card.id, card.winningNumbers, card.ownNumbers, countIntersections.applyAsInt(card)))
                .collect(Collectors.toMap(Card::id, Function.identity()));

        List<Card> process = cardInventory.values().stream().toList();

        do {
            process = process.stream()
                    .flatMap(card -> {
                        var newOriginal = new Card(card.id, card.winningNumbers, card.ownNumbers, 0);
                        var copies = IntStream.rangeClosed(card.id + 1, card.id + card.wins)
                                .mapToObj(cardInventory::get)
                                .toList();
                        return Stream.concat(Stream.of(newOriginal), copies.stream());
                    })
                    .toList();
        } while (process.stream().anyMatch(hasWins));

        return process.size();
    };

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(13));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.of(25004));

        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution2, Optional.of(30));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.empty());
    }

    record Card(Integer id, List<Integer> winningNumbers, List<Integer> ownNumbers, Integer wins) {}
}
