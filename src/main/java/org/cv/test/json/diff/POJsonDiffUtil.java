package org.cv.test.json.diff;


import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final public class POJsonDiffUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private POJsonDiffUtil() {
        throw new AssertionError("No instances is needed");
    }

    public static int main(final String[] args) throws IOException {
        assert args != null && args.length > 1 : "Usage: inputFile1 inputFile2 [outputFile]";
        return jsonCompare(args[0], args[1], args.length > 2 ? args[2] : null) ? 0 : 1;
    }

    private static Map<String, Map<String, List<JsonLocation>>> parseJson(final String inputFile) throws IOException {
        try (final JsonParser parser = mapper.getFactory().createParser(new File(inputFile))) {
            return parseJson(Collections.emptyMap(), parser, "").invoke();
        }
    }

    private static TailCall<Map<String, Map<String, List<JsonLocation>>>> parseJson(
            final Map<String, Map<String, List<JsonLocation>>> m,
            final JsonParser parser, final String path
    ) throws IOException {
        final JsonToken token = parser.nextValue();
        if (token == null) return done(m);
        else if (token.isStructStart() && parser.currentName() != null) {
            return () -> parseJson(m, parser, path + "/" + parser.currentName());
        } else if (token.isStructEnd() && parser.currentName() != null) {
            return () -> parseJson(m, parser, path.substring(0, path.lastIndexOf(
                    "/" + parser.currentName())));
        } else if (token.isScalarValue()) {
            return () -> parseJson(merge(m,
                    Collections.singletonMap(path + "/" + parser.currentName(),
                            Collections.singletonMap(parser.getValueAsString(),
                                    Collections.singletonList(parser.getCurrentLocation())))),
                    parser, path);
        } else return () -> parseJson(m, parser, path);
    }

    private static <K1, K2, V> Map<K1, Map<K2, List<V>>> merge(
            final Map<K1, Map<K2, List<V>>> m1, final Map<K1, Map<K2, List<V>>> m2) {
        return Stream.concat(m1.entrySet().stream(), m2.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> Stream.concat(a.entrySet().stream(), b.entrySet().stream())
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                        (x, y) -> Stream.concat(x.stream(), y.stream())
                                                .collect(Collectors.toList())))));
    }

    private static <K1, K2, V> Map<K1, Map<K2, Integer>> counts(final Map<K1, Map<K2, List<V>>> m) {
        return m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> counts2(e.getValue())));
    }

    private static <K, V> Map<K, Integer> counts2(final Map<K, List<V>> m) {
        return m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size()));
    }

    static boolean jsonCompare(final String inputFile1, final String inputFile2,
                               final String outputFile) throws IOException {
        final Map<String, Map<String, List<JsonLocation>>> index1 = parseJson(inputFile1);

        final Map<String, Map<String, List<JsonLocation>>> index2 = parseJson(inputFile2);
        final MapDifference<String, Map<String, Integer>> diff = Maps.difference(counts(index1), counts(index2));

        if (outputFile != null) System.setOut(new PrintStream(outputFile));
        System.out.println("Comparison of '" + inputFile1 + "' to '" + inputFile2 + "'");
        System.out.println("\nStatus: " + (diff.areEqual() ? "Success [equal]" : "Failure [mismatch]"));

        showDiff("Entries only in '" + inputFile1 + "'", diff.entriesOnlyOnLeft(), k -> showOnly(index1, k));
        showDiff("Entries only in '" + inputFile2 + "'", diff.entriesOnlyOnRight(), k -> showOnly(index2, k));
        showDiff("Entries differing", diff.entriesDiffering(), k -> showDiff(k, index1.get(k), index2.get(k)));

        return diff.areEqual();
    }

    private static String showOnly(Map<String, Map<String, List<JsonLocation>>> index1, String k) {
        return index1.get(k).entrySet().stream()
                .map(v -> showLoc(v.getKey(), v.getValue()))
                .collect(Collectors.toList()).toString();
    }

    private static <T> void showDiff(final String header, final Map<String, T> diff,
                                     final Function<String, String> format) {
        if (!diff.isEmpty()) {
            System.out.println("\n" + header + ":\n");
            diff.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEach(e -> System.out.println(format.apply(e.getKey())));
        }
    }

    private static String showDiff(final String path,
                                   final Map<String, List<JsonLocation>> m1,
                                   final Map<String, List<JsonLocation>> m2) {
        final MapDifference<String, Integer> diff = Maps.difference(counts2(m1), counts2(m2));
        final List<String> left = join(m1, diff, diff.entriesOnlyOnLeft().keySet(), (l, r) -> l > r);
        final List<String> right = join(m2, diff, diff.entriesOnlyOnRight().keySet(), (l, r) -> l < r);
        return (left.isEmpty() && right.isEmpty()) ? "" : (path + " (" +
                (left.isEmpty() ? " " : left) + ", " +
                (right.isEmpty() ? " " : right) + ")");
    }

    private static List<String> join(final Map<String, List<JsonLocation>> m1,
                                     final MapDifference<String, Integer> diff,
                                     final Set<String> keys,
                                     final BiFunction<Integer, Integer, Boolean> f) {
        return Stream.concat(keys.stream(),
                diff.entriesDiffering().entrySet().stream()
                        .filter(e -> f.apply(e.getValue().leftValue(),
                                e.getValue().rightValue()))
                        .map(Map.Entry::getKey))
                .sorted().map(v -> showLoc(v, m1.get(v)))
                .collect(Collectors.toList());
    }

    private static String showLoc(final String v, final List<JsonLocation> locations) {
        return "'" + v + "':" + locations.stream().map(loc -> "" + loc.getLineNr())
                .collect(Collectors.joining(","));
    }

    @FunctionalInterface
    private interface TailCall<T> {
        TailCall apply() throws IOException;

        default boolean isComplete() {
            return false;
        }

        default T result() {
            throw new Error("not implemented");
        }

        default T invoke() {
            return Stream.iterate(this, t -> {
                try {
                    return t.apply();
                } catch (IOException e) {
                    return failed(e);
                }
            })
                    .filter(TailCall::isComplete)
                    .findFirst()
                    .get()
                    .result();
        }
    }

    private static <T> TailCall<T> done(final T value) {
        return new TailCall<T>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public T result() {
                return value;
            }

            @Override
            public TailCall apply() {
                throw new Error("not implemented");
            }
        };
    }

    private static <T> TailCall<T> failed(final IOException e) {
        return new TailCall<T>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public TailCall apply() throws IOException {
                throw e;
            }
        };
    }

}