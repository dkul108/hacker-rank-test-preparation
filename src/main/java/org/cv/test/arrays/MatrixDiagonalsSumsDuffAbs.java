package org.cv.test.arrays;


public class MatrixDiagonalsSumsDuffAbs {
    public static void main(String[] args) {
        int[][] m = {
                {11, 2, 4},
                {4, 5, 6},
                {10, 8, -12}
        };

        int sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i][i];
            System.out.println(m[i][i]);
        }
        for (int i = 0, j = m.length - 1; i < m.length && j > -1; i++, j--) {
            sum -= m[i][j];
            System.out.println(m[i][j]);
        }
        System.out.println(Math.abs(sum));
    }
}
