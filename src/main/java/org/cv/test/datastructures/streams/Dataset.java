package org.cv.test.datastructures.streams;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Dataset<T> {

    interface Generator<T> {
        void generate(Consumer<T> consumer);
    }

    private final Generator<T> generator;

    private Dataset(Generator<T> generator) {
        this.generator = generator;
    }

    public void forEach(Consumer<T> consumer) {
        generator.generate(consumer);
    }

    public static <T> Dataset<T> of(Collection<T> collection) {
        return new Dataset<>(collection::forEach);
    }

    public Dataset<T> union(Collection<T> collection) {
        return new Dataset<>(consumer -> {
            generator.generate(consumer);
            collection.forEach(consumer);
        });
    }

    public Dataset<T> filter(Predicate<T> predicate) {
        return new Dataset<>(consumer -> generator.generate(value -> {
            if (predicate.test(value)) {
                consumer.accept(value);
            }
        }));
    }

    public <R> Dataset<R> map(Function<T, R> function) {
        return new Dataset<>(consumer -> generator.generate(
                value -> consumer.accept(function.apply(value))
        ));
    }


    public static void main(String[] args) {
        Dataset.of(Arrays.asList("шла", "саша", "по", "шоссе"))
                .union(Arrays.asList("и", "сосала", "сушку"))
                .filter(s -> s.length() > 4)
                .map(s -> s + ", length=" + s.length())
                .forEach(System.out::println);
    }
}
