import java.io.File;

public class Main {
    public static void main(String[] args) {
        WalkDirectory.walk(new File("files"), f -> System.out.println(f.getAbsolutePath()));
    }
}
