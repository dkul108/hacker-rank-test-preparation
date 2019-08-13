package org.cv.test.functional.cont;

import java.util.ArrayList;
import java.util.List;

public class ListPrefixes {

    public static void main(String[] args) {
        final List<String> a = List.of("a", "b", "c", "d");

        System.out.println(getPrefixes_O_N2_NoContinuation(a));
        System.out.println(getPrefixes_O_N_NoContinuation(a));
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

}
