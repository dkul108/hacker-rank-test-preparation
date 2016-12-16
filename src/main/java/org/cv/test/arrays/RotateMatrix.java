package org.cv.test.arrays;

/*
Algorithms
Your interview with Amazon will not be focused on rote memorization of algorithms; however, having a good
understanding of the most common algorithms will likely make solving some of the questions we ask a lot easier. Consider
reviewing traversals, divide and conquer, and any other common algorithms you feel might be worth brushing up on. For
example, it might be good to know how and when to use a breadth-first search versus a depth-first search, and what the
tradeoffs are. Knowing the runtimes, theoretical limitations, and basic implementation strategies of different classes of
algorithms is more important than memorizing the specific details of any given algorithm.

Coding
Expect to be asked to write syntactically correct code—no pseudo code. If you feel a bit rusty coding without an IDE or
coding in a specific language, it’s probably a good idea to dust off the cobwebs and get comfortable coding with a pen and
paper. The most important thing a Software Development Engineer does at Amazon is write scalable, robust, and welltested code. These are the main criteria by which your code will be evaluated, so make sure that you check for edge cases
and validate that no bad input can slip through. A few missed commas or typos here and there aren’t that big of a deal, but
the goal is to write code that’s as close to production ready as possible. This is your chance to show off your coding ability

Object-Oriented Design
Good design is paramount to extensible, bug free, long-lived code. It’s possible to solve any given software problem in an
almost limitless number of ways, but when software needs to be extensible and maintainable, good software design is
critical to success. Using Object-oriented design best practices is one way to build lasting software. You should have a
working knowledge of a few common and useful design patterns as well as know how to write software in an objectoriented way, with appropriate use of inheritance and aggregation. You probably won’t be asked to describe the details of
how specific design patterns work, but expect to have to defend your design choices.
 */
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
        for (int i = 0; i < w; i++) {
            System.out.println("[");
            for (int j = 0; j < h; j++) {
                System.out.print(matrix[i][j] + ",");
            }
            System.out.println("]");
        }
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
    }

}
