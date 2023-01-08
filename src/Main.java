import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DependencyGraph dependencyGraph = new DependencyGraph();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter directory path: ");
        String dir = sc.nextLine();
        dependencyGraph.concatenate(dir);
    }
}
