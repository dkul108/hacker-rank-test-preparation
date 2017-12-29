package org.cv.test.functional.optics;


import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

// Profunctor Optics
// http://programming-journal.org/2017/1/7/
public interface ProOptic {

    static void main(final String[] args) {
        System.out.println(Pair.<String, Optional<Pair<Integer, Boolean>>>
                lens2().compose(Prism.optional()).compose(Pair.lens1())
                .apply(Profunctor.of(x -> x + 1))
                .apply(Pair.of("!", Optional.of(Pair.of(2, true))))
        );// (!,Optional[(3,true)])
    }

    interface Profunctor<A, B> {
        B apply(A a);

        <C, D> Profunctor<C, D> dimap(Function<C, A> f, Function<B, D> g);

        <C> Profunctor<Pair<A, C>, Pair<B, C>> first();

        <C> Profunctor<Either<C, A>, Either<C, B>> right();

        static <A, B> Profunctor<A, B> of(final Function<A, B> h) {
            return new Profunctor<A, B>() {
                @Override
                public B apply(A a) {
                    return h.apply(a);
                }

                @Override
                public <C, D> Profunctor<C, D> dimap(final Function<C, A> f,
                                                     final Function<B, D> g) {
                    return of(g.compose(h.compose(f)));
                }

                @Override
                public <C> Profunctor<Pair<A, C>, Pair<B, C>> first() {
                    return Profunctor.<Pair<A, C>, Pair<B, C>>of(
                            p -> Pair.<B, C>of(h.apply(p._1), p._2));
                }

                @Override
                public <C> Profunctor<Either<C, A>, Either<C, B>> right() {
                    return Profunctor.<Either<C, A>, Either<C, B>>of(e ->
                            e.match(c -> Either.<C, B>left(c),
                                    a -> Either.<C, B>right(h.apply(a))));
                }
            };
        }
    }

    interface Optic<S, T, A, B> extends Function<Profunctor<A, B>, Profunctor<S, T>> {
        static <S, T, A, B> Optic<S, T, A, B> of(final Function<S, A> from, final Function<B, T> to) {
            return f -> f.dimap(from, to);
        }

        static <S, T, A, B> Optic<S, T, A, B> lens(final Function<S, A> get, final BiFunction<S, B, T> set) {
            return f -> f.<S>first().dimap(s -> Pair.of(get.apply(s), s), bs -> set.apply(bs._2, bs._1));
        }

        static <S, T, A, B> Optic<S, T, A, B> prism(final Function<S, Either<T, A>> match, final Function<B, T> build) {
            return f -> f.<T>right().dimap(match, e -> e.match(Function.identity(), build));
        }

        default <C, D> Optic<S, T, C, D> compose(final Optic<A, B, C, D> f) {
            return g -> apply(f.apply(g));
        }
    }

    interface Lens<S, A> extends Optic<S, S, A, A> {
        static <S, A> Lens<S, A> of(final Function<S, A> get,
                                    final BiFunction<S, A, S> set) {
            return f -> Optic.lens(get, set).apply(f);
        }

        default <B> Lens<S, B> compose(final Lens<A, B> f) {
            return g -> Optic.super.compose(f).apply(g);
        }
    }

    interface Prism<S, A> extends Optic<S, S, A, A> {
        static <A> Prism<Optional<A>, A> optional() {
            return of(Function.identity(), Optional::ofNullable);
        }

        static <S, A> Prism<S, A> of(final Function<S, Optional<A>> get,
                                     final Function<A, S> set) {
            return f -> Optic.prism((S s) -> get.apply(s).map(
                    Either::<S, A>right).orElse(Either.left(s)), set)
                    .apply(f);
        }

        default <B> Prism<S, B> compose(final Prism<A, B> f) {
            return g -> Optic.super.compose(f).apply(g);
        }
    }

    final class Pair<A, B> {
        public static <A, B> Lens<Pair<A, B>, A> lens1() {
            return Lens.of(p -> p._1, (p, a) -> Pair.of(a, p._2));
        }

        public static <A, B> Lens<Pair<A, B>, B> lens2() {
            return Lens.of(p -> p._2, (p, b) -> Pair.of(p._1, b));
        }

        public static <A, B> Pair<A, B> of(final A a, final B b) {
            return new Pair<>(a, b);
        }

        public final A _1;
        public final B _2;

        public Pair(final A _1, final B _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public String toString() {
            return "(" + _1 + "," + _2 + ")";
        }
    }

    interface Either<A, B> {
        <C> C match(Function<A, C> left, Function<B, C> right);

        static <A, B> Either<A, B> left(final A a) {
            return new Either<A, B>() {
                @Override
                public <C> C match(final Function<A, C> left,
                                   final Function<B, C> right) {
                    return left.apply(a);
                }
            };
        }

        static <A, B> Either<A, B> right(final B b) {
            return new Either<A, B>() {
                @Override
                public <C> C match(final Function<A, C> left,
                                   final Function<B, C> right) {
                    return right.apply(b);
                }
            };
        }
    }

}