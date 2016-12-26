package org.cv.test.diff;

public class BitwiseOperations {
    public static void main(String[] args) {

        int n = 16;
        printBinaryform(n);
        System.out.println("\n n=" + n +
                "\n Integer.toBinaryString(n)=" + Integer.toBinaryString(n) +
                "\n simplest decimal to octal: Integer.toString(n,8)="  + Integer.toString(n,8) +
                "\n simplest decimal to binary: Integer.toString(n,2)=" + Integer.toString(n,2) +
                "\n simplest decimal to Hex: Integer.toString(n,16)=" + Integer.toString(n,16)
        );

        int a = 60;	/* 60 = 0011 1100 */
        int b = 13;	/* 13 = 0000 1101 */
        int c = 0;

        c = a & b;        /* 12 = 0000 1100 */
        System.out.println("a & b = " + c );

        c = a | b;        /* 61 = 0011 1101 */
        System.out.println("a | b = " + c );

        c = a ^ b;        /* 49 = 0011 0001 */
        System.out.println("a ^ b = " + c );

        c = ~a;           /*-61 = 1100 0011 */
        System.out.println("~a = " + c );

        c = a << 2;       /* 240 = 1111 0000 */
        System.out.println("a << 2 = " + c );

        c = a >> 2;       /* 15 = 1111 */
        System.out.println("a >> 2  = " + c );

        c = a >>> 2;      /* 15 = 0000 1111 */
        System.out.println("a >>> 2 = " + c );
    }



    private static void printBinaryform(int number) {
        int remainder;

        if (number <= 1) {
            System.out.print(number);
            return;
        }

        remainder = number %2;
        printBinaryform(number >> 1);
        System.out.print(remainder);
    }

    public int[] convertBinaryNoRecursion(int no) {
        int i = 0, temp[] = new int[7];
        int binary[];
        while (no > 0) {
            temp[i++] = no % 2;
            no /= 2;
        }
        binary = new int[i];
        int k = 0;
        for (int j = i - 1; j >= 0; j--) {
            binary[k++] = temp[j];
        }
        return binary;
    }
}
