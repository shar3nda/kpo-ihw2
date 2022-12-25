import java.io.File;
import java.util.function.Consumer;

public final class DirectoryWalker {

    /**
     * Функция проходится по всем файлам в текущей папке и во всех вложенных в нее, выполняя операцию {@code fileConsumer} над каждым файлом.
     *
     * @param fileObject   Путь к папке или файлу
     * @param fileConsumer Метод, производящий нужное действие над файлом
     */
    public static void walk(File fileObject, Consumer<File> fileConsumer) {
        if (!fileObject.isDirectory()) {
            fileConsumer.accept(fileObject);
            return;
        }
        File[] fileArray = fileObject.listFiles();
        if (fileArray == null) {
            System.out.printf("I/O error occured in directory %s%n", fileObject.getName());
            return;
        }
        for (File file : fileArray) {
            walk(file, fileConsumer);
        }
    }
}
