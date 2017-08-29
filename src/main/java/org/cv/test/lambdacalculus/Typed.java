package org.cv.test.lambdacalculus;

public interface Typed {
    interface Fun<A, B> {
        B $(A a);
    }

    interface Pair<A, B, C> extends Fun<Fun<A, Fun<B, C>>, C> {
        static <A, B, C> Pair<A, B, C> $(final A a, final B b) {
            return p -> p.$(a).$(b);
        }

        static <A, B> A first(final Pair<A, B, A> p) {
            return p.$(a -> b -> a);
        }

        static <A, B> B second(final Pair<A, B, B> p) {
            return p.$(a -> b -> b);
        }
    }


    interface Repr<V, T> {
        V $();
    }

    interface Def<V> {
        <T> Repr<V, T> val(T v);

        <A, B> Repr<V, Fun<A, B>> fun(String v, Fun<Repr<V, A>, Repr<V, B>> f);

        <A, B> Fun<Repr<V, A>, Repr<V, B>> app(Repr<V, Fun<A, B>> f);
    }

    interface Expr<T> {
        <V> Repr<V, T> $(Def<V> e);
    }

    interface FunExpr<A, B> extends Expr<Fun<A, B>> {
        default Fun<Expr<A>, Expr<B>> $() {
            return app(this);
        }

        default Expr<B> $(final A a) {
            return $().$(val(a));
        }
    }

    static <T> Expr<T> val(final T v) {
        return new Expr<T>() {
            @Override
            public <V> Repr<V, T> $(final Def<V> e) {
                return e.val(v);
            }
        };
    }

    static <A, B> FunExpr<A, B> fun(
            final String v, final Fun<Expr<A>, Expr<B>> f) {
        return new FunExpr<A, B>() {
            @Override
            public <V> Repr<V, Fun<A, B>> $(final Def<V> e) {
                return e.fun(v, (Repr<V, A> a) -> f.<V, B>$(new Expr<A>() {
                    @Override
                    public <V> Repr<V, A> $(final Def<V> e) {
                        return (Repr<V, A>) a;
                    }
                }).$(e));
            }
        };
    }

    static <A, B> Fun<Expr<A>, Expr<B>> app(final Expr<Fun<A, B>> f) {
        return a -> new Expr<B>() {
            @Override
            public <V> Repr<V, B> $(final Def<V> e) {
                return e.app(f.$(e)).$(a.$(e));
            }
        };
    }

    static <T> Repr<String, T> show(final String str) {
        return () -> str;
    }

    interface ShowDef extends Def<String> {
        @Override
        default <A, B> Repr<String, Fun<A, B>> fun(
                final String v, final Fun<Repr<String, A>, Repr<String, B>> f) {
            return show("(" + v + " -> " + f.$(show(v)).$() + ")");
        }

        @Override
        default <A, B> Fun<Repr<String, A>, Repr<String, B>> app(
                final Repr<String, Fun<A, B>> f) {
            return a -> show(f.$() + " " + a.$());
        }

        @Override
        default <T> Repr<String, T> val(final T v) {
            return show("" + v);
        }
    }

    interface Lambda {
        Lambda $(Lambda f);

        default <T> T $() {
            throw new RuntimeException(
                    "Not a value");
        }

        static <T> Lambda $(final T v) {
            return new Val<>(v);
        }

        class Val<T> implements Lambda {
            private final T val;

            public Val(final T val) {
                this.val = val;
            }

            @Override
            public <T> T $() {
                return (T) val;
            }

            @Override
            public Lambda $(final Lambda x) {
                throw new RuntimeException(
                        "Not a functon: " + val);
            }
        }
    }

    interface EvalDef extends Def<Lambda> {
        @Override
        default <T> Repr<Lambda, T> val(final T v) {
            return () -> Lambda.$(v);
        }

        @Override
        default <A, B> Repr<Lambda, Fun<A, B>> fun(
                final String v, final Fun<Repr<Lambda, A>, Repr<Lambda, B>> f) {
            return () -> a -> f.$(() -> a).$();
        }

        @Override
        default <A, B> Fun<Repr<Lambda, A>,
                Repr<Lambda, B>> app(final Repr<Lambda, Fun<A, B>> f) {
            return a -> () -> f.$().$(a.$());
        }
    }

    interface IntDef<V> extends Def<V> {
        default V $(final Fun<IntDef<V>, Repr<V, Integer>> e) {
            return e.$(this).$();
        }

        Repr<V, Integer> plus(Repr<V, Integer> a, Repr<V, Integer> b);
    }


    interface ShowInt extends IntDef<String> {
        @Override
        default Repr<String, Integer> plus(final Repr<String, Integer> a,
                                           final Repr<String, Integer> b) {
            return show(a.$() + " + " + b.$());
        }
    }

    interface EvalInt extends IntDef<Lambda> {
        @Override
        default Repr<Lambda, Integer> plus(final Repr<Lambda, Integer> a,
                                           final Repr<Lambda, Integer> b) {
            return () -> Lambda.$(a.$().<Integer>$() + b.$().<Integer>$());
        }
    }

    interface IntExpr extends Expr<Integer> {
        <V> Repr<V, Integer> $(final IntDef<V> e);

        @Override
        default <V> Repr<V, Integer> $(final Def<V> e) {
            return $((IntDef<V>) e);
        }
    }

    static IntExpr plus(final Expr<Integer> a, final Expr<Integer> b) {
        return new IntExpr() {
            @Override
            public <V> Repr<V, Integer> $(final IntDef<V> e) {
                return e.plus(a.$(e), b.$(e));
            }
        };
    }

    interface Show extends ShowDef, ShowInt {}
    Show show = new Show() {};

    interface Eval extends EvalDef, EvalInt {}
    Eval eval = new Eval() {};

    static <P> Pair<String, Integer, P> showEval(final Expr<Integer> e) {
        return Pair.$(show.$(e::$), eval.$(e::$).<Integer>$());
    }


    Expr<Integer> expr = fun("a", (Expr<Integer> a) ->
            fun("b", (Expr<Integer> b) -> plus(a, b))
                    .$(1)).$(2);

    // (a -> (b -> a + b) 1) 2 = 3

    static void main(final String[] args) {

        System.out.println(showEval(expr)
                .$(s -> e -> s + " = " + e));
    }
}
