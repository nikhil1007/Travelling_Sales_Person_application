/*
Name: Nikhil Kashyap (nikhilka)
Assignment: DSA Project 4 - Traveveling Sales Person, Minimum Spanning Tree, Heaps and Graphs
Problem Statement: https://www.andrew.cmu.edu/user/mm6/95-771/Homeworks/Homework4Graphs/S22-project4-TSP.pdf
 */

import java.util.LinkedList;

/**
 * This class builds the Minimum Spanning Tree using Prim's Algorithm for a given weighted, undirected graph
 */
public class PrimMST {
    /**
     * MST - Tracks nodeNumbers in the Minimum Spanning Tree at a given time
     * SpanningTree - Matrix representation of the MST constructed
     * minHeap - an object of min Heap used to get the least costly edge to add to MST
     */
    private final LinkedList<Integer> MST;
    static int[][] SpanningTree;
    MinHeap minHeap;

    // A parameterized constructor to initialize the declared variables
    public PrimMST(int size){
        MST = new LinkedList<>();
        SpanningTree = new int[size][size];

        for (int i = 0; i < SpanningTree.length; i++){
            if(i != 0){
                SpanningTree[0][i] = 1;
            }
        }
    }

    public void AddToMST(HeapNode node){
        MST.add(node.getNodeNumber());
    }

    /**
     * Builds the initial min heap with node 0 and distance 0 along with all other nodes with distance INF
     * @precondition
     *  min heap class is defined with all appropriate methods
     * @param size
     *  The size of the min heap
     * @return
     *   Object of the min heap constructed
     */
    public MinHeap generateMinHeap(int size){
        minHeap = new MinHeap(size);
        for(int i = 0 ; i < size; i++){
            if(i == 0){
                minHeap.insertToHeap(new HeapNode(0, i, -1));
            }
            else {
                minHeap.insertToHeap(new HeapNode(Double.MAX_VALUE, i,0));
            }
        }
        return minHeap;
    }

    /**
     * The method updates the distance value of adjacent nodes of the node added to Minimum Spanning Tree
     * @precondition
     *  The distance matrix for the graph and min Heap is constructed
     * @param adjacentVertices
     *  NodeNumbers of adjacent nodes
     * @param adjacentDistances
     *  Distances of each adjacent node in order
     * @param vertex
     *  The node from which adjacent nodes are being evaluated
     */
    public void UpdateAdjacentVertices(LinkedList<Integer> adjacentVertices, LinkedList<Double> adjacentDistances, HeapNode vertex){

        for (int i = 0; i < adjacentVertices.size(); i++) {
            HeapNode heapNode = minHeap.getHeapNode(adjacentVertices.get(i));

            if (heapNode.getDistance() > adjacentDistances.get(i)) {
                int previousParent = heapNode.getParent();
                int currentParent = vertex.getNodeNumber();

                if(previousParent != currentParent){
                    SpanningTree[previousParent][heapNode.getNodeNumber()] = 0;
                    SpanningTree[heapNode.getNodeNumber()][previousParent] = 0;

                    SpanningTree[currentParent][heapNode.getNodeNumber()] = 1;
                    SpanningTree[heapNode.getNodeNumber()][currentParent] = 1;
                }

                minHeap.DecreaseDistance(heapNode, adjacentDistances.get(i), vertex.getNodeNumber());
            }
        }
    }

    public LinkedList<Integer> getMST(){
        return MST;
    }
}