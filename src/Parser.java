import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static Map<Node, Node> parseFile(File file, String fileDirectory) {
        Map<Node, Node> dependencies = new HashMap<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found!%n", file.getName());
            return dependencies;
        }

        dependencies.put(new Node(), new Node(file.getPath()));
        while (true) {
            String line;
            try {
                line = br.readLine();
            } catch (IOException e) {
                System.out.printf("I/O error occurred in file %s%n", file.getName());
                return dependencies;
            }
            if (line == null) {
                break;
            }
            if (!line.startsWith("require")) {
                continue;
            }
            String depName = "%s%s%s".formatted(fileDirectory, File.separator, line.replaceFirst("require ", "").replaceAll("[\"'“”‘’«»]", ""));
            dependencies.put((new Node((new File(depName)).getPath())), new Node(file.getPath()));
        }
        return dependencies;
    }
}
