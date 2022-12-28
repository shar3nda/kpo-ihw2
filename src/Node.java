import java.util.Objects;

public class Node {
    Node() {
        this.type = nodeType.NULL;
        name = null;
    }
    Node(String s) {
        this.type = nodeType.FILE;
        name = s;
    }
    enum nodeType {
        NULL,
        FILE
    }
    public nodeType type;
    private final String name;

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
}
