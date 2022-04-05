/*
Name: Nikhil Kashyap (nikhilka)
Assignment: DSA Project 4 - Traveveling Sales Person, Minimum Spanning Tree, Heaps and Graphs
Problem Statement: https://www.andrew.cmu.edu/user/mm6/95-771/Homeworks/Homework4Graphs/S22-project4-TSP.pdf
 */

public class GraphNode {
    private double x_coordinate;
    private double y_coordinate;
    private String data;
    private int nodeNumber;
    private double distance;

    public GraphNode (double x_coordinate, double y_coordinate, String data, int nodeNumber){
        setX_coordinate(x_coordinate);
        setY_coordinate(y_coordinate);
        setData(data);
        setNodeNumber(nodeNumber);
    }

    public String toString(){
        return " distance : " + distance;
    }

    //---------------------------------- Getters and Setters for instance variables of the class ---------------------
    public double getX_coordinate() {
        return x_coordinate;
    }

    public void setX_coordinate(double x_coordinate) {
        this.x_coordinate = x_coordinate;
    }

    public double getY_coordinate() {
        return y_coordinate;
    }

    public void setY_coordinate(double y_coordinate) {
        this.y_coordinate = y_coordinate;
    }

    public String getData() { return data;}

    public void setData(String data) {
        this.data = data;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }
}
