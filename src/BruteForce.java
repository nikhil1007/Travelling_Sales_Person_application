/*
Name: Nikhil Kashyap (nikhilka)
 */

/**
 * We determine every possible route from node 0 to traverse the graph and track the lease cost path while doing it.
 */
public class BruteForce {
    /**
     * num_of_nodes - Number of nodes in the graph
     * graph - the distance matrix
     * visitedVertices - keeps track of the vertices visited in the given time
     * minDistance - track minimum distance determined to traverse the graph at a given time
     * lastVertex - track the last vertex of the path
     */
    private final int num_of_nodes;
    private final double[][] graph;
    private final boolean[] visitedVertices;
    private Double minDistance;
    private HeapNode lastVertex;

    //parameterized constructor
    public BruteForce(double[][] graph){
        num_of_nodes = graph.length;
        this.graph = graph;
        visitedVertices = new boolean[num_of_nodes];
        minDistance = Double.MAX_VALUE;
        lastVertex = null;
    }

    /**
     * We determine the shortest path required by travsering all possible path from node 0 using a modified DFS algorithm
     * @precondition
     *  graph is constructed and initialized
     * @return
     *  The minimum cost required to visit all nodes in the graph
     */
    public double getShortestPathCost() {
        HeapNode node = new HeapNode(0,0,-1);
        node.setParentNode(null);
        visitedVertices[0] = true;
        backTracking(node,  1);
        return minDistance;
    }

    /**
     * A modified DFS algorithm where we find the optimum path to traverse the graph. Steps:
     *  a) If the node is not visited then visit and go to its univisted neighbor
     *  b) If you encounter last vertex then find the best way to return:
     *      i) either come back the same way you reached
     *      ii) Find if there is a path to start directly
     * @param currentVertex
     *  The vertex we are inspecting at a given time in recursion
     * @param verticesVisited
     *  The number of vertices visited already
     */

    private void backTracking(HeapNode currentVertex, int verticesVisited)
    {
        if (verticesVisited == num_of_nodes && graph[currentVertex.getNodeNumber()][0] > 0)
        {
            if (minDistance <= currentVertex.getDistance() + graph[currentVertex.getNodeNumber()][0])
                return;
            else
            {
                minDistance = currentVertex.getDistance() + graph[currentVertex.getNodeNumber()][0];
                lastVertex = currentVertex;
                return;
            }
        }

        for (int i = 0; i < num_of_nodes; i++)
        {
            if (!visitedVertices[i] && graph[currentVertex.getNodeNumber()][i] > 0)
            {
                // Mark as visited
                visitedVertices[i] = true;

                if (currentVertex.getDistance() + graph[currentVertex.getNodeNumber()][i] < minDistance) // DFS manner branch and bound
                {
                    HeapNode nextVertex =  new HeapNode( currentVertex.getDistance() + graph[currentVertex.getNodeNumber()][i],i,currentVertex.getNodeNumber());
                    nextVertex.setParentNode(currentVertex);
                    backTracking(nextVertex, verticesVisited + 1);
                }

                // Mark ith node as unvisited after the recursion return
                visitedVertices[i] = false;
            }
        }
    }

    /**
     * Traverses from Last vertex to start by moving to its parent node.
     * @return
     *  AN integer array representing the shortest path to traverse the graph.
     */
    int[] getShortestPath() {
        int[] shortestPath = new int[num_of_nodes+1];
        int local_index = 0;

        shortestPath[local_index++] = 0;

        HeapNode currentVertex = lastVertex;

        while (currentVertex != null){
            shortestPath[local_index++] = currentVertex.getNodeNumber();
            currentVertex = currentVertex.getParentNode();
        }

        return shortestPath;
    }
}
