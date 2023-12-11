package net.tonick.aoc2023;

import java.util.List;

public class Main {
    public static void main(String... args) {
        List.of(
                        new net.tonick.aoc2023.day01.Main(),
                        new net.tonick.aoc2023.day02.Main(),
                        new net.tonick.aoc2023.day03.Main(),
                        new net.tonick.aoc2023.day04.Main(),
                        new net.tonick.aoc2023.day05.Main(),
                        new net.tonick.aoc2023.day06.Main(),
                        new net.tonick.aoc2023.day07.Main(),
                        new net.tonick.aoc2023.day08.Main())
                .forEach(runnable -> {
                    System.out.println(runnable.getClass());
                    runnable.run();
                });
    }
}
