/*
Name: Nikhil Kashyap (nikhilka)
Traveveling Sales Person, Minimum Spanning Tree, Heaps and Graphs
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/*
        Solving Travelling Salesman Problem using Approximation and Brute force. We make use of the algorithm stated in “Introduction to Algorithms”
        by Cormen,Lieserson, Rivest and Stein.

        Overview of TravelingSalesPerson.java -
        a) Reads the csv file and filters records based on input dates
        b) Generate graph of these nodes
        c) Generate prim's MST from graph
        d) Generate preorder of MST and find Hamiltonian cycle in that order.
        e) We also look at the brute force approach by inspecting all possible routes
*/
public class TravelingSalesPerson {
    /**
     * nodeNumber - used to initially assign each record an Integer identifier
     * totalDistance - holds the distance travelled in the path determined by Hamiltonian cycle.
     * graph - Holds distance matrix and information about each node (object of Graph.java)
     * primMST - an object of PrimMST.java which aids in generating the Prim's MST
     * bf - object of the class that implements the brute force solution
     * visited[] - a boolean array to track visited node during traversal
     * preOrderWalk - stores the preOrder traversal of Prim's MST
     * vertices - List of GraphNodes i.e nothing but vertices of our graph but with additional information on X,Y etc.
     */
    int nodeNumber;
    double totalDistance;
    Graph graph;
    PrimMST primMST;
    BruteForce bf;
    private final boolean[] visited;
    private LinkedList<Integer> preOrderWalk;
    LinkedList<GraphNode> vertices;

    /**
     * Parameterized constructor which invokes function which initializes variables and invokes functions in the right order
     */
    public TravelingSalesPerson(String filename,String start_date, String end_date) throws IOException {
        nodeNumber = 0;
        totalDistance = 0;
        vertices = new LinkedList<>();
        readFile(filename, start_date, end_date);

        graph = new Graph(vertices.size());
        generateGraph();

        primMST = new PrimMST(vertices.size());
        generatePrimMST();

        visited = new boolean[vertices.size()];
        preOrderWalk = new LinkedList<>();
        preOrderWalk();

        bf = new BruteForce(graph.getAdjacencyMatrix());
        generateBruteForceSolution();

        generateKML();
        createKMLfile();
    }

    /**
     * Reads information from the specified filename and generates graphNodes.
     * @precondition
     *  The filename provided exists and is accessible. It is also in the format assumed in the sample csv file in problem statement
     *  start_date <= end_date, and they are in mm/dd/yy format with no fillers (like 0)
     * @param file
     *  File name to read the crime records
     * @param start_date
     *  Lower bound of our search criteria
     * @param end_date
     *  Upper bound of our search criteria
     */
    public void readFile(String file, String start_date, String end_date){
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);

            String[] start = start_date.split("/");
            String[] end = end_date.split("/");
            int s_month = Integer.parseInt(start[0]);
            int e_month = Integer.parseInt(end[0]);
            int s_day = Integer.parseInt(start[1]);
            int e_day = Integer.parseInt(end[1]);

            myReader.nextLine(); //consuming the header of csv file

            System.out.println();
            System.out.println("Crime records between " + start_date + " and " + end_date);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] tokens = data.split(",");
                String[] date = tokens[5].split("/");
                int month = Integer.parseInt(date[0]);
                int day = Integer.parseInt(date[1]);

                if((s_month <= month && month <= e_month) &&
                        (s_day <= day && day <= e_day)) {
                     vertices.add(new GraphNode(Double.parseDouble(tokens[0]),Double.parseDouble(tokens[1]),data,nodeNumber++));
                    System.out.println(data);
                }
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error: File not found exception.");
            e.printStackTrace();
        }
    }

    /**
     * Using the vertices obtained, it generates a distance matrix between these graphNodes. The distances are calculated
     * with the help of pythagorean theorem.
     * @preconditions
     *  The vertices list is populated with graphNodes after filtering records by the start and end date
     */
    public void generateGraph(){
        graph.generateAdajacencyList(vertices);
    }

    /**
     * From the graph generated, this function determines the Prim's Minimum Spanning Tree of the graph. Steps involved:
     *      a) Create a minHeap with node 0 with distance 0 and all the others nodes with distance INF
     *      b) We keep polling the minimum edge from minHeap and adding it to MST while keeping track of the parent node
     *      c) We update the adjacent nodes in heap with their minimum distances along the way as and when we add edge to MST
     * @preconditions
     *  A graph (distance matrix) is generated from the graphNodes with valid values.
     */
    public void generatePrimMST(){
        MinHeap minHeap = primMST.generateMinHeap(vertices.size());

        while (!minHeap.isEmpty()){
            HeapNode heapNode = minHeap.deleteMin();
            primMST.AddToMST(heapNode);
            LinkedList<Integer> adj_vertices = graph.getAdjacentVertices(primMST.getMST(), heapNode.getNodeNumber());
            LinkedList<Double> adj_distances = graph.getAdjacentDistances(adj_vertices, heapNode.getNodeNumber());
            primMST.UpdateAdjacentVertices(adj_vertices, adj_distances, heapNode);
        }
    }

    /**
     * Finds the PreOrder walk of the Prim's Minimum Spanning tree constructed earlier : Root LeftNode RightNode. Since
     * it is not a typical binary tree, we implement a modified DFS algorithm to determine the cycle.
     * @precondition
     *  The Prim's Minimum Spanning Tree is constructed for the graph (Graph)
     */
    public void preOrderWalk(){
        preOrderWalk = new LinkedList<>();
        visited[0] = true;
        preOrderWalk.add(0);
        double ans = hamiltonCycle(0);

        System.out.println();
        System.out.println("Hamiltonian Cycle (not necessarily optimum)" );
        for (Integer nodes : preOrderWalk){
            System.out.print(nodes + " ");
        }

        System.out.println();
        System.out.println("Length Of Cycle: " + ans + " miles");
        System.out.println();
    }

    /**
     * Determines the Hamilton cycle of the Minimum Spanning Tree generated. The steps are:
     *  a) Start of node 0 and do DFS on the tree until all the nodes are visited
     *  b) Calculate the distance travelled while doing (1)
     *  c) At the end we need to determine the best possible way to get back to 0 by inspecting the path (if exists) or
     *     travel back the path traversed (worst case).
     * @precondition
     *  The Prim's Minimum Spanning tree is constructed for the graph
     * @param nodeNumber
     *  The node identifier which indicates which node we are inspecting (at each phase of recursion)
     * @return
     *  Total Distance travelled when visiting all vertices and returning to starting point
     */
    public double hamiltonCycle( int nodeNumber)
    {
        if (preOrderWalk.size() == vertices.size())
        {
            if(PrimMST.SpanningTree[nodeNumber][0] > 0){
                totalDistance += Math.min(graph.getAdjacencyMatrix()[nodeNumber][0], totalDistance);
            }
            else {
                totalDistance += totalDistance;
            }
            preOrderWalk.add(0);
            return totalDistance;
        }

        for (int i = 0; i < vertices.size(); i++)
        {
            if (!visited[i] && PrimMST.SpanningTree[nodeNumber][i] > 0)
            {
                visited[i] = true;
                preOrderWalk.add(i);
                totalDistance += graph.getAdjacencyMatrix()[nodeNumber][i];
                hamiltonCycle(i);
                visited[i] = false;
            }
        }
        return totalDistance;
    }


    public void generateBruteForceSolution()
    {
        double distance = bf.getShortestPathCost();
        int[] shortestPath = bf.getShortestPath();

        System.out.println("Looking at every permutation to find the optimal solution");
        System.out.println("The best permutation is: " );
        for (int j : shortestPath) System.out.print(j + " ");

        System.out.println();
        System.out.println("Optimum Cycle length = " + distance + " miles");
    }

    public String generateKML(){
        StringBuilder kmldoc = new StringBuilder();
        String data = null;
        String[] tokens;

        kmldoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<kml xmlns=\"http://earth.google.com/kml/2.2\">\n" +
                "<Document>\n" +
                "<name>Pittsburgh TSP</name><description>TSP on Crime</description><Style id=\"style6\">\n" +
                "<LineStyle>\n" +
                "<color>73FF0000</color>\n" +
                "<width>5</width>\n" +
                "</LineStyle>");
        kmldoc.append("</Style>\n" +
                "<Style id=\"style5\">\n" +
                "<LineStyle>\n" +
                "<color>507800F0</color>\n" +
                "<width>5</width>\n" +
                "</LineStyle>\n" +
                "</Style>\n" +
                "<Placemark>\n" +
                "<name>TSP Path</name>\n" +
                "<description>TSP Path</description>\n" +
                "<styleUrl>#style6</styleUrl>\n" +
                "<LineString>\n" +
                "<tessellate>1</tessellate>\n" +
                "<coordinates>\n");

        for (int nodeNumber : preOrderWalk) {
            for (GraphNode vertex : vertices) {
                if (vertex.getNodeNumber() == nodeNumber) {
                    data = vertex.getData();
                    tokens = data.split(",");
                    kmldoc.append(tokens[8]).append(",").append(tokens[7]);
                    kmldoc.append(",").append(0.000000).append("\n");
                }
            }
        }

        kmldoc.append("</coordinates>\n" +
                "</LineString>\n" +
                "</Placemark>\n" +
                "<Placemark>\n" +
                "<name>Optimal Path</name>\n" +
                "<description>Optimal Path</description>\n" +
                "<styleUrl>#style5</styleUrl>\n" +
                "<LineString>\n" +
                "<tessellate>1</tessellate>\n" +
                "<coordinates>\n");
        for (int nodeNumber : bf.getShortestPath()) {
            for (GraphNode vertex : vertices) {
                if (vertex.getNodeNumber() == nodeNumber) {
                    data = vertex.getData();
                    tokens = data.split(",");
                    kmldoc.append(tokens[8]).append(",").append(tokens[7]);
                    kmldoc.append(",").append(0.000000).append("\n");
                }
            }
        }

        kmldoc.append("</coordinates>\n" +
                "</LineString>\n" +
                "</Placemark>\n" +
                "</Document>\n" +
                "</kml>");

        return kmldoc.toString();
    }

    public void createKMLfile() throws IOException {
        File myObj = new File("PGHCrimes.kml");
        FileWriter myWriter = new FileWriter(myObj);
        String kml = generateKML();
        myWriter.write(kml);
        myWriter.close();
    }

    public static void main(String[] args) throws IOException {
        while (true) {
            Scanner sc = new Scanner(System.in);
            String start_date, end_date;

            System.out.println("Enter start date");
            start_date = sc.nextLine();
            System.out.println("Enter end date");
            end_date = sc.nextLine();

            new TravelingSalesPerson("CrimeLatLonXY1990.csv", start_date, end_date);

            System.out.println();
            System.out.println("Do you want to continue (y/n)?");
            if(sc.nextLine().equalsIgnoreCase("n")){
                break;
            }
        }
    }
}
