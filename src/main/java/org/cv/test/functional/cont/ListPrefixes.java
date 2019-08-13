package org.cv.test.functional.cont;

import java.util.ArrayList;
import java.util.List;

public class ListPrefixes {

    public static void main(String[] args) {
        final List a = List.<String>of("a", "b", "c", "d");

        final List<List<String>> prefixes = getPrefixesON2NoContinuation(a);
        System.out.println(prefixes);
    }

    private static List<List<String>> getPrefixesON2NoContinuation(List a) {
        final List<List<String>> prefixes = new ArrayList<List<String>>();
        for (int i = 0; i < a.size(); i++) {
            final List curr = new ArrayList<String>();

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

}
