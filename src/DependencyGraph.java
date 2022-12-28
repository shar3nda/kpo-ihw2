import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DependencyGraph {
    private Map<Node, List<Node>> adjacencyList;
    private List<Node> compilationOrder;
    private String rootDirectory = "";

    private void build() {
        adjacencyList = new LinkedHashMap<>();
        DirectoryWalker.walk(new File(rootDirectory), file -> addEdges(Parser.parseFile(file, rootDirectory)));
        compilationOrder = getCompilationOrder();
    }

    public void concatenate(String _rootDirectory) {
        rootDirectory = _rootDirectory;
        build();
        System.out.println("Concatenated files:");
        for (Node filename : compilationOrder) {
            if (filename.type != Node.nodeType.NULL) {
                try {
                    System.out.printf("%s%n", new String(Files.readAllBytes(Paths.get(String.valueOf(filename)))));
                    System.out.println("<------------------------------------>");
                } catch (IOException e) {
                    System.out.printf("I/O error: can't read file %s%n", filename);
                }
            }
        }
    }

    private void addEdges(Map<Node, Node> edges) {
        for (Map.Entry<Node, Node> edge : edges.entrySet()) {
            Node begin = edge.getKey(), end = edge.getValue();
            if (adjacencyList.containsKey(begin)) {
                adjacencyList.get(begin).add(end);
            } else {
                adjacencyList.put(begin, new ArrayList<>(List.of(end)));
            }
        }
    }

    private List<Node> getCompilationOrder() {
        List<Node> topoSortResult = new ArrayList<>();
        Map<Node, Boolean> visited = new HashMap<>();
        for (Node key : adjacencyList.keySet()) {
            visited.put(key, false);
            for (Node value : adjacencyList.get(key)) {
                visited.put(value, false);
            }
        }
        Stack<Node> entryStack = new Stack<>();
        for (Node filename : adjacencyList.keySet()) {
            if (!visited.get(filename)) {
                dfs(filename, visited, entryStack);
            }
        }
        Map<Node, Integer> sortPositions = new HashMap<>();
        int i = 0;
        while (!entryStack.isEmpty()) {
            Node topElem = entryStack.peek();
                sortPositions.put(topElem, i++);
                topoSortResult.add(topElem);
            entryStack.pop();
        }

        for (Node parentNode : adjacencyList.keySet()) {
            for (Node childNode : adjacencyList.get(parentNode)) {
                if (sortPositions.get(parentNode) > sortPositions.get(childNode) || Objects.equals(parentNode, childNode)) {
                    System.out.println("Cycle detected!");
                    return new ArrayList<>();
                }
            }
        }
        return topoSortResult;
    }

    private void dfs(Node node, Map<Node, Boolean> vis, Stack<Node> st) {
        vis.put(node, true);
        List<Node> childNodes = adjacencyList.get(node);
        if (childNodes != null) {
            for (Node childNode : childNodes) {
                if (!vis.get(childNode)) {
                    dfs(childNode, vis, st);
                }
            }
        }

        st.push(node);
    }
}
