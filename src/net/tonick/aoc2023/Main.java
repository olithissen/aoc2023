package net.tonick.aoc2023;

import java.util.List;

public class Main {
    public static void main(String... args) {
        List.of(
                        new net.tonick.aoc2023.day01.Main(),
                        new net.tonick.aoc2023.day02.Main(),
                        new net.tonick.aoc2023.day03.Main())
                .forEach(runnable -> {
                    System.out.println(runnable.getClass());
                    runnable.run();
                });
    }
}
