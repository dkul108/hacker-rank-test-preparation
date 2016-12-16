package org.cv.test.trees;


import java.util.ArrayList;
import java.util.List;

public class TreeTranspass {

    interface Node<T> {
        T getValue();
        Node<T> getLeft();
        Node<T> getRight();
        default boolean isLeaf() {
            return getLeft() == null && getRight() == null;
        }
    }

    public static List traversePreRecursive(Node node) {
        if (node == null) return new ArrayList();

        List nodeValues = new ArrayList();
        nodeValues.add(node.getValue());
        nodeValues.addAll(traversePreRecursive(node.getLeft()));
        nodeValues.addAll(traversePreRecursive(node.getRight()));

        return nodeValues;
    }
}
