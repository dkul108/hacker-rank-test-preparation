package org.cv.test.diff;


import java.util.Scanner;

public class TimeToWords {
    private static final String OCLOCK = " o' clock";
    private static final String QUARTER = "quarter";
    private static final String TO = " to ";
    private static final String PAST = " past ";
    private static final String HALF = "half";

    private static String words[] = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
            "twenty", "twenty one", "twenty two", "twenty three", "twenty four", "twenty five",
            "twenty six", "twenty seven", "twenty eight", "twenty nine", "half"};

    public static void main(String[] args) {
        /*
        Scanner in = new Scanner(System.in);
        int h = in.nextInt();
        int m = in.nextInt();
        */


        timeToWords(5, 00);
        timeToWords(5, 01);
        timeToWords(5, 10);
        timeToWords(5, 30);
        timeToWords(5, 40);
        timeToWords(5, 45);
        timeToWords(5, 47);
        timeToWords(12, 00);
    }

    private static void timeToWords(int h, int m) {

        if (h > 12 || h < 1 || m > 60 || m < 0)
            return;

        StringBuilder sb = new StringBuilder(20);

        String hourRep = (m > 30) ? ((h == 12) ? words[1] : words[h + 1]) : words[h];
        String minutesRep = (m == 1 || m == 59) ? "one minute" : words[Math.min(Math.abs(60 - m), m)] + " minutes";
        String preposition = (m <= 30) ? PAST : TO;

        switch (m) {
            case 0:
                sb.append(words[h] + OCLOCK);
                break;

            case 15:
                sb.append(QUARTER + preposition + hourRep);
                break;

            case 30:
                sb.append(HALF + preposition + hourRep);
                break;

            case 45:
                sb.append(QUARTER + preposition + hourRep);
                break;
            default:
                sb.append(minutesRep + " " + preposition + " " + hourRep);
        }

        System.out.println(sb.toString().replaceAll(" +", " "));
    }
}
