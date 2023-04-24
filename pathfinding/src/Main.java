import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        long startTime = System.nanoTime();// Code block to measure runtime
        // Loop each input file
        for (String inputFile : args) {
            System.out.println("Processing " + inputFile + "...");
            int[][] weights = readWeightsFromFile(inputFile);
            int shortestPath = dijkstra(weights);
            System.out.println("Shortest path weight: " + shortestPath);
        }
        long endTime = System.nanoTime();
        double duration =((endTime - startTime) / 1000000000.0);  // Calculate duration in nanoseconds and convert to seconds
        System.out.println("Runtime: " + duration + " Seconds" );
        System.out.println();
    }
    public static int[][] readWeightsFromFile(String inputFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            //Scanner scanner = new Scanner(new File(inputFile));  Legacy
            ArrayList<String[]> lines = new ArrayList<>();
            String line;
            int n = 0;
            int m = 0;
            // ‼️ legacy code
            //while (scanner.hasNextLine()) {
            //String[] line = scanner.nextLine().split(" ");
            //lines.add(line);
            //if (line.length > m) {
            //    m = line.length;
            //}
            //scanner.close();
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(" ");
                lines.add(splitLine);
                n++;
                if (splitLine.length > m) {
                    m = splitLine.length;
                }
            }

            reader.close();
            int[][] weights = new int[n][m];
            for (int i = 0; i < n; i++) {
                //String[] line = lines.get(i);
                String[] splitLine = lines.get(i);
                for (int j = 0; j < splitLine.length; j++) {
                    weights[i][j] = Integer.parseInt(splitLine[j]);
                }
            }
            return weights;

            // Handle error section
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + inputFile);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: Unable to read file: " + inputFile);
            System.exit(1);
        }
        return null;
    }

    public static int dijkstra(int[][] weights) {
        int n = weights.length;
        int m = weights[0].length;
        int[][] dist = new int[n][m];
        boolean[][] visited = new boolean[n][m];

        // add PriorityQueue to improve performance to handling with large matrices
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> dist[a[0]][a[1]] - dist[b[0]][b[1]]);

        try {
            // 1. Initialize distances to infinity and visited to false
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    dist[i][j] = Integer.MAX_VALUE;  // initial distance to inf
                    visited[i][j] = false;
                }
            }


            dist[0][0] = weights[0][0]; // start from top left so the distance from the source should be itself
            pq.offer(new int[]{0, 0}); // add new int array to PQ with current distance vertex from current vertex index

            // Find shortestPath
            while (!pq.isEmpty()) {
                int[] minVertex = pq.poll(); //  the cell with the smallest distance is removed from the front of the queue
                int minI = minVertex[0]; // store index of small vertex that pop out
                int minJ = minVertex[1]; // store index of small vertex that pop out

                //System.out.println("minI: " + minI);
                //System.out.println("minJ: " + minJ);

                visited[minI][minJ] = true; // mark that vertex has been visited

                // rule of traversal   up, down, left, right
                int[] di = {-1, 1, 0, 0};
                int[] dj = {0, 0, -1, 1};

                //System.out.println("=======");
                for (int k = 0; k < 4; k++) { // check ao=round them
                    //System.out.println("K = " + k);
                    int ni = minI + di[k];
                    int nj = minJ + dj[k];

                    //System.out.println("Check around " + " ni: " +ni + " nj: " + nj);

                    if (ni >= 0 && ni < n && nj >= 0 && nj < m && !visited[ni][nj]) { // check it's in bound and not visit yet
                        //System.out.println("Current Dist = " + dist[minI][minJ] + " Next Node weight =  " + weights[ni][nj]);
                        int newDist = dist[minI][minJ] + weights[ni][nj]; // define the newDist prevDis + weight of current vertex
                        if (newDist < dist[ni][nj]) {
                           // System.out.println("I did compare " + newDist  + " with " + dist[ni][nj] + " i choose " + newDist + " and offer " + ni + " / " + nj);// if newDist is less than prev distance to reach that vertex then update the distance
                            dist[ni][nj] = newDist;
                            pq.offer(new int[]{ni, nj}); // add neighboring to priority Queue [ni nj] = value
                        }
                    }
                }
               // System.out.println("=======");
                // System.out.println();
            }

            // Return shortest path
            return dist[n - 1][m - 1];
        } catch (Exception e) {
            System.out.println("Error with" + e.getMessage());
        }
        return -1;
    }
}
