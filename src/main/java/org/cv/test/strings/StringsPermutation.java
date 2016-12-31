package org.cv.test.strings;

/**
 * see really better option here https://www.nayuki.io/page/next-lexicographical-permutation-algorithm
 */
public class StringsPermutation {

    public static void permutation(String str) {
        permutation("", str);
    }

    private static void permutation(String prefix, String str) {
        int n = str.length();
        if (n == 0) System.out.println(prefix);
        else {
            for (int i = 0; i < n; i++) {
                System.out.println(String.format("progress. i:%d, prefix: %s, str.charAt(i): %s, str.substring(0, i):%s, str.substring(i + 1, n):%s",
                        i, prefix, str.charAt(i), str.substring(0, i), str.substring(i + 1, n)));
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
            }
        }
    }

    public static void main(String... args) {
        permutation("abc");
    }
}
