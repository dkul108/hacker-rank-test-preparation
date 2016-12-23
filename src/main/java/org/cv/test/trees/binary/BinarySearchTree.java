package org.cv.test.trees.binary;

public class BinarySearchTree {

    private Node root;

    // A utility function to search a given key in BST
    public Node breadthSearch(Node root, int key) {
        // Base Cases: root is null or key is present at root
        if (root == null || root.getValue() == key)
            return root;

        // val is greater than root's key
        if (root.getValue() > key)
            return breadthSearch(root.getLeft(), key);

        // val is less than root's key
        return breadthSearch(root.getRight(), key);
    }

    public void insert(int key) {
        root = insertRec(root, key);
    }

    public Node insertRec(Node root, int key) {

        /* If the tree is empty, return a new node */
        if (root == null) {
            root = new Node(key);
            return root;
        }

        /* Otherwise, recur down the tree */
        if (key < root.getValue())
            root.setLeft(insertRec(root.getLeft(), key));
        else if (key > root.getValue())
            root.setRight(insertRec(root.getRight(), key));

        /* return the (unchanged) node pointer */
        return root;
    }

    // This method mainly calls InorderRec()
    void inorder()  {
        inorderRec(root);
    }

    // A utility function to do inorder traversal of BST
    void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.getLeft());
            System.out.println(root.getValue());
            inorderRec(root.getRight());
        }
    }

    // Driver Program to test above functions
    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();

        /* Let us create following BST
              50
           /     \
          30      70
         /  \    /  \
       20   40  60   80 */
        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        // print inorder traversal of the BST
        tree.inorder();
    }
}
