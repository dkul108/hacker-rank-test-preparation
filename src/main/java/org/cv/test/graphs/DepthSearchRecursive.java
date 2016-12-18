package org.cv.test.graphs;


import org.cv.test.graphs.search.iterable.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DepthSearchRecursive {

    private Graph graph;

    public static void main(String[] args) {

        Node root = new Node("A");
        root.left = new Node("B");
        root.right = new Node("C");
        root.left.right = new Node("D");
        root.left.left = new Node("E");
        root.right.right = new Node("F");
        root.right.left = new Node("G");
        root.right.left.left = new Node("H");

        dfs(root, "G");

    }

    public static void dfs(Node root, String searched) {
        if (root == null || (root.value == null && root.getChildren().isEmpty())) {
            return ;
        }

        if (root.value.equals(searched)) {
            System.out.println("Foind! " + root.toString());
        }

        for(Node child : root.getChildren()) {
            dfs(child, searched);
        }
    }

    static class Node {
        private String value;
        private Node left;
        private Node right;

        public Node(String value) {
            this.value = value;
        }

        Collection<Node> getChildren() {
            List<Node> nodes = new ArrayList<>();
            nodes.add(left);
            nodes.add(right);
            return nodes;
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

}
