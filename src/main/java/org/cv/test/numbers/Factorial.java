package org.cv.test.numbers;


import java.math.BigInteger;
import java.util.Scanner;

public class Factorial {
    public static void main(String[] args) {
        factorial(5);
        extraLargeFactorial(100);


        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int k = scan.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; ++i) {
            arr[i] = scan.nextInt();
        }
        int[] cnts = new int[k];
        for (int i = 0; i < k; ++i) {
            cnts[i] = 0;
        }
        for (int i = 0; i < n; ++i) {
            cnts[arr[i] % k] += 1;
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

    public static void factorial(int n) {
        long f = 1;
        for (int i = 1; i <= n; i++) {
            f *= i;
        }
        System.out.println(f);
    }

    public static void extraLargeFactorial(int n) {
        BigInteger f = new BigInteger("1");
        for (int i = 1; i <= n; i++) {
            f = f.multiply(new BigInteger(String.valueOf(i)));
        }
        System.out.println(f);
    }
}
