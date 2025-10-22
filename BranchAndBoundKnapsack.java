import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Title: BranchAndBoundKnapsack.java
 * Description: Solves the 0/1 Knapsack Problem using the Branch and Bound algorithm.
 * The Branch and Bound technique uses a Priority Queue (Best-First Search) 
 * to efficiently find the optimal solution by calculating an upper bound (UB)
 * and pruning nodes whose UB is less than the current best solution (max profit).
 */
public class BranchAndBoundKnapsack {

    // Helper class to represent an item with its value, weight, and value-to-weight ratio
    static class Item {
        int value;
        int weight;
        double ratio;

        public Item(int value, int weight) {
            this.value = value;
            this.weight = weight;
            this.ratio = (double) value / weight;
        }
    }

    // Helper class to represent a node in the state-space tree
    static class Node {
        int level; // The index of the item being considered (level in the tree)
        int profit; // Total value/profit of items included up to this level
        int weight; // Total weight of items included up to this level
        double bound; // The maximum possible profit that can be achieved from this node

        public Node(int level, int profit, int weight) {
            this.level = level;
            this.profit = profit;
            this.weight = weight;
        }
    }

    /**
     * Calculates the upper bound (maximum possible profit) from the current node 
     * using the fractional knapsack greedy approach on remaining items.
     * * @param u The current node.
     * @param n Total number of items.
     * @param W Knapsack capacity.
     * @param items Array of all items.
     */
    private static void calculateBound(Node u, int n, int W, Item[] items) {
        // If the node is the last level, no more items can be added, bound is just current profit
        if (u.level == n) {
            u.bound = u.profit;
            return;
        }

        // Initialize bound with current profit
        u.bound = u.profit;
        int j = u.level;
        int totalWeight = u.weight;

        // Apply greedy fractional approach for remaining items
        while (j < n && totalWeight + items[j].weight <= W) {
            totalWeight += items[j].weight;
            u.bound += items[j].value;
            j++;
        }

        // If capacity remains, add a fraction of the next item
        if (j < n) {
            u.bound += (W - totalWeight) * items[j].ratio;
        }
    }

    /**
     * Solves the 0/1 Knapsack problem using the Branch and Bound method.
     * * @param W Knapsack capacity.
     * @param values Array of item values.
     * @param weights Array of item weights.
     * @return The maximum profit that can be achieved.
     */
    public static int knapsackBranchAndBound(int W, int[] values, int[] weights) {
        int n = values.length;

        // 1. Prepare items and sort by value/weight ratio (Greedy approach preparation)
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(values[i], weights[i]);
        }
        // Sort items in descending order of value/weight ratio
        Arrays.sort(items, Comparator.comparingDouble((Item a) -> a.ratio).reversed());

        // 2. Priority Queue for Best-First Search
        // Stores nodes and prioritizes the one with the highest bound (max-heap)
        PriorityQueue<Node> pq = new PriorityQueue<>(
            Comparator.comparingDouble((Node node) -> node.bound).reversed()
        );

        // 3. Initialize the root node
        Node root = new Node(0, 0, 0);
        calculateBound(root, n, W, items);
        pq.add(root);

        int maxProfit = 0; // The best profit found so far

        // 4. Branch and Bound main loop
        while (!pq.isEmpty()) {
            Node u = pq.poll();

            // Pruning: If the node's bound is less than the current max profit, 
            // no optimal solution can be found down this branch, so skip it.
            if (u.bound < maxProfit) {
                continue;
            }
            
            // Branching Level Check
            if (u.level == n) {
                continue; // Reached end of tree (shouldn't happen with the bound check, but safe)
            }
            
            int nextItemIndex = u.level;

            // --- BRANCH 1: Include the next item ---
            int inclWeight = u.weight + items[nextItemIndex].weight;
            int inclProfit = u.profit + items[nextItemIndex].value;

            if (inclWeight <= W) {
                Node v = new Node(nextItemIndex + 1, inclProfit, inclWeight);
                
                // Update maxProfit if we found a better full solution
                if (v.profit > maxProfit) {
                    maxProfit = v.profit;
                }
                
                calculateBound(v, n, W, items);
                
                // Pruning check: Only add to PQ if it's potentially better
                if (v.bound >= maxProfit) {
                    pq.add(v);
                }
            }
            
            // --- BRANCH 2: Exclude the next item ---
            // The profit and weight remain the same as the parent node u
            Node w = new Node(nextItemIndex + 1, u.profit, u.weight);
            calculateBound(w, n, W, items);
            
            // Pruning check: Only add to PQ if it's potentially better
            if (w.bound >= maxProfit) {
                pq.add(w);
            }
        }
        
        return maxProfit;
    }

    // --- Main method for testing the implementation ---
    public static void main(String[] args) {
        int W = 15; // Knapsack capacity
        int[] values = {10, 50, 40, 70};
        int[] weights = {4, 6, 5, 8};
        
        System.out.println("Knapsack Capacity (W): " + W);
        System.out.println("Items (Value, Weight):");
        for (int i = 0; i < values.length; i++) {
            System.out.println("  Item " + (i + 1) + ": (" + values[i] + ", " + weights[i] + ")");
        }

        int maxProfit = knapsackBranchAndBound(W, values, weights);
        
        System.out.println("\nMaximum Profit using Branch and Bound: " + maxProfit); // Expected: 120 (Items with V=50, W=6 and V=70, W=8 are taken)
    }
}
