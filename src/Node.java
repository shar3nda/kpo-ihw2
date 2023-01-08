import java.util.Objects;


/**
 * Класс вершины, который содержит тип (обчная и фиктивная родительская) и путь к файлу для этой вершины.
 * Конструктор без параметров создает фиктивную вершину, с параметром - вершину от данного файла.
 */
public class Node {
    private final String name;
    public nodeType type;

    Node() {
        this.type = nodeType.NULL;
        name = null;
    }

    Node(String file) {
        this.type = nodeType.FILE;
        name = file;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return type == node.type && Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }

    /**
     * Тип вершины (NULL - родительская вершина для всех ребер графа, FILE - обычная)
     */
    enum nodeType {
        NULL,
        FILE
    }
}
