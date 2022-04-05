/*
Name: Nikhil Kashyap (nikhilka)
Assignment: DSA Project 4 - Traveveling Sales Person, Minimum Spanning Tree, Heaps and Graphs
Problem Statement: https://www.andrew.cmu.edu/user/mm6/95-771/Homeworks/Homework4Graphs/S22-project4-TSP.pdf
 */

public class HeapNode {
    private double distance;
    private int nodeNumber;
    private int parent;
    private HeapNode parentNode;

    public HeapNode(double distance, int nodeNumber, int parent){
        this.distance = distance;
        this.nodeNumber = nodeNumber;
        this.parent = parent;
    }

    //------------------------------- Getters and Setters ---------------------------

    public HeapNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(HeapNode parentNode) {
        this.parentNode = parentNode;
    }

    public int getParent() { return parent; }

    public void setParent(int parent) { this.parent = parent;}

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

}
