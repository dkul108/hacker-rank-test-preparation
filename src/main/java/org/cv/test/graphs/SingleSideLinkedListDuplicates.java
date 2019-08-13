package org.cv.test.graphs;


import java.util.HashSet;
import java.util.Set;

public class SingleSideLinkedListDuplicates {


    public static void main(String args[]) {


        LinkedListNode node_1 = setupLinkedNodes();
        print(node_1);

        deleteDups(node_1);
        System.out.println("no duplicates");
        print(node_1);

        node_1 = setupLinkedNodes();
        deleteDups2NoBuffer(node_1);
        System.out.println("no duplicates");
        print(node_1);

        printReversedRecursive(node_1);

        System.out.println("reversed");
        print(reverse(node_1));
    }



    private static LinkedListNode setupLinkedNodes() {
        LinkedListNode node_1 = new LinkedListNode(1);
        LinkedListNode node_2 = new LinkedListNode(1);
        node_1.next = node_2;
        LinkedListNode node_3 = new LinkedListNode(3);
        node_2.next = node_3;
        LinkedListNode node_4 = new LinkedListNode(1);
        node_3.next = node_4;
        LinkedListNode node_5 = new LinkedListNode(4);
        node_4.next = node_5;
        node_5.next = new LinkedListNode(4);
        return node_1;
    }

    private static void print(LinkedListNode node_1) {
        LinkedListNode current = node_1;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    public static void deleteDups(LinkedListNode n) {
        Set<Integer> table = new HashSet<>();
        LinkedListNode previous = null;
        while (n != null) {
            if (table.contains(n.data)) {
                previous.next = n.next;
            } else {
                table.add(n.data);
                previous = n;
            }
            n = n.next;
        }
    }

    public static void deleteDups2NoBuffer(LinkedListNode head) {
        if (head == null) return;
        LinkedListNode previous = head;
        LinkedListNode current = head.next;
        while (current != null) {
            LinkedListNode runner = head;
            while (runner != current) {
                if (runner.data == current.data) {
                    LinkedListNode tmp = current.next;
                    previous.next = tmp;
                    current = tmp;
                    break;
                }
                runner = runner.next;
            }
            if (runner == current) {
                previous = current;
                current = current.next;
            }
        }
    }


    public static LinkedListNode reverse(LinkedListNode head) {
        LinkedListNode prev = null;
        LinkedListNode curr= head;
        LinkedListNode next = null;

        while(curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        head = prev;

        return head;
    }

    private static void printReversedRecursive(LinkedListNode node) {
        if(node == null) {
            return;
        }
        printReversedRecursive(node.next);
        System.out.println("Reversed recursively: "+node.data);
    }

    private static class LinkedListNode {
        LinkedListNode next;
        int data;

        public LinkedListNode(int d) {
            this.data = d;
        }
    }

}
