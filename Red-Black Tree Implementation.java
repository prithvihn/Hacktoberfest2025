/**
 * Red-Black Tree Implementation
 * A self-balancing binary search tree with guaranteed O(log n) operations
 * 
 * Properties:
 * 1. Every node is either red or black
 * 2. Root is always black
 * 3. All leaves (NIL) are black
 * 4. Red nodes have black children
 * 5. All paths from root to leaves contain same number of black nodes
 * 
 * @author Hacktoberfest 2025 Contributor
 */

class Node {
    int data;
    Node parent;
    Node left;
    Node right;
    int color; // 1 for Red, 0 for Black
    
    public Node(int data) {
        this.data = data;
        this.color = 1; // New nodes are red by default
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}

public class RedBlackTree {
    private Node root;
    private Node TNULL;
    
    public RedBlackTree() {
        TNULL = new Node(0);
        TNULL.color = 0;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }
    
    // Pre-order traversal
    private void preOrderHelper(Node node) {
        if (node != TNULL) {
            System.out.print(node.data + " ");
            preOrderHelper(node.left);
            preOrderHelper(node.right);
        }
    }
    
    // In-order traversal
    private void inOrderHelper(Node node) {
        if (node != TNULL) {
            inOrderHelper(node.left);
            System.out.print(node.data + " ");
            inOrderHelper(node.right);
        }
    }
    
    // Search for a key
    private Node searchTreeHelper(Node node, int key) {
        if (node == TNULL || key == node.data) {
            return node;
        }
        
        if (key < node.data) {
            return searchTreeHelper(node.left, key);
        }
        return searchTreeHelper(node.right, key);
    }
    
    // Fix violations after insertion
    private void fixInsert(Node k) {
        Node u;
        while (k.parent.color == 1) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;
                if (u.color == 1) {
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right;
                
                if (u.color == 1) {
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = 0;
    }
    
    // Left rotation
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }
    
    // Right rotation
    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }
    
    // Insert a node
    public void insert(int key) {
        Node node = new Node(key);
        node.parent = null;
        node.data = key;
        node.left = TNULL;
        node.right = TNULL;
        node.color = 1;
        
        Node y = null;
        Node x = this.root;
        
        while (x != TNULL) {
            y = x;
            if (node.data < x.data) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        
        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.data < y.data) {
            y.left = node;
        } else {
            y.right = node;
        }
        
        if (node.parent == null) {
            node.color = 0;
            return;
        }
        
        if (node.parent.parent == null) {
            return;
        }
        
        fixInsert(node);
    }
    
    // Search for a key
    public Node search(int k) {
        return searchTreeHelper(this.root, k);
    }
    
    // Get minimum node
    public Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }
    
    // Get maximum node
    public Node maximum(Node node) {
        while (node.right != TNULL) {
            node = node.right;
        }
        return node;
    }
    
    // Print tree structure
    private void printHelper(Node root, String indent, boolean last) {
        if (root != TNULL) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "     ";
            } else {
                System.out.print("L----");
                indent += "|    ";
            }
            
            String sColor = root.color == 1 ? "RED" : "BLACK";
            System.out.println(root.data + "(" + sColor + ")");
            printHelper(root.left, indent, false);
            printHelper(root.right, indent, true);
        }
    }
    
    // Public methods
    public void preorder() {
        preOrderHelper(this.root);
    }
    
    public void inorder() {
        inOrderHelper(this.root);
    }
    
    public void printTree() {
        printHelper(this.root, "", true);
    }
    
    // Main method for testing
    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        
        System.out.println("=== Red-Black Tree Implementation ===\n");
        
        // Insert nodes
        int[] values = {55, 40, 65, 60, 75, 57, 30, 45};
        System.out.println("Inserting values: ");
        for (int value : values) {
            System.out.print(value + " ");
            tree.insert(value);
        }
        System.out.println("\n");
        
        // Print tree structure
        System.out.println("Tree Structure:");
        tree.printTree();
        
        // In-order traversal
        System.out.println("\nIn-order Traversal:");
        tree.inorder();
        System.out.println();
        
        // Search for a value
        int searchKey = 60;
        Node result = tree.search(searchKey);
        System.out.println("\nSearching for " + searchKey + ": " + 
                         (result != tree.TNULL ? "Found!" : "Not Found"));
        
        // Find minimum and maximum
        Node min = tree.minimum(tree.root);
        Node max = tree.maximum(tree.root);
        System.out.println("\nMinimum value: " + min.data);
        System.out.println("Maximum value: " + max.data);
    }
}
