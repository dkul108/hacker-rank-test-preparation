package org.cv.test.numbers;


/**
 *   Name   Width in Bits   Range
    double  64              1 .7e–308 to 1.7e+308
    float   32              3 .4e–038 to 3.4e+038
 */
public class OperatotionsWithNumbers {
    public static void main(String[] args) {
        System.out.println("(double)3/(double)2 = " + (double)3/(double)2 );
        System.out.println("3/2 = " + 3/2 );

        System.out.println("2%3 = " + 2%3 );
        System.out.println("(double)2%(double)3 = " + (double)2%(double)3 );

        System.out.println("1000000000000l%1l="+ 1000000000000l%1l);
        int n = 16;
        System.out.println("\n n=" + n +
                "\n Integer.toBinaryString(n)=" + Integer.toBinaryString(n) +
                "\n simplest decimal to octal: Integer.toString(n,8)="  + Integer.toString(n,8) + " what?!" +
                "\n simplest decimal to binary: Integer.toString(n,2)=" + Integer.toString(n,2) +
                "\n simplest decimal to Hex: Integer.toString(n,16)=" + Integer.toString(n,16) + " what?!"
        );


        System.out.println(Integer.parseInt("00101", 2));

    }
}
