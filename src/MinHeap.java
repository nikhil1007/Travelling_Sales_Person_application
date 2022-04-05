/*
Name: Nikhil Kashyap (nikhilka)
Assignment: DSA Project 4 - Traveveling Sales Person, Minimum Spanning Tree, Heaps and Graphs
Problem Statement: https://www.andrew.cmu.edu/user/mm6/95-771/Homeworks/Homework4Graphs/S22-project4-TSP.pdf
 */

/**
 * A Min-Heap is a complete binary tree in which the value in each internal node is smaller than or equal to the values
 * in the children of that node.
 */
public class MinHeap {
    /**
     * current_size - live size of the node as in decreases
     * minHeap - the minimum heap itself represented in a 1D array
     */
    private int current_size;
    HeapNode[] minHeap;

    public MinHeap(int maxsize){
        current_size = 0;
        minHeap = new HeapNode[maxsize];
    }

    /**
     * Inserts a new element into the min heap. Steps:
     *  a) Insert element to the end of heap
     *  b) Move the element up the tree until its parent is smaller than itself
     * @precondition
     *  The minHeap is initialized and current_size < size of heap
     * @param node
     *  Node of type HeapNode
     */
    public void insertToHeap(HeapNode node){
        minHeap[current_size] = node;
        int current = current_size;

        while (minHeap[current].getDistance() < minHeap[getParent(current)].getDistance()){
            swap(current, getParent(current));
            current = getParent(current);
        }
        current_size ++;
    }

    /**
     * Modifies the distance in the heapNode and re-adjusts the heap to be a minHeap
     * @precondition
     *  The min Heap is constructed, the newNode is in min Heap already
     * @param newNode
     *  Heap node to decrease the distance on
     * @param distance
     *  The new distance value
     * @param parent
     *  Parent of the current node who's distance is being modified
     */
    public void DecreaseDistance(HeapNode newNode, double distance, int parent){
        int index = getHeapIndex(newNode.getNodeNumber());
        newNode.setDistance(distance);
        newNode.setParent(parent);
        minHeap[index] = newNode;

        while (index != 0 && minHeap[getParent(index)].getDistance() > minHeap[index].getDistance()){
            swap(index, getParent(index));
            index = getParent(index);
        }

    }

    /**
     * This function helps repair the min heap after removing an element from the Heap.
     * @precondition
     *  The min Heap is constructed
     * @param index
     *  The nodeNumber on which the min heap properties are to be enforced on
     */
    public void heapify(int index) {
        if(!isLeaf(index)){
            if(minHeap[index].getDistance() > minHeap[getLeft(index)].getDistance() ||
                    minHeap[index].getDistance() > minHeap[getRight(index)].getDistance()){

                if(minHeap[getLeft(index)].getDistance() < minHeap[getRight(index)].getDistance()){
                    swap(index, getLeft(index));
                    heapify(getLeft(index));
                }
                else {
                    swap(index, getRight(index));
                    heapify(getRight(index));
                }
            }
        }
    }

    /**
     * Deletes the minimum element from the min Heap i.e. the root
     * @precondition
     *  minHeap is initialized and constructed, current_size > 0
     * @return
     *  Minimum element from the heap
     */
    public HeapNode deleteMin(){
        HeapNode popped = minHeap[0];
        minHeap[0] = minHeap[current_size - 1];
        current_size = current_size - 1;
        heapify(0);
        return popped;
    }

    /**
     * Returns the HeapNode from heap for a given NodeNumber
     * @precondition
     *  The min heap is constructed and initialized, the current_size > 0
     * @param nodeNumber
     *  The nodeNumber of a node in the minHeap
     * @return
     *  A heapNode with the specfied node Number
     */
    public HeapNode getHeapNode(int nodeNumber){
        HeapNode node = null;
        for(int i = 0 ; i < current_size; i ++){
            if(minHeap[i].getNodeNumber() == nodeNumber){
                node = minHeap[i];
                break;
            }
        }
        return node;
    }

    /**
     * Returns the index of HeapNode in heap for a given NodeNumber
     * @precondition
     *  The min heap is constructed and initialized, the current_size > 0
     * @param nodeNumber
     *  The nodeNumber of a node in the minHeap
     * @return
     *  Index of HeapNode with specified nodeNumber in the min Heap
     */
    public int getHeapIndex(int nodeNumber){
        int index = 0;
        for(int i = 0 ; i < minHeap.length; i ++){
            if(minHeap[i].getNodeNumber() == nodeNumber){
                index = i;
                break;
            }
        }
        return index;
    }

    //------------------------------------- Helper Methods to implement functions of the min Heap ----------------------------
    public int getParent(int childIndex){
        return (childIndex - 1)/2 ;
    }

    public int getRight(int RootIndex){
        return (2 * RootIndex) + 2;
    }

    public int getLeft(int RootIndex){
        return  (2 * RootIndex) + 1;
    }

    public boolean isLeaf(int index){
        int left = getLeft(index);
        int right = getRight(index);

        return left > current_size || right > current_size || ((index > (current_size / 2)) && index <= current_size);
    }

    public void swap (int index_1, int index_2){
        HeapNode temp;
        temp = minHeap[index_1];
        minHeap[index_1] = minHeap[index_2];
        minHeap[index_2] = temp;
    }
    public boolean isEmpty(){
        return current_size == 0;
    }

}
