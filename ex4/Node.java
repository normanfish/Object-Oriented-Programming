package oop.ex4.data_structures;

class Node {
    /**
     * Implements the "Node" class. Each Node contains information about it's parent, both its son, it height,
     * and the number it contains.
     */
    /* The parent Node */
    private Node father;

    /* The left son Node */
    private Node leftSon;

    /* The right son Node */
    private Node rightSon;

    /* The height of the node */
    private int height;

    /* The int data held by the node */
    private int data;

    /**
     * Node constructor. receives information about the node's father, son's and data, and creates a new Node
     * for those values
     *
     * @param fatherNode   - the father of the node to be created
     * @param leftSonNode  - the Node's left son
     * @param rightSonNode - the Node's right son
     * @param dataInput    - the data the node holds.
     */
    Node(Node fatherNode, Node leftSonNode, Node rightSonNode, int dataInput) {
        father = fatherNode;
        leftSon = leftSonNode;
        rightSon = rightSonNode;
        data = dataInput;
    }

    /**
     * Copy constructor. receives a Node and copies all of its fields to create a new node (different address
     * in memory, but pointing to the same father and sons).
     *
     * @param copyNode - the Node from which we will copy the data
     */
    Node(Node copyNode) {
        father = copyNode.getFather();
        data = copyNode.getData();
        leftSon = copyNode.getLeftSon();
        rightSon = copyNode.getRightSon();
    }

    /**
     * Default constructor, creates a new empty node
     */
    Node() {
        father = null;
        leftSon = null;
        rightSon = null;
        data = 0;
    }

    /**
     * creates a new node with no parents and sets the data parameter to be the input parameter
     *
     * @param newData - the data to be set.
     */
    Node(int newData) {
        father = null;
        leftSon = null;
        rightSon = null;
        data = newData;
    }

    /**
     * @return the node's father
     */
    Node getFather() {
        return father;
    }

    /**
     * sets the Node's father
     *
     * @param newFather - the new father
     */
    void setFather(Node newFather) {
        this.father = newFather;
    }

    /**
     * @return - the Node's height in the tree
     */
    private int getHeight() {
        return height;
    }

    /**
     * sets the Node's height
     *
     * @param newHeight - the new height
     */
    void setHeight(int newHeight) {
        this.height = newHeight;
    }

    /**
     * @return - the Node's data
     */
    int getData() {
        return data;
    }

    /**
     * sets the Node's data
     *
     * @param newData - the new data
     */
    public void setData(int newData) {
        this.data = newData;
    }


    public String toString() {
        return Integer.toString(data);
    }

    /**
     * @return - the node's left son
     */
    Node getLeftSon() {
        return leftSon;
    }

    /**
     * @return- the node's right son
     */
    Node getRightSon() {
        return rightSon;
    }

    /**
     * sets the left son of the node to the input parameter
     *
     * @param newLeftSon - the new left son
     */
    void setLeftSon(Node newLeftSon) {
        leftSon = newLeftSon;
    }

    /**
     * sets the right son of the node to the input parameter
     *
     * @param newRightSon - the new right son
     */
    void setRightSon(Node newRightSon) {
        rightSon = newRightSon;
    }

    /**
     * updates the height of the node, based on the height of its sons.
     * if the node has no sons at all, it's a leaf and therefor has a height of 0.
     * if a node only has a right or a let son, then it's height is the height of it's son + 1.
     * if the node has two sons, then its height is the maximum between the two sons plus one.
     */
    void updateHeight() {
        //the node is a leaf - that means height zero
        if (this.getLeftSon() == null && this.getRightSon() == null) {
            this.setHeight(0);
        }
        //we only have a right son
        else if (this.getLeftSon() == null && this.getRightSon() != null) {
            this.setHeight(this.getRightSon().getHeight() + 1);
        }
        // only have a left son
        else if (this.getLeftSon() != null && this.getRightSon() == null) {
            this.setHeight(this.getLeftSon().getHeight() + 1);
        }
        // has two sons, we'll take the maximum of the two
        else {
            this.setHeight(Math.max(this.getRightSon().getHeight(),
                    this.getLeftSon().getHeight()) + 1);
        }
    }

    /**
     * @return - the function returns the "balance factor" of the node. meaning, the difference between the
     * height of its left son and its right son. the height is defined to start from the left (meaning, a
     * right heavy tree will have a negative AVL factor).
     */
    int getAVLfactor() {
        //the node is a leaf, so it has a balance factor of 0.
        if (this.getLeftSon() == null && this.getRightSon() == null) {
            return 0;
        }
        //we only have a right son
        else if (this.getLeftSon() == null && this.getRightSon() != null) {

            return -1 * (this.getRightSon().getHeight() + 1);
        }
        // only have a left son
        else if (this.getLeftSon() != null && this.getRightSon() == null) {
            return this.getLeftSon().getHeight() + 1;
        }
        // has two sons, we'll take the left son's height minus the right son's height
        else {
            return this.getLeftSon().getHeight() - this.getRightSon().getHeight();
        }
    }

    /**
     * @return - true if the node is a leaf, false otherwise.
     */
    boolean isLeaf() {
        return ((getLeftSon() == null) && (getRightSon() == null));
    }

    /**
     * @return true if the Node is a left son of it's father, and false otherwise.
     */
    boolean isLeftSon() { // true if node is a left son
        if (getFather() == null) { // if root
            return false;
        }
        return this == getFather().getLeftSon();
    }

    /**
     * This function handles "removing" a leaf from the tree. It sets the leafs father to point to null, and
     * sets the father of the leaf to null.
     *
     * @return - the father of the leaf, which has been stripped of it's leaf son.
     */
    Node unLeafNode() { // assumes node is a leaf
        if (this.isLeftSon()) {
            this.getFather().setLeftSon(null);
        } else {
            this.getFather().setRightSon(null);
        }
        Node origianlFather = this.getFather();
        this.setFather(null);
        return origianlFather;
    }

    /**
     * checks if the node in question has a single son
     *
     * @return - true if the node has a single son.
     */
    boolean hasSingleSon() {
        if (this.getFather() == null) {
            if ((this.getLeftSon() == null) || (this.getRightSon() == null)) {
                return true; // also returns treu when root leaf but doesnt matter
            }
            return false;
        }
        if ((this.getLeftSon() != null) && (this.getRightSon() != null)) {
            return false;
        }
        return true;
    }


}








