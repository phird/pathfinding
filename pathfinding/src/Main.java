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
            System.out.println();
        }
        long endTime = System.nanoTime();
        double duration =((endTime - startTime) / 1000000000.0);  // Calculate duration in nanoseconds and convert to second
        System.out.println("Runtime: " + duration + "seconds " );
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
        System.out.println("Matrix ");
        System.out.println(weights.length + " x " + weights[0].length);
        int n = weights.length;  // rows
        int m = weights[0].length; // col
        int[][] dist = new int[n][m];
        boolean[][] visited = new boolean[n][m];

        try {
            // 1. Initialize distances to infinity and visited to false
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    dist[i][j] = Integer.MAX_VALUE;
                    visited[i][j] = false;
                }
            }

            // Distance to source is 0 -> make dist = input
            dist[0][0] = weights[0][0];

            // Find shortestPath
            for (int i = 0; i < n * m - 1; i++) {
                // Find vertex that have minimum distance
                int minDist = Integer.MAX_VALUE;
                int minI = -1;
                int minJ = -1;
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < m ; k++) {
                        if (!visited[j][k] && dist[j][k] < minDist) {  // check is it less than minDist and still not visit yeะ
                            minDist = dist[j][k];
                            minI = j;
                            minJ = k;
                        }
                    }
                }

                // Mark vertex is visited
                visited[minI][minJ] = true;

                // Update distances of adjacent vertices --> From the problem there must be "up down left right"
                int[] di = {1, 0, -1, 0};  //  4 dir that can travel
                int[] dj = {0, -1, 0, 1};  //
                for (int k = 0; k < 4; k++) { // check around them
                    int ni = minI + di[k];
                    int nj = minJ + dj[k];
                    if (ni >= 0 && ni < n && nj >= 0 && nj < m && !visited[ni][nj]) { // is it in bound ?  and not visit yet
                        int newDist = dist[minI][minJ] + weights[ni][nj];
                        if (newDist < dist[ni][nj]) {
                            dist[ni][nj] = newDist;
                        }
                    }
                }
            }

            // Return shortest path
            return dist[n - 1][m - 1];
        }catch (Exception e){
            System.out.println("Error with" + e.getMessage());
        }
    return -1;
    }
}
