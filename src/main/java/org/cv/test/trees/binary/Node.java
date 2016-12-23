package org.cv.test.trees.binary;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Node {
    private int value;
    private Node left;
    private Node right;

    public Node(int value) {
        this.value = value;
    }

    Collection<Node> getChildren() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(left);
        nodes.add(right);
        return nodes;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public int getValue() {
        return value;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value='" + value + '\'' +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}
