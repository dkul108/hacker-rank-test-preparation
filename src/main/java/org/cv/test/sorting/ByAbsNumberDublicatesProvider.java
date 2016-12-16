package org.cv.test.sorting;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.valueOf;
import static java.lang.Math.abs;

public class ByAbsNumberDublicatesProvider {

    private Integer[] arr = {1, 2, 3, 5, -1, -2, -3, 8, -17, 5, 4, 3, 2, 3, -3, 3, 1, 17};

    private ByAbsNumberDublicatesProvider() {
    }

    private Map<Integer, Integer> absSortedPairDublicates() {
        Map<Integer, Integer> pairs = new HashMap<>();
        Arrays.sort(Arrays.copyOf(arr, arr.length), (o1, o2) -> {
            int compared = compareToByAbsValueOnly(o1, o2);
            if ( compared == 0 &&
                    !o1.equals(o2) &&
                    o1 > 0 &&
                    !pairs.containsKey(o1)) {
                pairs.put(o1, o2);
            }
            return compared;
        });
        return pairs;
    }

    private int compareToByAbsValueOnly(Integer o1, Integer o2) {
        return valueOf(abs(o1)).compareTo(abs(o2));
    }

    public void printPairsOfDublicates() {
        absSortedPairDublicates()
                .entrySet()
                .forEach(
                    (entry) -> System.out.println(String.format("key: %d  value: %d", entry.getKey(), entry.getValue()))
                );
    }

    public String toString() {
        System.out.println(Arrays.toString(arr));
        return null;
    }

    public static void main(String... args) {
        ByAbsNumberDublicatesProvider pairs = new ByAbsNumberDublicatesProvider();
        pairs.toString();
        pairs.printPairsOfDublicates();
    }


}
