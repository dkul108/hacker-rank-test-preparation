package org.cv.test.datastructures;

import java.util.ArrayList;

public class Stack<T> {
    private int maxSize;
    private T[] stackArray;
    private int top;

    public Stack(int s) {
        stackArray = (T [])new ArrayList<T>(maxSize).toArray();
        maxSize = s;
        top = -1;
    }

    public void push(T j) {
        stackArray[++top] = j;
    }

    public T pop() {
        return stackArray[top--];
    }
    public T peek() {
        return stackArray[top];
    }
    public boolean isEmpty() {
        return (top == -1);
    }
    public boolean isFull() {
        return (top == maxSize - 1);
    }
}
