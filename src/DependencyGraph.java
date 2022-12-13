import java.io.*;
import java.util.HashSet;

public class DependencyGraph {
    private final String rootDir;
    private final HashSet<Edge> adjList;

    DependencyGraph(String parentDirectory) {
        rootDir = parentDirectory;
        adjList = new HashSet<>();
    }

    public void buildDepGraph() {
        WalkDirectory.walk(new File(rootDir), this::parseFile);
        for (Edge e : adjList) {
            System.out.printf("'%s' -> '%s'%n", e.begin(), e.end());
        }
    }

    private void addEdge(String begin, String end) {
        adjList.add(new Edge(begin, end));
    }

    private void parseFile(File file) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return;
        }
        while (true) {
            String line;
            try {
                line = br.readLine();
            } catch (IOException e) {
                System.out.println("I/O error occurred!");
                return;
            }
            if (line == null) {
                break;
            }
            if (!line.startsWith("require")) {
                continue;
            }
            String depName = line.replaceFirst("require ", "").replaceAll("[\"'“”‘’«»]", "");
            addEdge((new File(depName)).getAbsolutePath(), file.getAbsolutePath());
        }
    }
}
