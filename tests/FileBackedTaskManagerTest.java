import managers.Managers;
import managers.task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class FileBackedTaskManagerTest {

    Managers managers = new Managers();
    String path = "tasks.txt";
    TaskManager taskManager = managers.getFileBackedTaskManager(path);
    int idTask;
    int idEpic;
    int idSubTask;

    @Test
    public void createEmptyFileTasks() {
        int quantityString = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            while (bufferedReader.readLine() != null) {
                quantityString++;
            }
        } catch (NoSuchFileException exception) {
            System.out.println("Ошибка, файл не найден." );
        } catch (IOException exception) {
            System.out.println("Ошибка при чтении файла.");
        }
        Assertions.assertEquals(0, quantityString, "Ошибка, файл не пустой.");
    }

    @Test
    public void createTasksSaveFile() {
        int quantityString = 0;
        idEpic = taskManager.createEpic(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(),
                "Epic 1", "Epic 1");
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(),
                idEpic, "SubTask 1", "SubTask 1");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            while (bufferedReader.readLine() != null) {
                quantityString++;
            }
        } catch (NoSuchFileException exception) {
            System.out.println("Ошибка, файл не найден.");
        } catch (IOException exception) {
            System.out.println("Ошибка при чтении файла.");
        }
        Assertions.assertEquals(3, quantityString, "Ошибка, количество строк в файле не соответствует "
                + "ожидаемому значению.");

        try {
            Files.delete(Paths.get(path));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void createTasksFromFile() {
        int quantityTasks = 0;
        idTask = taskManager.creatingTask(taskManager.getListOfTasks(), "Task 1", "Task 1");
        idTask = taskManager.creatingTask(taskManager.getListOfTasks(), "Task 2", "Task 2");
        TaskManager taskManagerOne = managers.getFileBackedTaskManager(path);
        for (Task task : taskManagerOne.getListOfTasks().values()) {
            quantityTasks++;
        }
        Assertions.assertEquals(2, quantityTasks, "Ошибка, количество восстановленных из файла "
                + "задач не соответствует ожидаемому значению.");

        try {
            Files.delete(Paths.get(path));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}