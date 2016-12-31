package org.cv.test.numbers;

/**
 * Given a set, S, of n distinct integers, print the size of a maximal subset, S', of S where the sum of any  2 numbers in  is not evenly divisible by .

 Input Format

 The first line contains 2 space-separated integers, n and k, respectively.
 The second line contains n space-separated integers (we'll refer to the i-th value as a(i)) describing the unique values of the set.


 Output Format

 Print the size of the largest possible subset (S').

 Sample Input
 4 3
 1 7 2 4

 Sample Output

 3
 */
public class NonDivisibleSubset {

    /**
     Can be done on O(n) with a few considerations.
     For any number K, the sum of 2 values (A & B) is evenly divisible by K if the sum of the remainders of A/K + B/K is K.
     (There is also a special case where both A & B are evenly divisible, giving a sum of 0.)
     For any such remainder, there is 1 and only 1 other remainder value which will make a sum divisible by K.
     Example: with K of 5, remainder pairs are 1+4 & 2+3. Given the numbers with a remainder of 1,
     they can't be paired with ANY of the numbers with remainder 4 (and vice versa). So, for the number of values
     in the resultant set, choose the larger of values with remainder 1 vs. values with remainder 4. Choose the larger set of remainder 2 vs remainder 3.
     For the special case where remainder is 0, given the set of all values which are individually divisible by K, they can't be paired with any others.
     So, at most 1 value which is evenly divisible by K can be added to the result set.
     For even values of K, the equal remainder is simular to the 0 case. For K = 6, pairs are 1+5, 2+4, 3+3.
     For values with remainder 3, at most one value can be added to the result set.
     */

    public static void main(String[] args) {
        int n = 4;
        int k = 3;
        int [] a = {1, 7, 2, 4};
        int[] cnts = new int[k];

        for (int i = 0; i < n; ++i) {
            cnts[a[i] % k] += 1;
        }

        int cnt = cnts[0] < 1 ? cnts[0] : 1;
        for (int i = 1; i < k / 2 + 1; ++i) {
            if (i != k - i) {
                cnt += cnts[i] > cnts[k - i] ? cnts[i] : cnts[k - i];
            }
        }
        if (k % 2 == 0) {
            cnt += 1;
        }
        System.out.println(cnt);
    }
}
