package org.cv.test.functional.optics;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ProOpticTC {

    static void main(final String[] args) {

        final Lens<Pair<String, Optional<Pair<Integer, Boolean>>>, Integer>
                lens = Pair.<String, Optional<Pair<Integer, Boolean>>>
                lens2().compose(Lens.optional()).compose(Pair.lens1());

        final Pair<String, Optional<Pair<Integer, Boolean>>> pairs =
                Pair.of("!", Optional.of(Pair.of(2, true)));

        System.out.println(lens.apply(x -> x + 1).apply(pairs));
        // (!,Optional[(3,true)])

        System.out.println(lens.get().apply(pairs));
        // Right(2)

        System.out.println(lens.get().apply(Pair.of("?", Optional.empty())));
        // Left(Optional.empty)

    }

    interface Kind2<F, A, B> {
        F unkind();
    }

    interface Profunctor<P> {
        <A, B, C, D> Kind2<P, C, D> dimap(Kind2<P, A, B> h, Function<C, A> f, Function<B, D> g);

        <A, B, C> Kind2<P, Pair<A, C>, Pair<B, C>> first(Kind2<P, A, B> h);

        <A, B, C> Kind2<P, Either<C, A>, Either<C, B>> right(Kind2<P, A, B> h);
    }

    interface ProOptic<P, S, T, A, B> extends Function<Kind2<P, A, B>, Kind2<P, S, T>> {

        default <C, D> ProOptic<P, S, T, C, D> compose(final ProOptic<P, A, B, C, D> f) {
            return g -> apply(f.apply(g));
        }

        static <P, S, T, A, B> ProOptic<P, S, T, A, B> iso(final Profunctor<P> p,
                                                           final Function<S, A> from,
                                                           final Function<B, T> to) {
            return h -> p.dimap(h, from, to);
        }

        static <P, S, T, A, B> ProOptic<P, S, T, A, B> lens(final Profunctor<P> p,
                                                            final Function<S, A> get,
                                                            final BiFunction<S, B, T> set) {
            return h -> p.dimap(p.first(h), s -> Pair.of(get.apply(s), s), bs -> set.apply(bs._2, bs._1));
        }

        static <P, S, T, A, B> ProOptic<P, S, T, A, B> prism(final Profunctor<P> p,
                                                             final Function<S, Either<T, A>> get,
                                                             final Function<B, T> set) {
            return h -> p.dimap(p.right(h), get, e -> e.match(Function.identity(), set));
        }
    }

    interface Optic<S, T, A, B> {

        <P> ProOptic<P, S, T, A, B> apply(Profunctor<P> p);

        default Function<S, T> apply(final Function<A, B> f) {
            return apply(Fun.profunctor).apply(Fun.of(f)).unkind();
        }

        default Function<S, A> get() {
            return apply(Forget.forget()).apply(Forget.identity()).unkind();
        }

        default <C, D> Optic<S, T, C, D> compose(final Optic<A, B, C, D> f) {
            return new Optic<S, T, C, D>() {
                @Override
                public <P> ProOptic<P, S, T, C, D> apply(final Profunctor<P> p) {
                    return Optic.this.apply(p).compose(f.apply(p));
                }
            };
        }

        static <S, T, A, B> Optic<S, T, A, B> iso(final Function<S, A> from,
                                                  final Function<B, T> to) {
            return new Optic<S, T, A, B>() {
                @Override
                public <P> ProOptic<P, S, T, A, B> apply(final Profunctor<P> p) {
                    return ProOptic.iso(p, from, to);
                }
            };
        }

        static <S, T, A, B> Optic<S, T, A, B> lens(final Function<S, A> get,
                                                   final BiFunction<S, B, T> set) {
            return new Optic<S, T, A, B>() {
                @Override
                public <P> ProOptic<P, S, T, A, B> apply(final Profunctor<P> p) {
                    return ProOptic.lens(p, get, set);
                }
            };
        }

        static <S, T, A, B> Optic<S, T, A, B> prism(final Function<S, Either<T, A>> get,
                                                    final Function<B, T> set) {
            return new Optic<S, T, A, B>() {
                @Override
                public <P> ProOptic<P, S, T, A, B> apply(final Profunctor<P> p) {
                    return ProOptic.prism(p, get, set);
                }
            };
        }
    }

    interface Lens<S, A> extends Optic<S, S, A, A> {

        default <B> Lens<S, B> compose(final Lens<A, B> h) {
            return new Lens<S, B>() {
                @Override
                public <P> ProOptic<P, S, S, B, B> apply(final Profunctor<P> p) {
                    return Lens.this.apply(p).compose(h.apply(p));
                }
            };
        }

        static <A> Lens<Optional<A>, A> optional() {
            return prism(Function.identity(), Optional::ofNullable);
        }

        static <S, A> Lens<S, A> of(final Function<S, A> get, final BiFunction<S, A, S> set) {
            return new Lens<S, A>() {
                @Override
                public <P> ProOptic<P, S, S, A, A> apply(final Profunctor<P> p) {
                    return ProOptic.lens(p, get, set);
                }
            };
        }

        static <S, T> Lens<S, T> iso(final Function<S, T> from, final Function<T, S> to) {
            return new Lens<S, T>() {
                @Override
                public <P> ProOptic<P, S, S, T, T> apply(final Profunctor<P> p) {
                    return ProOptic.iso(p, from, to);
                }
            };
        }

        static <S, A> Lens<S, A> prism(final Function<S, Optional<A>> get, final Function<A, S> set) {
            return new Lens<S, A>() {
                @Override
                public <P> ProOptic<P, S, S, A, A> apply(final Profunctor<P> p) {
                    return ProOptic.prism(p, (S s) -> get.apply(s)
                            .map(Either::<S, A>right).orElse(Either.left(s)), set);
                }
            };
        }
    }

    interface Forget<R, A, B> extends Function<A, R>, Kind2<Forget, A, B> {

        static <R, A, B> Forget<R, A, B> of(final Function<A, R> f) {
            return a -> f.apply(a);
        }

        static <A, B> Forget<A, A, B> identity() {
            return a -> a;
        }

        static <A, B> Function<A, B> of(final Kind2<Forget, A, B> f) {
            return f.unkind();
        }


        @Override
        default Forget<R, A, B> unkind() {
            return this;
        }

        static <R> Profunctor<Forget> forget() {
            return new Profunctor<Forget>() {
                @Override
                public <A, B, C, D> Kind2<Forget, C, D> dimap(final Kind2<Forget, A, B> k,
                                                              final Function<C, A> f,
                                                              final Function<B, D> g) {
                    return Forget.of(Forget.of(k).compose(f));
                }

                @Override
                public <A, B, C> Kind2<Forget, Pair<A, C>, Pair<B, C>> first(final Kind2<Forget, A, B> k) {
                    return Forget.of(p -> Forget.<A, B>of(k).apply(p._1));
                }

                @Override
                public <A, B, C> Kind2<Forget, Either<C, A>, Either<C, B>> right(final Kind2<Forget, A, B> h) {
                    return Forget.of(e -> e.match(Either::left, a -> Either.right(Forget.<A, B>of(h).apply(a))));
                }
            };
        }
    }

    interface Fun<A, B> extends Kind2<Fun, A, B>, Function<A, B> {

        @Override
        default Fun<A, B> unkind() {
            return this;
        }

        static <A, B> Fun<A, B> of(final Function<A, B> f) {
            return a -> f.apply(a);
        }

        static <A, B> Function<A, B> of(final Kind2<Fun, A, B> f) {
            return f.unkind();
        }

        Profunctor<Fun> profunctor = new Profunctor<Fun>() {
            @Override
            public <A, B, C, D> Kind2<Fun, C, D> dimap(final Kind2<Fun, A, B> h,
                                                       final Function<C, A> f,
                                                       final Function<B, D> g) {
                return Fun.of(g.compose(Fun.<A, B>of(h).compose(f)));
            }

            @Override
            public <A, B, C> Kind2<Fun, Pair<A, C>, Pair<B, C>> first(final Kind2<Fun, A, B> h) {
                return Fun.of(p -> Pair.<B, C>of(Fun.<A, B>of(h).apply(p._1), p._2));
            }

            @Override
            public <A, B, C> Kind2<Fun, Either<C, A>, Either<C, B>> right(final Kind2<Fun, A, B> h) {
                return Fun.of(e -> e.match(Either::left, a -> Either.right(Fun.<A, B>of(h).apply(a))));
            }
        };
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
                public String toString() {
                    return "Left(" + a + ")";
                }

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
                public String toString() {
                    return "Right(" + b + ")";
                }

                @Override
                public <C> C match(final Function<A, C> left,
                                   final Function<B, C> right) {
                    return right.apply(b);
                }
            };
        }
    }

}