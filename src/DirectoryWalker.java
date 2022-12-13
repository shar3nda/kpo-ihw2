import java.io.File;
import java.util.function.Consumer;

public final class DirectoryWalker {

    /**
     * Функция проходится по всем файлам в текущей папке и во всех вложенных в нее, выполняя операцию {@code fileConsumer} над каждым файлом.
     *
     * @param dir          Путь к корневой папке
     * @param fileConsumer Метод, производящий нужное действие над файлом
     */
    public static void walk(File dir, Consumer<File> fileConsumer) {
        if (!dir.isDirectory()) {
            fileConsumer.accept(dir);
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            System.out.println("I/O error occured");
            return;
        }
        for (File file1 : files) {
            walk(file1, fileConsumer);
        }
    }
}
