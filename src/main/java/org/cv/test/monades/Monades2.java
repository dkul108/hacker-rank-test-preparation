package org.cv.test.monades;


import java.util.function.Function;

public class Monades2 {

    interface Kind<T extends Kind<T, A>, A> {
        interface Pointed<T extends Kind<T, A>, A> {
            T point(A a);
        }

        interface Functor<A, B, F extends Kind<F, A>,
                G extends Kind<G, B>>
                extends Pointed<F, A> {
            Function<F, G> map(Function<A, B> f);
        }

        interface Monad<A, B, F extends Kind<F, A>,
                G extends Kind<G, B>>
                extends Functor<A, B, F, G> {
            Function<F, G> bind(Function<A, G> f);
        }
    }

    interface Maybe<A> extends Kind<Maybe<A>, A> {
        static <A> Maybe<A> point(A a) {
            return a == null ? Nothing : new Just(a);
        }

        <B> Maybe<B> map(Function<A, B> f);

        <B> Maybe<B> bind(Function<A, Maybe<B>> f);

        Maybe Nothing = new Maybe() {
            @Override
            public Maybe map(final Function f) {
                return Nothing;
            }

            @Override
            public Maybe bind(final Function f) {
                return Nothing;
            }
        };

        class Just<A> implements Maybe<A> {
            public A a;

            public Just(A a) {
                this.a = a;
            }

            @Override
            public <B> Maybe<B> map(final Function<A, B> f) {
                return point(f.apply(a));
            }

            @Override
            public <B> Maybe<B> bind(final Function<A, Maybe<B>> f) {
                return f.apply(a);
            }
        }

        interface Monad<A, B> extends Kind.Monad<A, B, Maybe<A>, Maybe<B>> {
        }

        Monad monad = new Monad() {
            @Override
            public Function<Maybe, Maybe> bind(final Function f) {
                return m -> m.bind(f);
            }

            @Override
            public Function<Maybe, Maybe> map(final Function f) {
                return m -> m.map(f);
            }

            @Override
            public Maybe point(final Object o) {
                return Maybe.point(o);
            }
        };

        static <A, B> Monad<A, B> monad() {
            return monad;
        }
    }

}
