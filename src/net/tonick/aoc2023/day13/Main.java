package net.tonick.aoc2023.day13;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class Main implements Runnable {
    private static final Function<List<String>, List<String>> transpose = (inputList) -> {
        List<String> outputList = new ArrayList<>();
        int maxLength = inputList.get(0).length();

        for (int i = 0; i < maxLength; i++) {
            StringBuilder sb = new StringBuilder(inputList.size());
            for (String s : inputList) {
                if (i < s.length()) {
                    sb.append(s.charAt(i));
                }
            }
            outputList.add(sb.toString());
        }

        return outputList;
    };
    private static final Function<List<String>, List<MirrorField>> extractMirrorFields = input -> {
        return Arrays.stream(String.join("\n", input).split("\n\n"))
                .map(block -> Arrays.stream(block.split("\n")).toList())
                .map(block -> new MirrorField(block, transpose.apply(block)))
                .toList();
    };

    private static final ToIntFunction<List<String>> solveList = field -> {
        return 0;
    };

    private static final Function<MirrorField, Result> solveField = field -> {
        var byRow = solveList.applyAsInt(field.rows);
        if (byRow > 0) return new Result(field, false, byRow);
        var byCol = solveList.applyAsInt(field.rowsTransposed);
        return new Result(field, true, 100 * byCol);
    };

    private static final Function<List<String>, Object> solution1 = input -> {
        var mirrorFields = extractMirrorFields.apply(input);
        return mirrorFields.stream()
                .map(solveField)
                .mapToInt(Result::score)
                .sum();
    };
    private static final Function<List<String>, Object> solution2 = input -> input;

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(405));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.empty());

        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution2, Optional.empty());

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.empty());
    }

    private record MirrorField(List<String> rows, List<String> rowsTransposed) {
    }
    private record Result(MirrorField field, boolean vertical, Integer score) {
    }
}
