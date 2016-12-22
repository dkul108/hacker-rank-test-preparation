package org.cv.test.graphs.search.iterable;

import java.util.*;

public class Graph {

    private Map<String, List<String>> edges = new HashMap<>();

    public void addEdge(String src, String dest) {
        List<String> srcNeighbors = this.edges.get(src);
        if (srcNeighbors == null) {
            this.edges.put(
                    src,
                    srcNeighbors = new ArrayList<>()
            );
        }
        srcNeighbors.add(dest);
    }

    public Iterable<String> getNeighbors(String vertex) {
        List<String> neighbors = this.edges.get(vertex);
        if (neighbors == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(neighbors);
        }
    }

    public static void main(String[] args) {
        Graph g = new Graph();
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        g.addEdge("B", "D");
        g.addEdge("B", "A");
        g.addEdge("B", "E");
        g.addEdge("B", "F");
        g.addEdge("C", "A");
        g.addEdge("D", "C");
        g.addEdge("E", "B");
        g.addEdge("F", "B");

        expectIteration("Z", new PreOrderDFSIterator(g, "Z"));
        expectIteration("A B C D E F", new PreOrderDFSIterator(g, "A"));
        expectIteration("B C A D E F", new PreOrderDFSIterator(g, "B"));
        expectIteration("Z", new BreadthFirstIterator(g, "Z"));
        expectIteration("A B C D E F", new BreadthFirstIterator(g, "A"));
        expectIteration("B C D A E F", new BreadthFirstIterator(g, "B"));

    }

    private static void expectIteration(String answer, Iterator<String> it) {
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            sb.append(' ').append(it.next());
        }
        String result = "Answer= "+answer + " got from iteraqtor= " + sb.substring(1);
        if(!answer.equals(sb.substring(1))) {
            throw new IllegalStateException(result);
        } else {
            System.out.println("Passed! " + result);
        }
    }
}
