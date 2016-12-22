package org.cv.test.arrays;

import java.util.Arrays;

public class BinarySearch {

    public static void main(String[] args) {
        int [] arr = {49, 1,2,3,4,5,6,7,8,9,10,11,15,25,42,48,12};
        Arrays.sort(arr);

        System.out.println("Index: " + searchBinary(9, arr, 0, arr.length - 1));
        assert 9 == searchBinary(9, arr, 0, arr.length - 1) : "incorrect index found";
    }

    static int searchBinary(int number, int [] arr, int low, int hight) {
        int middle = low + (hight - low)/2;
        if(arr[middle] > number) {
            searchBinary(number, arr, middle + 1, hight);
        } else if (arr[middle] < number) {
            searchBinary(number, arr, low, middle);
        }
        return middle;
    }
}
