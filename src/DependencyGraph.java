import java.io.File;
import java.util.*;

public class DependencyGraph {
    private String rootDir = "";
    private final Map<String, List<String>> adjList;

    DependencyGraph() {
        adjList = new LinkedHashMap<>();
    }

    public void build(String dir) {
        rootDir = dir;
        DirectoryWalker.walk(new File(rootDir), file -> addEdges(Parser.parseFile(file, rootDir)));
        System.out.println("Dependencies:");
        for (Map.Entry<String, List<String>> e : adjList.entrySet()) {
            System.out.printf("'%s' -> {", e.getKey());
            List<String> childNodes = e.getValue();
            for (int i = 0; i < childNodes.size(); ++i) {
                System.out.printf("'%s'%s", childNodes.get(i), i == childNodes.size() - 1 ? "}\n" : ", ");
            }
        }
        System.out.println();
        System.out.println("Compilation order:");
        for (String filename : getCompilationOrder()) {
            System.out.println(filename);
        }
    }

    private void addEdges(Map<String, String> edges) {
        for (Map.Entry<String, String> e : edges.entrySet()) {
            String begin = e.getKey(), end = e.getValue();
            if (adjList.containsKey(begin)) {
                adjList.get(begin).add(end);
            } else {
                adjList.put(begin, new ArrayList<>(List.of(end)));
            }
        }
    }


    private List<String> getCompilationOrder() {
        List<String> topoSortResult = new ArrayList<>();
        Map<String, Boolean> vis = new HashMap<>();
        for (String key : adjList.keySet()) {
            vis.put(key, false);
            for (String value : adjList.get(key)) {
                vis.put(value, false);
            }
        }
        Stack<String> st = new Stack<>();
        for (String filename : adjList.keySet()) {
            if (!vis.get(filename)) {
                dfs(filename, vis, st);
            }
        }
        Map<String, Integer> sortPositions = new HashMap<>();
        int i = 0;
        while (!st.isEmpty()) {
            sortPositions.put(st.peek(), i);
            ++i;
            topoSortResult.add(st.peek());
            st.pop();
        }

        for (String parentNode : adjList.keySet()) {
            for (String childNode : adjList.get(parentNode)) {
                if (sortPositions.get(parentNode) > sortPositions.get(childNode) || Objects.equals(parentNode, childNode)) {
                    System.out.println("Cycle exists!");
                    return new ArrayList<>();
                }
            }
        }
        return topoSortResult;
    }

    private void dfs(String node, Map<String, Boolean> vis, Stack<String> st) {
        vis.put(node, true);
        if (adjList.get(node) != null) {
            for (String childNode : adjList.get(node)) {
                if (!vis.get(childNode)) {
                    dfs(childNode, vis, st);
                }
            }
        }

        st.push(node);
    }
}
