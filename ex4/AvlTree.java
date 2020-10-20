package oop.ex4.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException; // used in the iterator function
import java.lang.NullPointerException; // used  once in the deleteLeaf function

/**
 * Implements the AVLTree data structure, using Nodes that store information about their height.
 */
public class AvlTree implements Iterable<Integer> {


    /*
    the root of the tree
     */
    private Node root;

    /*
    the total number of nodes in the tree
     */
    private int totalNodes;


    private static final int MINIMAL_HEIGHT_OF_TREE_SIZED_0 = 1;
    private static final int MINIMAL_HEIGHT_OF_TREE_SIZED_1 = 2;
    private static final int MINIMAL_HEIGHT_OF_TREE_SIZED_2 = 4;
    private static final int LEFT_HEAVY = 1;
    private static final int RIGHT_HEAVY = -1;

    /**
     * A default constructor. initiates an empty AVL tree.
     */
    public AvlTree() {
        totalNodes = 0;

    }

    /**
     * A constructor that builds the tree by adding the elements in the input array one-by-one.
     * If the same values appears twice (or more) in the list, it is ignored.
     *
     * @param data values to add to tree
     */
    public AvlTree(int[] data) {
        // if a null argument is given, then we'll throw an exception
        if (data == null)
            throw new IllegalArgumentException();
        for (int num : data) {
            add(num);
        }
    }

    /**
     * A copy-constructor that builds the tree from existing tree
     *
     * @param avlTree tree to be copied
     */
    public AvlTree(AvlTree avlTree) {
        if (avlTree == null) {
            totalNodes = 0;
        } else {
            for (int nodeData : avlTree) {
                add(nodeData);
            }
        }
    }

    /**
     * A method that calculates the minimum numbers of nodes in an AVL tree of height h.
     *
     * @param h height of the tree (a non-negative number).
     * @return minimum number of nodes of height h
     */
    public static int findMinNodes(int h) {
        if (h == 0)
            return MINIMAL_HEIGHT_OF_TREE_SIZED_0;
        if (h == 1)
            return MINIMAL_HEIGHT_OF_TREE_SIZED_1;
        int stepBack = MINIMAL_HEIGHT_OF_TREE_SIZED_1;
        int sum = MINIMAL_HEIGHT_OF_TREE_SIZED_2;
        int temp;
        for (int i = 2; i < h; i++) {
            temp = sum;
            sum += stepBack + 1;
            stepBack = temp;
        }
        return sum;
    }

    /**
     * A method that calculates the maximum number of nodes in an AVL tree of height h.
     *
     * @param h height of the tree (a non-negative number).
     * @return maximum number of nodes of height h
     */
    public static int findMaxNodes(int h) {

        return (int) (Math.pow(2, h + 1) - 1);

    }



    /**
     * Returns an iterator for the Avl Tree. The returned iterator iterates over the tree nodes in an
     * ascending order, and does NOT implement the remove() method.
     *
     * @return an iterator for the Avl Tree.
     */
    public Iterator<Integer> iterator() {

        return new Iterator<Integer>() {
            Node iteratorTree = findMin();

            @Override
            public boolean hasNext() {
                return (iteratorTree != null);
            }

            @Override
            public Integer next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                int num = iteratorTree.getData();
                iteratorTree = successor(iteratorTree);
                return num;
            }
        };
    }

    /**
     * Add a new node with key newValue into the tree.
     *
     * @param newValue new value to add to the tree.
     * @return false iff newValue already exist in the tree
     */
    public boolean add(int newValue) {
        // checks if the tree is empty, and if so, adds the new Value to be the new root.
        if (totalNodes == 0) {
            root = new Node(newValue);
            totalNodes = 1;
            return true;
        }
        //calls on the recursive helper function to add the value
        Node currentNode = root;
        return addRecursiveHelper(currentNode, newValue);

    }

    /**
     * The recursive function whose job is it to:
     * check if the value is in the AVL tree
     * add it if not to the right place
     * update the heights of the nodes that need updating
     * balance the new tree if need be
     *
     * @param nodeToCompare the current node we are comparing to in this recursive cycle
     * @param valueToAdd    the value we wish to add to the AVL tree
     * @return false iff newValue already exist in the tree
     */
    private boolean addRecursiveHelper(Node nodeToCompare, int valueToAdd) {
        if (nodeToCompare.getData() == valueToAdd) { // found node with data - don't add!
            return false;
        }
        if (nodeToCompare.getData() < valueToAdd) { // new node should be added to the right
            if (nodeToCompare.getRightSon() == null) { // node has no right son, add to the right
                addNewLeafAndBalance(nodeToCompare, valueToAdd, "right");
                return true;
            }
            return addRecursiveHelper(nodeToCompare.getRightSon(), valueToAdd);
        } // new node should be added to the left
        if (nodeToCompare.getLeftSon() == null) { // has no left son, add to the left
            addNewLeafAndBalance(nodeToCompare, valueToAdd, "left");
            return true;
        }
        //keep looking for the right spot recursively
        return addRecursiveHelper(nodeToCompare.getLeftSon(), valueToAdd);
    }

    /**
     * this function is called when we have reached the place where the new value is to be added to the tree.
     * the value is added, and the tree is balanced using the balanceUp function
     *
     * @param newFather  - the node which will become the new father of the value we are adding (will always
     *                   be a leaf)
     * @param valueToAdd - the value we wish to add to the tree
     * @param whereToAdd - if we should add it to the right or to the left of the father
     */
    private void addNewLeafAndBalance(Node newFather, int valueToAdd, String whereToAdd) {
        // creates the new node and adds it to the appropriate place
        Node newNode = new Node(newFather, null, null, valueToAdd);
        if (whereToAdd.equals("right"))
            newFather.setRightSon(newNode);
        else
            newFather.setLeftSon(newNode);
        //increases the number of nodes in the tree by one and sets height of the new value to zero
        totalNodes += 1;
        newNode.setHeight(0);
        // update all of the fathers along the way, and balance them
        balanceUp(newFather);
    }

    /**
     * this function receives a node, and goes all the way up to the root, while doing 2 things:
     * updates the height of the Node
     * balances it our if necessary
     * at the end of this process, the tree will be a balanced avl tree.
     *
     * @param newFather - the Node where we start updating the balancing the tree
     */
    private void balanceUp(Node newFather) {
        //until we have reached the root, keep going
        while (newFather != null) {
            //update the height of the node
            newFather.updateHeight();
            // balance the tree
            balanceTree(newFather);
            //keep going upp
            newFather = newFather.getFather();
        }
        // if we switched the root in the balancing process, then make sure we are set at the new root.
        if (root.getFather() != null)
            root = root.getFather();
    }

    /**
     * balances the node in accordance with the Avl principle. We have 4 cases for possible imbalance here:
     * RL,RR,LR,LL. all of them are dealt with in a different function. If the node does not need balancing,
     * then the function does nothing.
     *
     * @param startingNode - the node we wish to balance (if necessary)
     */
    private void balanceTree(Node startingNode) {
        /*
        we have 4 possible cases here - RR,LL,RL,LR where each of them depends on the AVLfactor,
         */
        if (startingNode.getAVLfactor() > LEFT_HEAVY) {
            //LL or LR.
            if (startingNode.getLeftSon().getAVLfactor() <= RIGHT_HEAVY) {
                //LR
                LRRotation(startingNode.getLeftSon());
                LLRotation(startingNode);

            } else
                //if (startingNode.getLeftSon().getAVLfactor() >= LEFT_HEAVY) {
                //LL
                LLRotation(startingNode);

        } else if (startingNode.getAVLfactor() < RIGHT_HEAVY) {
            //RR or RL
            if (startingNode.getRightSon().getAVLfactor() >= LEFT_HEAVY) {
                //RL
                RLRotation(startingNode.getRightSon());
                RRRotation(startingNode);
            } else
                //(startingNode.getRightSon().getAVLfactor() <= RIGHT_HEAVY) {
                //RR
                RRRotation(startingNode);

        }
    }

    /**
     * handles the LL type imbalance.
     * defines the pivot to be the left son of the node we are balancing. then:
     * sets the pivot's right son as the root's son (could be right or left)
     * sets the pivot to be the node's father
     * sets the roots father to be the node's father
     *
     * @param root the node which was found to be imbalanced
     */
    private void LLRotation(Node root) {

        Node pivot = root.getLeftSon();

        root.setLeftSon(pivot.getRightSon());
        if (pivot.getRightSon() != null)
            pivot.getRightSon().setFather(root);

        pivot.setRightSon(root);
        pivot.setFather(root.getFather());
        if (root.getFather() != null) {
            if (root.getFather().getLeftSon() == root) {
                root.getFather().setLeftSon(pivot);
            } else {
                root.getFather().setRightSon(pivot);
            }
        }
        root.setFather(pivot);

        root.updateHeight();
        pivot.updateHeight();
    }

    /**
     * handles the LR type imbalance.
     * first, makes the imbalance into an LL type, then calls pon the LL function to fix the imbalance
     *
     * @param root the node which was found to be imbalanced
     */
    private void LRRotation(Node root) {
        Node pivot = root.getRightSon();

        root.setRightSon(pivot.getLeftSon());
        if (pivot.getLeftSon() != null)
            pivot.getLeftSon().setFather(root);

        pivot.setLeftSon(root);
        pivot.setFather(root.getFather());
        if (root.getFather() != null) {
            if (root.getFather().getLeftSon() == root) {
                root.getFather().setLeftSon(pivot);
            } else {
                root.getFather().setRightSon(pivot);
            }
        }
        root.setFather(pivot);

        root.updateHeight();
        pivot.updateHeight();
    }

    /**
     * handles the RL type imbalance.
     * first, makes the imbalance into an RR type, then calls pon the RR function to fix the imbalance
     *
     * @param root the node which was found to be imbalanced
     */
    private void RLRotation(Node root) {
        Node pivot = root.getLeftSon();

        root.setLeftSon(pivot.getRightSon());
        if (pivot.getRightSon() != null)
            pivot.getRightSon().setFather(root);

        pivot.setRightSon(root);
        pivot.setFather(root.getFather());
        if (root.getFather() != null) {
            if (root.getFather().getLeftSon() == root) {
                root.getFather().setLeftSon(pivot);
            } else {
                root.getFather().setRightSon(pivot);
            }
        }
        root.setFather(pivot);

        root.updateHeight();
        pivot.updateHeight();
    }

    /**
     * handles the RR type imbalance.
     * defines the pivot to be the right soon of the node we are balancing. then:
     * sets the pivot's left son as the root's son (could be right or left)
     * sets the pivot to be the node's father
     * sets the roots father to be the node's father
     *
     * @param root the node which was found to be imbalanced
     */
    private void RRRotation(Node root) {
        Node pivot = root.getRightSon();

        root.setRightSon(pivot.getLeftSon());
        if (pivot.getLeftSon() != null)
            pivot.getLeftSon().setFather(root);

        pivot.setLeftSon(root);
        pivot.setFather(root.getFather());
        if (root.getFather() != null) {
            if (root.getFather().getLeftSon() == root) {
                root.getFather().setLeftSon(pivot);
            } else {
                root.getFather().setRightSon(pivot);
            }
        }
        root.setFather(pivot);

        root.updateHeight();
        pivot.updateHeight();
    }

    /**
     * @return the number of nodes in the tree
     */
    public int size() {
        return totalNodes;
    }

    /**
     * find the "succesor" of the node (the next in order by data node).
     *
     * @param nodeToCheck - the node for which we wish to find the successor.
     * @return The node's successor
     */
    private Node successor(Node nodeToCheck) {
        if (nodeToCheck.getRightSon() != null) {  // has right son - go and search for its most left node
            return findSmallestLeaf(nodeToCheck.getRightSon());
        }
        return findBiggestAncestor(nodeToCheck);//has no right son - return first ancestor to right
    }

    /**
     * finds the smallest leaf (read - the furthermost left leaf) of a subtree. works recursively by looking
     * until the subtree doesn't have any more left sons.
     *
     * @param node - the node for which we wish to find the smallest leaf
     * @return the smallest leaf.
     */
    private Node findSmallestLeaf(Node node) {
        if (node.getLeftSon() == null) {
            return node;
        }
        return findSmallestLeaf(node.getLeftSon());
    }

    /**
     * finds the Node's biggest ancestor
     *
     * @param node the node for which we wish to find it's largest ancestor
     * @return the node's ancestor
     */
    private Node findBiggestAncestor(Node node) {
        if (node.getFather() == null) { // only rightmost leaf in tree gets here
            return null;
        }
        if (node.isLeftSon()) { // node is a left son - return father
            return node.getFather();
        }
        return findBiggestAncestor(node.getFather());
    }

    /**
     * looks for a value in an AVL tree, by sing the properties of a binary tree (left subtree is smaller than
     * value, and right subtree is bigger than value). This function works recursively, by checking if the
     * value we are looking for is found at the current node, and if not, which subtree to keep looking in
     * it for.
     *
     * @param searchVal    the value we are searching for.
     * @param nodeToSearch - the node we are currently at.
     * @return the node if we found the value, and null otherwise
     */
    private Node findVal(int searchVal, Node nodeToSearch) { // return NODE (not int) of value found
        if (nodeToSearch.getData() == searchVal) {
            return nodeToSearch;
        }
        if (nodeToSearch.getData() < searchVal) { // if exists - its in right subtree
            if (nodeToSearch.getRightSon() == null) { // val not in tree
                return null;
            }
            return findVal(searchVal, nodeToSearch.getRightSon());
        } else { // if exists - its in left subtree
            if (nodeToSearch.getLeftSon() == null) {
                return null;
            }
            return findVal(searchVal, nodeToSearch.getLeftSon());
        }

    }

    /**
     * finds the depth of the Node recursively.
     *
     * @param node     - the node to find the depth of
     * @param minDepth - the current depth of the node we reached recursively
     * @return - the depth of the node (as opposed to the height, is measured from the top)
     */
    private int findDepth(Node node, int minDepth) {
        if (node.getFather() == null) {
            return minDepth;
        }
        return findDepth(node.getFather(), minDepth + 1);
    }

    /**
     * a helper function, meant to unite the "findVal" function which does the actual looking for a value,
     * with the contains functions, which receives an int to look for (not a node) and does not work
     * recursively by API design.
     *
     * @param searchVal   the value to search for (from the contains function)
     * @param subtreeRoot - the root of the subtree we wish to find the value in
     * @return the depth of the value if we found it, and 0 if we didn't
     */
    private int containsHelper(int searchVal, Node subtreeRoot) {
        if ((subtreeRoot == null) || (findVal(searchVal, subtreeRoot) == null)) {
            return -1;
        }
        Node nodeFound = findVal(searchVal, subtreeRoot);
        if (nodeFound == null)
            throw new NullPointerException();
        return findDepth(nodeFound, 0);
    }

    /**
     * Does tree contain a given input value.
     *
     * @param searchVal value to search for
     * @return if val is found in the tree, return the depth of its node (where 0 is the root).
     * Otherwise -- return -1.
     */
    public int contains(int searchVal) {

        return containsHelper(searchVal, root);
    }



    /**
     * handles the deleting of a leaf. Simply cuts off it from its father and re-balances from the father up
     *
     * @param leafToDelete the leaf we wish to delete
     * @return boolean 'true'
     */
    private boolean deleteLeaf(Node leafToDelete) {
        if (leafToDelete.getFather() == null) { // is root
            root = null; // make tree empty
        } else {
            balanceUp(leafToDelete.unLeafNode()); // balance tree
        }
        return true; // in both cases return true

    }

    /**
     * The method is called in case the root is deleted, and it has a single son.
     * After deleting the root, the method balances the tree.
     * @param node root Node object to delete
     * @return boolean 'true'
     */
    private boolean deleteRootWithSingleSon(Node node) {
        if (node.getRightSon() != null) { // root has right son
            node.getRightSon().setFather(null);
            root = node.getRightSon();
            balanceTree(node.getRightSon());
            return true;
        } // root has left son
        node.getLeftSon().setFather(null);
        node.getLeftSon().setFather(null);
        root = node.getLeftSon(); // update root pointer
        balanceTree(node.getLeftSon()); // balance the new tree
        return true;

    }

    /**
     * This method deletes a node with a single son.
     * The method finds the single son of the deleted node and connects it to the deleted nodes' father.
     * After the deletion - the new tree is balanced.
     * @param node Node object we wish to delete.
     * @return boolean 'true'
     */
    private boolean singleSonDelete(Node node) {
        Node fatherOfDeleted = node.getFather(); // deleted node is not the root, thus has father!
        if (node.getLeftSon() != null) { // deleted node has left son
            if (node.isLeftSon()) {
                fatherOfDeleted.setLeftSon(node.getLeftSon());
            } else {
                fatherOfDeleted.setRightSon(node.getLeftSon());
            }
            node.getLeftSon().setFather(fatherOfDeleted);
        } else if (node.getRightSon() != null) { // deleted node has right son
            if (node.isLeftSon()) {
                fatherOfDeleted.setLeftSon(node.getRightSon());
            } else {
                fatherOfDeleted.setRightSon(node.getRightSon());
            }
            node.getRightSon().setFather(fatherOfDeleted);

        }
        balanceUp(fatherOfDeleted); // balance the new tree
        return true;
    }

    /**
     * This method deletes a given node, which has two sons. The method finds the successor of the node to
     * delete and swaps their int data.
     * Then, the method recursively calls the deleteHelper() method on the successor, which has the data of
     * the node to delete.
     * After the recursive call to deleteHelper(), the method balances the new tree.
     * @param node Node object to delete. The Node input is of a Node with two sons.
     * @return boolean 'true'.
     */
    private boolean deleteFatherOfTwo(Node node) {
        Node successorOfDeleted = successor(node); // find the successor of the node to delete.
        int successorData = successorOfDeleted.getData();
        int deletedData = node.getData();
        // swap int data of node input and its successor
        node.setData(successorData);
        successorOfDeleted.setData(deletedData);
        deleteHelper(deletedData, node.getRightSon(), false); // recursive call to deleteHelper()
        if (node.getRightSon() == null) {
            balanceTree(node);
            return true;
        }
        balanceTree(node.getRightSon()); // balance the new tree
        return true;
    }

    /**
     * the helper function of the main delete function. Separates the problem into 3 sub problem, and calls the
     * appropriate function.
     *
     * @param NumToDelete   the number to delete
     * @param subTreeRoot   the subtree we wish to remove the value from
     * @param updateCounter a boolean value indicating if the counter of total number of nodes should be
     *                      updated or not, in case called recursively.
     * @return true iff toDelete found and deleted
     */
    private boolean deleteHelper(int NumToDelete, Node subTreeRoot, boolean updateCounter) {
        if (containsHelper(NumToDelete, subTreeRoot) == -1) { // cal not in tree -  return false
            return false;
        }
        if (updateCounter) {
            totalNodes--; // decrease -1 from total node counter of tree
        }
        Node nodeToDelete = findVal(NumToDelete, subTreeRoot);
        if (nodeToDelete.isLeaf()) { // if leaf
            return deleteLeaf(nodeToDelete);
        }
        if (nodeToDelete.hasSingleSon()) {
            if (nodeToDelete.getFather() == null) {
                return deleteRootWithSingleSon(nodeToDelete);
            }
            return singleSonDelete(nodeToDelete);
        } // made it here - deleted node has 2 sons
        return deleteFatherOfTwo(nodeToDelete);
    }

    /**
     * Remove a node from the tree, if it exists.
     * The method calls the recursive deleteHelper() method.
     *
     * @param toDelete value to delete
     * @return boolean true if value found and deleted
     */
    public boolean delete(int toDelete) {
        return deleteHelper(toDelete, root, true);
    }

    /**
     * @return the Node with the smallest value in the Tree. meaning, the leftmost leaf in the whole AVL tree.
     */
    private Node findMin() {
        Node min = new Node(root);
        while (min.getLeftSon() != null)
            min = min.getLeftSon();
        return min;
    }
}






