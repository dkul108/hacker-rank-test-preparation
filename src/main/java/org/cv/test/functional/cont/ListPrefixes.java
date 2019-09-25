package org.cv.test.functional.cont;

import com.google.common.collect.Lists;
import org.cv.test.functional.optics.Optics;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ListPrefixes {

    public static void main(String[] args) {
        final List<String> a = List.of("a", "b", "c", "d");

        System.out.println(getPrefixes_O_N2_NoContinuation(a));
        System.out.println(getPrefixes_O_N_NoContinuation(a));

        System.out.println("cross join" + crossJoin(Lists.newArrayList(1, 2), Lists.newArrayList("A", "B")));
        System.out.println("inner join" + innerJoin(Lists.newArrayList(0, 1, 2, 3), Lists.newArrayList(3,2,7, 9)));
        System.out.println("left join" + leftJoinBogus(Lists.newArrayList(0, 1, 2, 3), Lists.newArrayList(3,2,7, 9)));

    }

    private static List<List<String>> getPrefixes_O_N2_NoContinuation(List<String> a) {
        final List<List<String>> prefixes = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            final List<String> curr = new ArrayList<>();

            curr.add(a.get(i));
            prefixes.add(curr);

            if(prefixes.size() > 1) {
                final List<String> prev = prefixes.get(prefixes.size() - 2);
                for(int j = prev.size() -1; j >= 0; j--) {
                    curr.add(0, prev.get(j));
                }
            }
        }
        return prefixes;
    }

    private static List<List<String>> getPrefixes_O_N_NoContinuation(List<String> a) {
        final List<List<String>> prefixes = new ArrayList<>();
        final ArrayList<String> curr = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            curr.add(a.get(i));
            prefixes.add((List<String>) curr.clone());
        }
        return prefixes;
    }

    private static <E, D> List crossJoin(List<E> l1, List<D>l2) {
        Stream<E> s1 = l1.stream();
        Supplier<Stream<D>> s2 = l2::stream;

        return s1.flatMap(v1 -> s2.get()
                .map(v2 -> new Optics.Pair(v1, v2)))
                .collect(Collectors.toList());
    }

    private static <E, D> List innerJoin(List<E> l1, List<D>l2) {
        return l1.stream().flatMap(x1->l2.stream().filter(x2-> Objects.equals(x1, x2)).map(x2 -> new Optics.Pair(x1, x2))).collect(Collectors.toList());
    }

    private static <A> Stream<A> defaultIfEmpty(Stream<A> stream, Supplier<A>sup){
        final Iterator<A> iterator = stream.iterator();
        if (iterator.hasNext()) {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
        } else {
            return Stream.of(sup.get());
        }
    }

    private static List<Optics.Pair> leftJoinBogus(List<Integer> l1, List<Integer>l2) {
        return defaultIfEmpty(l1.stream(), () -> -1)
                .flatMap(x1 -> defaultIfEmpty(l2.stream(),  () -> -1).filter(x2-> Objects.equals(x1, x2)).map(x2 -> new Optics.Pair(x1, x2))).collect(Collectors.toList());
    }

//    public static  <K, V1, V2> List<Triple<K, V1, V2>> nestedLoopsJoin(List<Pair<K, V1>> left, List<Pair<K, V2>> right) {
//        List<Triple<K, V1, V2>> result = new ArrayList<>();
//        for (Pair<K, V1> leftPair: left) {
//            for (Pair<K, V2> rightPair: right) {
//                if (Objects.equals(leftPair.k, rightPair.k)) {
//                    result.add(new Triple<>(leftPair.k, leftPair.v, rightPair.v));
//                }
//            }
//        }
//        return result;
//    }




}
