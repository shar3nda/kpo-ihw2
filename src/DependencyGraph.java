import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DependencyGraph {
    private Map<Node, List<Node>> adjacencyList;
    private String rootDirectory = "";

    /**
     * Функция конкатенирует файлы в заданной директории в требуемом порядке и выводит содержимое итогового файла в консоль.
     *
     * @param directory директория для конкатенации
     */
    public void concatenate(String directory) {
        rootDirectory = directory;
        adjacencyList = new LinkedHashMap<>();
        DirectoryWalker.walk(new File(rootDirectory), file -> addEdges(Parser.parseFile(file, rootDirectory)));

        Optional<List<Node>> compilationOrder = getCompilationOrder();
        if (compilationOrder.isEmpty()) {
            System.out.println("Cycle detected! No files were concatenated.");
            return;
        }

        System.out.println("Concatenated files:");
        for (Node filename : compilationOrder.get()) {
            if (filename.type != Node.nodeType.NULL) {
                try {
                    System.out.printf("%s%n%n%n", new String(Files.readAllBytes(Paths.get(String.valueOf(filename)))));
                } catch (IOException e) {
                    System.out.printf("I/O error: can't read file %s%n", filename);
                }
            }
        }
    }


    /**
     * Функция добавляет все ребра из <code>edges</code> в список смежности графа <code>adjacencyList</code>.
     *
     * @param edges ребра для добавления
     */
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


    /**
     * Функция вычисляет порядок сборки в виде списка вершин.
     * Реализована топологическая сортировка графа через DFS с учетом циклов.
     *
     * @return <code>Optional.empty</code>, если найден цикл, иначе <code>Optional.of(порядок_сортировки)</code>.
     */
    private Optional<List<Node>> getCompilationOrder() {
        List<Node> topoSortResult = new ArrayList<>();
        Map<Node, Boolean> visited = new HashMap<>();
        for (Node key : adjacencyList.keySet()) {
            visited.put(key, false);
            for (Node value : adjacencyList.get(key)) {
                visited.put(value, false);
            }
        }
        Stack<Node> entryStack = new Stack<>();
        for (Node node : adjacencyList.keySet()) {
            if (!visited.get(node)) {
                dfs(node, visited, entryStack);
            }
        }
        Map<Node, Integer> sortPositions = new HashMap<>();
        int i = 0;
        while (!entryStack.isEmpty()) {
            sortPositions.put(entryStack.peek(), i++);
            topoSortResult.add(entryStack.peek());
            entryStack.pop();
        }
        if (hasCycles(sortPositions)) {
            return Optional.empty();
        }
        return Optional.of(topoSortResult);
    }


    /**
     * Вспомогательная функция для поиска циклов в графе.
     *
     * @param sortPositions <code>Map</code> вершин и их индексов в отсортированном массиве.
     * @return <code>true</code>, если цикл найден, иначе <code>false</code>.
     */
    private boolean hasCycles(Map<Node, Integer> sortPositions) {
        for (Node parentNode : adjacencyList.keySet()) {
            for (Node childNode : adjacencyList.get(parentNode)) {
                if (sortPositions.get(parentNode) > sortPositions.get(childNode) || Objects.equals(parentNode, childNode)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Вспомогательная функция для глубинного обхода графа.
     *
     * @param node       текущая вершина
     * @param visited    <code>Map</code> уже посещенных вершин
     * @param entryStack <code>Stack</code> вершин в порядке топологической сортировки
     */
    private void dfs(Node node, Map<Node, Boolean> visited, Stack<Node> entryStack) {
        visited.put(node, true);
        List<Node> childNodes = adjacencyList.get(node);
        if (childNodes != null) {
            for (Node childNode : childNodes) {
                if (!visited.get(childNode)) {
                    dfs(childNode, visited, entryStack);
                }
            }
        }

        entryStack.push(node);
    }
}
