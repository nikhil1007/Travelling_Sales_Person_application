/*
Name: Nikhil Kashyap (nikhilka)
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
