package org.cv.test.arrays;


public class RotateRight {

    public static void main(String[] args) {
        int k = 6;
        int[] positions = {0, 1, 2};

        int[] a = getTestData();
        System.out.println("Naive rotations, O(k*(n-1))");
        for (int i = 0; i < k; i++) {
            a = rotateRightNaive(a);
        }
        printPositions(positions, a);

        a = getTestData();
        System.out.println("With buffer, but with no k multiplier, O(n)");
        a = rotateWithCopyButWithNoOffsets(a, k);
        printPositions(positions, a);

    }

    public static int[] rotateRightNaive(int[] arr) {
        int[] arr2 = new int[arr.length];
        arr2[0] = arr[arr.length - 1];
        for (int i = 0; i < arr.length - 1; i++) {
            arr2[i + 1] = arr[i];
        }
        return arr2;
    }

    public static int[] rotateWithCopyButWithNoOffsets(int[] arr, int order) {
        int offset = arr.length - order % arr.length;
        if (offset > 0) {
            int[] copy = arr.clone();
            for (int i = 0; i < arr.length; ++i) {
                int j = (i + offset) % arr.length;
                arr[i] = copy[j];
            }
        }
        return arr;
    }

    public static void printPositions(int [] positions, int [] a) {
        for (int i = 0; i < positions.length; i++) {
            System.out.println(a[positions[i]]);
        }
    }

    public static int [] getTestData() {
        int[] a = {1, 2, 3};
        return a;
    }

}
