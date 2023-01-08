import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    /**
     * Функция парсит файл и возвращает <code>HashMap</code> зависимостей в нём.
     *
     * @param parsedFile файл для парсинга
     * @param parentDir  родительская директория файла
     * @return <code>HashMap</code> в виде списка ребёр <code>родитель -> сын</code>
     */
    public static Map<Node, Node> parseFile(File parsedFile, String parentDir) {
        Map<Node, Node> dependencies = new HashMap<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(parsedFile));
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found!%n", parsedFile.getName());
            return dependencies;
        }

        dependencies.put(new Node(), new Node(parsedFile.getPath()));
        while (true) {
            String line;
            try {
                line = br.readLine();
            } catch (IOException e) {
                System.out.printf("I/O error occurred in file %s%n", parsedFile.getName());
                return dependencies;
            }
            if (line == null) {
                break;
            }
            if (!line.startsWith("require")) {
                continue;
            }
            String depName = "%s%s%s".formatted(parentDir, File.separator, line.replaceFirst("require ", "").replaceAll("[\"'“”‘’«»]", ""));
            dependencies.put(new Node(new File(depName).getPath()), new Node(parsedFile.getPath()));
        }
        return dependencies;
    }
}
