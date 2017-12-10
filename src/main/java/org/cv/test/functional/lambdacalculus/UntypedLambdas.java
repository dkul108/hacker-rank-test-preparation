package org.cv.test.functional.lambdacalculus;

import java.util.Objects;


public interface UntypedLambdas {

    interface Lambda {
        Lambda $(Lambda f);

        default <T> Lambda $(final T v) {
            return $(new Val<>(v));
        }

        class Val<T> implements Lambda {
            public final T val;

            private Val(final T val) {
                this.val = val;
            }

            @Override
            public Lambda $(final Lambda f) {
                return !(f instanceof Val) ? f.$(this) :
                        x -> x.$(this).$(f);
            }

            @Override
            public String toString() {
                return "" + val;
            }

            @Override
            public boolean equals(final Object o) {
                return (this == o) || Objects.equals(val, o) ||
                        (o != null && o instanceof Val &&
                                Objects.equals(val, ((Val) o).val));
            }

            @Override
            public int hashCode() {
                return Objects.hash(val);
            }
        }
    }

    static <T> Lambda $(final T v) {
        return I.$(v);
    }

    static <T> T print(final T x) {
        System.out.println(x);
        return x;
    }

    Lambda print = x -> print(x);

    //2 cases, both are alternatives to Alan Turing Machine:

    //IKS combinators
    Lambda I = x -> x;
    Lambda K = x -> y -> x;
    Lambda S = x -> y -> z -> x.$(z).$(y.$(z));

    //labda calculus
    Lambda pair = p -> a -> b -> p.$(a).$(b);
    Lambda first = p -> p.$(a -> b -> a);
    Lambda second = p -> p.$(a -> b -> b);

    static void main(String[] args) {

        print(S.$(K).$(K).$("x").equals(I.$("x")));

        pair.$("x").$("y").$(first).$(print);

        second.$("x").$("y").$(print);

        $("x").$("y").$(first).$(print);

        $("x").$("y").$(K.$(print));
    }
}