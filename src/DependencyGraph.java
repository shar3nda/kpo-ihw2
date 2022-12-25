import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DependencyGraph {
    private Map<String, List<String>> adjacencyList;
    private List<String> compilationOrder;
    private String rootDirectory = "";

    DependencyGraph() {}

    private void build() {
        adjacencyList = new LinkedHashMap<>();
        DirectoryWalker.walk(new File(rootDirectory), file -> addEdges(Parser.parseFile(file, rootDirectory)));
        compilationOrder = getCompilationOrder();
    }

    private void printDependencies() {
        System.out.println("Dependencies:");
        for (Map.Entry<String, List<String>> edge : adjacencyList.entrySet()) {
            System.out.printf("'%s' -> {", edge.getKey());
            List<String> childNodes = edge.getValue();
            for (int i = 0; i < childNodes.size(); ++i) {
                System.out.printf("'%s'%s", childNodes.get(i), i == childNodes.size() - 1 ? "}\n" : ", ");
            }
        }
    }

    public void concatenate(String _rootDirectory) {
        rootDirectory = _rootDirectory;
        build();
        printDependencies();
        System.out.println();
        for (String filename : compilationOrder) {
            try {
                System.out.printf("%s%n%n", new String(Files.readAllBytes(Paths.get(filename))));
            } catch (IOException e) {
                System.out.printf("I/O error: can't read file %s%n", filename);
            }
        }
    }

    private void addEdges(Map<String, String> edges) {
        for (Map.Entry<String, String> edge : edges.entrySet()) {
            String begin = edge.getKey(), end = edge.getValue();
            if (adjacencyList.containsKey(begin)) {
                adjacencyList.get(begin).add(end);
            } else {
                adjacencyList.put(begin, new ArrayList<>(List.of(end)));
            }
        }
    }


    private List<String> getCompilationOrder() {
        List<String> topoSortResult = new ArrayList<>();
        Map<String, Boolean> visited = new HashMap<>();
        for (String key : adjacencyList.keySet()) {
            visited.put(key, false);
            for (String value : adjacencyList.get(key)) {
                visited.put(value, false);
            }
        }
        Stack<String> entryStack = new Stack<>();
        for (String filename : adjacencyList.keySet()) {
            if (!visited.get(filename)) {
                dfs(filename, visited, entryStack);
            }
        }
        Map<String, Integer> sortPositions = new HashMap<>();
        int i = 0;
        while (!entryStack.isEmpty()) {
            sortPositions.put(entryStack.peek(), i);
            ++i;
            topoSortResult.add(entryStack.peek());
            entryStack.pop();
        }

        for (String parentNode : adjacencyList.keySet()) {
            for (String childNode : adjacencyList.get(parentNode)) {
                if (sortPositions.get(parentNode) > sortPositions.get(childNode) || Objects.equals(parentNode, childNode)) {
                    System.out.println("Cycle detected!");
                    return new ArrayList<>();
                }
            }
        }
        return topoSortResult;
    }

    private void dfs(String node, Map<String, Boolean> vis, Stack<String> st) {
        vis.put(node, true);
        if (adjacencyList.get(node) != null) {
            for (String childNode : adjacencyList.get(node)) {
                if (!vis.get(childNode)) {
                    dfs(childNode, vis, st);
                }
            }
        }

        st.push(node);
    }
}
