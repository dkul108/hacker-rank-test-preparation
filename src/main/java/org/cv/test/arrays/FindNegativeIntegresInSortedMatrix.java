package org.cv.test.arrays;


public class FindNegativeIntegresInSortedMatrix {

    public static void main(String... args) {
        int[][] array = {
                {-3, -2, -1, 4, 5},
                {-2, 6, 7, 8, 4},
                {-6, -5, -4, 2, 3},
                {3, 4, 5, 6, 1}
        };
        int numberOfNegatives = byLastNegativeIndexInRow(array, array.length, array[0].length);
        assert 7 == numberOfNegatives : "incorrect number of negative ints";
    }

    //search complexity O(n+m)
    public static int byLastNegativeIndexInRow(int[][] array, int height, int width) {
        int count = 0;
        int i = 0;
        int j = width - 1;
        while (j >= 0 && i < height) {
            if (array[i][j] < 0) {
                count += (j + 1);
                i++;
            } else {
                j--;
            }
        }
        System.out.println("count=" + count);
        return count;
    }
}
