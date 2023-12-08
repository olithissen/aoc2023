package net.tonick.aoc2023.day05;

import net.tonick.aoc2023.util.InputFile;
import net.tonick.aoc2023.util.Solver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main implements Runnable {
    private static final Function<String, List<Long>> seedExtractor = string -> {
        String[] split = string.split(":");
        List<Long> list = Arrays.stream(split[1].split(" ")).filter(s -> !s.isEmpty()).map(Long::parseLong).toList();
        return list;
    };

    private static final Function<String, List<SeedRange>> rangedSeedExtractor = string -> {
        String[] split = string.split(": ");
        var ranges = split[1].split(" ");

        var seedRanges = IntStream.range(0, ranges.length)
                .filter(i -> i % 2 == 0)
                .mapToObj(i -> new SeedRange(Long.parseLong(ranges[i]), Long.parseLong(ranges[i + 1])))
                .toList();

        return seedRanges;
    };
    private static final Function<String, Direction> directionExtractor = string -> {
        String[] split = string.split(" ")[0].split("-");
        return new Direction(split[0], split[2]);
    };
    private static final Function<String, LookupMap> mapExtractor = string -> {
        String[] split = string.split(":");
        List<MapEntry> mapEntries = Arrays.stream(split[1].split("\n")).filter(s -> !s.isEmpty()).map(line -> {
            String[] entries = line.split(" ");
            return new MapEntry(Long.parseLong(entries[0]), Long.parseLong(entries[1]), Long.parseLong(entries[2]));
        }).toList();

        Direction direction = directionExtractor.apply(split[0]);

        return new LookupMap(direction, mapEntries);
    };
    private static final Function<List<String>, Object> solution2 = input -> {
        String collect = String.join("\n", input);
        String[] split = collect.split("\n\n");

        var seeds = rangedSeedExtractor.apply(split[0]);
        Map<Direction, LookupMap> lookup = Arrays.stream(split, 1, split.length).map(mapExtractor)
                .collect(Collectors.toMap(LookupMap::direction, Function.identity()));

        var result = LongStream.iterate(0, i -> i + 1)
                .map(location -> {
                    Long unwalked = unwalk(lookup, "location", location);
                    Optional<SeedRange> first = seeds.stream().filter(s -> s.isInSeedRange(unwalked)).findFirst();
                    return first.isPresent() ? location : 0;
                })
                .filter(l -> l > 0)
                .findFirst();

        return result.getAsLong();
    };

    private static final Function<List<String>, Object> solution2_1 = input -> {
        String collect = String.join("\n", input);
        String[] split = collect.split("\n\n");

        var seeds = rangedSeedExtractor.apply(split[0]);
        Map<Direction, LookupMap> lookup = Arrays.stream(split, 1, split.length).map(mapExtractor).collect(Collectors.toMap(LookupMap::direction, Function.identity()));

        var result = seeds.stream()
                .flatMapToLong(s -> LongStream.range(s.from, s.from + s.range()))
                .parallel()
                .map(seed -> walk(lookup, "seed", seed))
                .min();

        return result.getAsLong();
    };
    private static final Function<List<String>, Object> solution1 = input -> {
        String collect = String.join("\n", input);
        String[] split = collect.split("\n\n");

        var seeds = seedExtractor.apply(split[0]);
        Map<Direction, LookupMap> lookup = Arrays.stream(split, 1, split.length).map(mapExtractor).collect(Collectors.toMap(LookupMap::direction, Function.identity()));

        var result = seeds.stream().map(seed -> walk(lookup, "seed", seed)).mapToLong(x -> x).min();

        return result.getAsLong();
    };

    private static Long walk(Map<Direction, LookupMap> lookup, String from, Long value) {
        Optional<LookupMap> lookupMapO = lookup.values().stream().filter(v -> v.direction.from.equals(from)).findFirst();
        var lookupMap = lookupMapO.get();
        if (lookupMap.direction.to.equals("location")) {
            return lookupMap.map(value);
        }
        return walk(lookup, lookupMap.direction.to, lookupMap.map(value));
    }

    private static Long unwalk(Map<Direction, LookupMap> lookup, String to, Long value) {
        Optional<LookupMap> lookupMapO = lookup.values().stream().filter(v -> v.direction.to.equals(to)).findFirst();
        var lookupMap = lookupMapO.get();
        if (lookupMap.direction.from.equals("seed")) {
            return lookupMap.unmap(value);
        }
        return unwalk(lookup, lookupMap.direction.from, lookupMap.unmap(value));
    }

    public static void main(String... args) {
        new Main().run();
    }

    @Override
    public void run() {
        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution1, Optional.of(35L));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution1, Optional.of(282277027L));

        Solver.solve(InputFile.of(Main.class, "sample.txt"), solution2, Optional.of(46L));

        Solver.solve(InputFile.of(Main.class, "input.txt"), solution2, Optional.of(11554135L));
    }

    private record MapEntry(long destinationRangeStart, long sourceRangeStart, long length) {
        public Optional<Long> map(Long input) {
            if (input >= sourceRangeStart && input <= sourceRangeStart + length) {
                return Optional.of(input - sourceRangeStart + destinationRangeStart);
            }
            return Optional.empty();
        }

        public Optional<Long> unmap(Long input) {
            var k = -destinationRangeStart + sourceRangeStart + input;
            if (k >= sourceRangeStart && k <= sourceRangeStart + length) {
                return Optional.of(k);
            }
            return Optional.empty();
        }

    }

    private record LookupMap(Direction direction, List<MapEntry> entries) {
        public Long map(Long input) {
            var ranges = entries.stream().map(e -> e.map(input)).filter(Optional::isPresent).findFirst();
            return ranges.map(Optional::get).orElse(input);
        }

        public Long unmap(Long input) {
            var ranges = entries.stream().map(e -> e.unmap(input)).filter(Optional::isPresent).findFirst();
            return ranges.map(Optional::get).orElse(input);
        }
    }

    private record Direction(String from, String to) {
    }

    private record SeedRange(Long from, Long range) {
        public boolean isInSeedRange(Long value) {
            return value >= from && value <= from + range;
        }
    }
}
