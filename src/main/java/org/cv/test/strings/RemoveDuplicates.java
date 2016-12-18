package org.cv.test.strings;


import java.util.Arrays;
import java.util.Random;


/**
 *
 * <b>How to read and talk about the above code:</b>

 The method called removeDupes takes an array of primitive char called arr.
 arr is returned as an array of primitive characters "by value". The arr passed in is garbage collected at the end of Main's member method removeDupes.
 The runtime complexity of this algorithm is O(n) or more specifically O(n+(small constant)) the constant being the unique characters in the entire array of primitive chars.
 The copyOfRange does not increase runtime complexity significantly since it only copies a small constant number of items. The char array called arr is not stepped all the way through.
 If you pass null into removeDupes, the method returns null.
 If you pass an empty array of primitive chars or an array containing one value, that unmodified array is returned.
 Method removeDupes goes about as fast as physically possible, fully utilizing the L1 and L2 cache, soBranch redirects are kept to a minimum.
 A 2015 standard issue unburdened computer should be able to complete this method with an primitive char array containing 500 million characters between 15 and 25 seconds.

 <b>Explain how this code works:</b>

 The first part of the array passed in is used as the repository for the unique characters that are ultimately returned. At the beginning of the function the answer is: "the characters between 0 and 1" as between 0 and tail.

 We define the variable y outside of the loop because we want to find the first location where the array index that we are looking at has been duplicated in our repository. When a duplicate is found, it breaks out and quits, the y==tail returns false and the repository is not contributed to.

 when the index x that we are peeking at is not represented in our repository, then we pull that one and add it to the end of our repository at index tail and increment tail.

 At the end, we return the array between the points 0 and tail, which should be smaller or equal to in length to the original array.

 <b>Talking points exercise for coder interviews:</b>

 Will the program behave differently if you change the y++ to ++y? Why or why not.

 Does the array copy at the end represent another 'N' pass through the entire array making runtime complexity O(n*n) instead of O(n) ? Why or why not.

 Can you replace the double equals comparing primitive characters with a .equals? Why or why not?

 Can this method be changed in order to do the replacements "by reference" instead of as it is now, "by value"? Why or why not?

 Can you increase the efficiency of this algorithm by sorting the repository of unique values at the beginning of 'arr'? Under which circumstances would it be more efficient?
 */
public class RemoveDuplicates {


    public static char[] removeDupes(char[] arr){
        if (arr == null || arr.length < 2)
            return arr;
        int len = arr.length;
        int tail = 1;
        for(int x = 1; x < len; x++){
            int y;
            for(y = 0; y < tail; y++){
                if (arr[x] == arr[y]) break;
            }
            if (y == tail){
                System.out.println("x:" + x + " TAIL #: " + tail + " TAIL: "+arr[tail]);
                arr[tail] = arr[x];
                tail++;
            }
        }
        return Arrays.copyOfRange(arr, 0, tail);
    }

    public static char[] bigArr(int len){
        char[] arr = new char[len];
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-=_+[]{}|;:',.<>/?`~";

        for(int x = 0; x < len; x++){
            arr[x] = alphabet.charAt(r.nextInt(alphabet.length()));
        }

        return arr;
    }
    public static void main(String args[]){

        String result = new String(removeDupes(new char[]{'a', 'b', 'c', 'd', 'a'}));
        assert "abcd".equals(result) : "abcda should return abcd but it returns: " + result;

        result = new String(removeDupes(new char[]{'a', 'a', 'a', 'a'}));
        assert "a".equals(result) : "aaaa should return a but it returns: " + result;

        result = new String(removeDupes(new char[]{'a', 'b', 'c', 'a'}));
        assert "abc".equals(result) : "abca should return abc but it returns: " + result;

        result = new String(removeDupes(new char[]{'a', 'a', 'b', 'b'}));
        assert "ab".equals(result) : "aabb should return ab but it returns: " + result;

        result = new String(removeDupes(new char[]{'a'}));
        assert "a".equals(result) : "a should return a but it returns: " + result;

        result = new String(removeDupes(new char[]{'a', 'b', 'b', 'a'}));
        assert "ab".equals(result) : "abba should return ab but it returns: " + result;


        char[] arr = bigArr(5000000);
        long startTime = System.nanoTime();
        System.out.println("2: " + new String(removeDupes(arr)));
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Program took: " + duration + " nanoseconds");
        System.out.println("Program took: " + duration/1000000000 + " seconds");

    }
}
