package org.cv.test.functional.monades;


import java.util.function.Function;

public class Monades {
    interface Functor<A> {
        <B> Functor<B> map(Function<A, B> f);
    }

    interface Monad<A, M extends Monad> extends Functor<A> {
        <B> Monad<B, M> bind(Function<A, M> f);
    }

    interface Maybe<A> extends Monad<A, Maybe> {

        static <A> Maybe<A> point(final A a) {
            return a == null ? Nothing : new Just<>(a);
        }

        class Just<A> implements Maybe<A> {
            public final A a;

            public Just(A a) {
                this.a = a;
            }

            @Override
            public <B> Maybe<B> map(final Function<A, B> f) {
                return point(f.apply(a));
            }

            @Override
            public <B> Maybe<B> bind(final Function<A, Maybe> f) { //should be Maybe<B> but can't typed in Java
                return f.apply(a);
            }
        }

        Maybe Nothing = new Maybe() {
            @Override
            public Maybe bind(Function f) {
                return Nothing;
            }

            @Override
            public Maybe map(final Function f) {
                return Nothing;
            }
        };
    }
}
