/*
Name: Nikhil Kashyap (nikhilka)
 */
import java.util.LinkedList;

/**
 * This class represents the Graph, constructed after filtering records by start and end date.
 */
public class Graph {
    /**
     * adjacencyMatrix - holds the distance matrix
     */
    private final double[][] adjacencyMatrix;

    public Graph(int size){
        adjacencyMatrix = new double[size][size];
    }

    /**
     * Constructs the graph from set of GraphNodes
     * @param vertices
     *  A list of graphNodes obtained after filtering based on start and end date
     */
    public void generateAdajacencyList(LinkedList<GraphNode> vertices){
        double x1,y1,x2,y2;

        for(int i = 0 ; i < vertices.size(); i++){
            x1 = vertices.get(i).getX_coordinate();
            y1 = vertices.get(i).getY_coordinate();

            for(int j = 0; j < vertices.size(); j++){
                x2 = vertices.get(j).getX_coordinate();
                y2 = vertices.get(j).getY_coordinate();
                adjacencyMatrix[i][j] = calculateDistance(x1,y1,x2,y2);
            }
        }
    }

    public double[][] getAdjacencyMatrix(){
        return adjacencyMatrix;
    }

    /**
     * Returns the Node Numbers of the adjacent nodes to node "Vertex" in the graph
     * @precondition
     *  The distance matrix for the graph is computed
     * @param MST
     *  This is used to filter adjacent nodes as we are not interested in them if they are already in MST
     * @param vertex
     *  The GraphNode who's adjacent vertices we are returning back
     * @return
     *  List of vertex's adjacent node's nodeNumber
     */
    public LinkedList<Integer> getAdjacentVertices(LinkedList<Integer> MST, int vertex){
        LinkedList<Integer> vertices = new LinkedList<>();

        for(int i = 0; i < adjacencyMatrix.length; i++){
            if(i != vertex && !MST.contains(i)){
                vertices.add(i);
            }
        }

        return vertices;
    }

    /**
     * Returns the distances of the adjacent nodes from "Vertex" in the graph
     * @precondition
     *  The distance matrix for the graph is computed and adjacent node numbers of vertex are determined
     * @param vertices
     *  The list of adjacent vertices' nodeNumbers of Vertex
     * @param vertex
     *  The GraphNode from who's distance we are returning
     * @return
     *  List of distances of adjacent nodes from vertex
     */
    public LinkedList<Double> getAdjacentDistances(LinkedList<Integer> vertices, int vertex){
        LinkedList<Double> distances = new LinkedList<>();

        for (Integer adjVertex : vertices) {
            distances.add(adjacencyMatrix[vertex][adjVertex]);
        }

        return distances;
    }

    /**
     * Calculates distance between 2 points using pythagorean theorem
     * @return
     *  Distance between points (x1,y1) and (x2,y2) in miles
     */
    public double  calculateDistance (double  x1, double  y1, double  x2, double  y2){
        return  (Math.pow(Math.pow((x2- x1), 2) + Math.pow((y2 - y1), 2) , 0.5)) * 0.00018939;
    }
}
