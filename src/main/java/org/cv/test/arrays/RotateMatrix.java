package org.cv.test.arrays;

public class RotateMatrix {
    public static int[][] rotateMatrixRight(int[][] matrix) {
        int w = matrix.length;
        int h = matrix[0].length;
        int[][] ret = new int[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                ret[i][j] = matrix[w - j - 1][i];
            }
        }
        return ret;
    }

    public static int[][] rotateMatrixLeft(int[][] matrix) {
        int w = matrix.length;
        int h = matrix[0].length;
        int[][] ret = new int[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                ret[i][j] = matrix[j][h - i - 1];
            }
        }
        return ret;
    }

    public static void print(int[][] matrix) {
        int w = matrix.length;
        int h = matrix[0].length;
        for (int[] aMatrix : matrix) {
            System.out.println("[");
            for (int j = 0; j < h; j++) {
                System.out.print(aMatrix[j] + ",");
            }
            System.out.println("]");
        }
    }

    //Time - O(N), Space - O(N)
    public static int[][] rotateRightSquareMatrixNoNewInst(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n / 2; i++) {
            int last = n - 1 - i;
            for (int j = i; j < last; j++) {
                //save top
                int top = matrix[i][j];
                //left->top
                matrix[i][j] = matrix[last - j][i];
                //bottome->left
                matrix[last - j][i] = matrix[last][last - j];
                //right->bottom
                matrix[last][last - j] = matrix[j][last];
                //top->right
                matrix[j][last] = top;
            }
        }
        return matrix;
    }

    //https://www.youtube.com/watch?v=aClxtDcdpsQ
    public static int[][] realRotateSquareMatrixRightByLayers(int[][] matrix) {
        int n = matrix.length - 1;
        for (int layer = 0; layer < n / 2; layer++) {
            int first = layer;
            int last = n - layer;// - 1;
            for (int i = first; i < last; i++) {
                int offset = i - first;
                //save top
                int top = matrix[first][i];
                //left->top
                matrix[first][i] = matrix[last - offset][first];
                //bottome->left
                matrix[last - offset][first] = matrix[last][last - offset];
                //right->bottom
                matrix[last][last - offset] = matrix[i][last];
                //top->right
                matrix[i][last] = top;
            }
        }
        return matrix;
    }

    public static void main(String... args) {
        int[][] array = {
                {1, 2, 3, 4, 5},
                {5, 6, 7, 8, 4},
                {9, 0, 1, 2, 3},
                {3, 4, 5, 6, 1}
        };
        print(array);
        System.out.println("\n right");
        print(rotateMatrixRight(array));
        System.out.println("\n left");
        print(rotateMatrixLeft(array));

        int[][] squareMatrix = {
                {1, 2, 3, 4, 5},
                {5, 6, 7, 8, 4},
                {9, 0, 1, 2, 3},
                {3, 4, 5, 6, 1},
                {2, 6, 9, 1, 7}
        };
        System.out.println("initial square matrix");
        print(squareMatrix);
        System.out.println("\n rotate square matrix right no new matrix instance");
        print(rotateRightSquareMatrixNoNewInst(squareMatrix));
        System.out.println("\n rotate square matrix right by layers no new matrix instance");
        print(realRotateSquareMatrixRightByLayers(squareMatrix)); //<-here is a proper solution
    }

}
