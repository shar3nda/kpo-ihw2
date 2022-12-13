public class Main {
    public static void main(String[] args) {
        DependencyGraph dg = new DependencyGraph("files");
        dg.buildDepGraph();
    }
}
