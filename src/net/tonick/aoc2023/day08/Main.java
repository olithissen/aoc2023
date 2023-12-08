package net.tonick.aoc2023.day08;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Main implements Runnable {
    private static final Function<List<String>, Object> solution1 = input -> input;
    private static final Function<List<String>, Object> solution2 = input -> input;

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(
                InputFile.of(Main.class, "sample.txt"),
                solution1,
                Optional.empty()
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution1,
                Optional.empty()
        );

        Solver.solve(
                InputFile.of(Main.class, "sample.txt"),
                solution2,
                Optional.empty()
        );

        Solver.solve(
                InputFile.of(Main.class, "input.txt"),
                solution2,
                Optional.empty()
        );
    }
}
