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

    public static void main (String[] args) {
            String s = "[()]{}{[()()]()}";
            //System.out.println(s);
            Stack<Character> stack = new Stack<>(s.length());
            boolean balanced = true;
            for(char c : s.toCharArray()) {
                if(c == '[' || c == '{' || c == '(') {
                    stack.push(c);
                } else if (c == ']' || c == '}' || c == ')' ) {
                    char c2 = stack.pop();
                    balanced =  (c == ']' && c2 == '[') ||  (c == '}' && c == '{') || (c == ')' && c =='(');
                }
            }
            System.out.println(balanced ? "balanced" : "not balanced");
    }
}
