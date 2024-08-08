import managers.Managers;
import managers.task.TaskManager;
import org.junit.jupiter.api.AfterEach;
import tasks.status.Status;
import tasks.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InMemoryTaskManagerTest {

    Managers managers = new Managers();
    String filePath = "tasks.txt";
    TaskManager taskManager = managers.getFileBackedTaskManager(filePath);
    String name;
    String description;
    int idTask;
    int idEpic;
    int idSubTask;
    Status status;

    @AfterEach
    public void deleteFileAfterEach() {
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void createUpdatePrintDeleteTask() {
        Task task = new Task("Task 1", "Task 1");
        name = "Task 1";
        description = "Task 1";
        idTask = taskManager.creatingTask(taskManager.getListOfTasks(), name, description);
        Assertions.assertEquals(String.valueOf(task), taskManager.printTask(taskManager.getListOfTasks(), idTask),
                "Ошибка, обычные Задачи не совпадают.");
        Assertions.assertEquals(task.hashCode(), idTask, "Ошибка, id обычных Задач не совпадает.");
        name = "Task 2";
        description = "Task 2";
        status = Status.DONE;
        taskManager.updateTask(taskManager.getListOfTasks(), idTask, name, description, status);
        Assertions.assertNotEquals(String.valueOf(task), taskManager.printTask(taskManager.getListOfTasks(), idTask),
                "Ошибка, обычные Задачи совпадают.");
        Assertions.assertEquals(task.hashCode(), idTask, "Ошибка, id обычных Задач не совпадает.");
        taskManager.deleteTask(taskManager.getListOfTasks(), idTask);
        Assertions.assertNull(taskManager.printTask(taskManager.getListOfTasks(), idTask),
                "Ошибка, обычная Задача не удалена.");
    }

    @Test
    public void createUpdatePrintDeleteEpic() {
        Epic epic = new Epic("Epic 1", "Epic 1");
        name = "Epic 1";
        description = "Epic 1";
        idEpic = taskManager.createEpic(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(),
                name, description);
        Assertions.assertEquals(String.valueOf(epic), taskManager.printEpic(taskManager.getListOfEpics(), idEpic),
                "Ошибка, задачи типа Эпик не совпадают.");
        Assertions.assertEquals(epic.hashCode(), idEpic, "Ошибка, id задач типа Эпик не совпадает.");
        name = "Epic 2";
        description = "Epic 2";
        taskManager.updateEpic(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(), idEpic,
                name, description);
        Assertions.assertNotEquals(String.valueOf(epic), taskManager.printEpic(taskManager.getListOfEpics(), idEpic),
                "Ошибка, задачи типа Эпик совпадают.");
        Assertions.assertEquals(epic.hashCode(), idEpic, "Ошибка, id задач типа Эпик не совпадает.");
        taskManager.deleteEpic(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(), idEpic);
        Assertions.assertNull(taskManager.printEpic(taskManager.getListOfEpics(), idEpic),
                "Ошибка, задача типа Эпик не удалена.");
    }

    @Test
    public void createUpdatePrintDeleteSubTask() {
        SubTask subTask = new SubTask("Subtask 1", "Subtask 1");
        name = "Epic 1";
        description = "Epic 1";
        idEpic = taskManager.createEpic(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(),
                name, description);
        name = "SubTask 1";
        description = "SubTask 1";
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(), idEpic,
                name, description);
        Assertions.assertEquals(String.valueOf(subTask), taskManager.printSubTask(taskManager.getSubTaskListOfEpic(),
                idEpic, idSubTask), "Ошибка, Подзадачи не совпадают.");
        Assertions.assertEquals(subTask.hashCode(), idSubTask, "Ошибка, id Подзадач не совпадает.");
        name = "SubTask 2";
        description = "SubTask 2";
        status = Status.DONE;
        taskManager.updateSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(), idEpic,
                idSubTask, name, description, status);
        Assertions.assertNotEquals(String.valueOf(subTask), taskManager.printSubTask(taskManager.getSubTaskListOfEpic(),
                idEpic, idSubTask), "Ошибка, Подзадачи совпадают.");
        Assertions.assertEquals(subTask.hashCode(), idSubTask, "Ошибка, id Подзадач не совпадает.");
        Assertions.assertEquals(Status.DONE, taskManager.getListOfEpics().get(idEpic).getTaskStatus(),
                "Ошибка, статус задачи Эпика отличается от ожидаемого");
        name = "SubTask 3";
        description = "SubTask 3";
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(), idEpic,
                name, description);
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getListOfEpics().get(idEpic).getTaskStatus(),
                "Ошибка, статус задачи Эпика отличается от ожидаемого");
        taskManager.deleteSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(), idEpic, idSubTask);
        Assertions.assertNull(taskManager.printSubTask(taskManager.getSubTaskListOfEpic(),
                idEpic, idSubTask), "Ошибка, Подзадача не удалена.");
    }

    @Test
    public void idTasksNotEquals() {
        name = "Task 1";
        description = "Task 1";
        idTask = taskManager.creatingTask(taskManager.getListOfTasks(), name, description);
        name = "Epic 1";
        description = "Epic 1";
        idEpic = taskManager.createEpic(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(),
                name, description);
        name = "SubTask 1";
        description = "SubTask 1";
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(), idEpic,
                name, description);
        Assertions.assertNotEquals(idTask, idEpic, "Ошибка, id Задачи и Эпиков совпадает.");
        Assertions.assertNotEquals(idEpic, idSubTask, "Ошибка, id Задачи и Эпиков совпадает.");
    }

    @Test
    public void printDeleteAllTasks() {
        String infoAllTasks;
        String infoAllEpicSubTasks;
        Task task = new Task("Task 1", "Task 1");
        Epic epic = new Epic("Epic 1", "Epic 1");
        SubTask subTaskOne = new SubTask("SubTask 1", "SubTask 1");
        infoAllTasks = task + "\n" + epic + "\n" + subTaskOne + "\n";
        infoAllEpicSubTasks = subTaskOne + "\n";
        name = "Task 1";
        description = "Task 1";
        idTask = taskManager.creatingTask(taskManager.getListOfTasks(), name, description);
        name = "Epic 1";
        description = "Epic 1";
        idEpic = taskManager.createEpic(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(),
                name, description);
        name = "SubTask 1";
        description = "SubTask 1";
        idSubTask = taskManager.creatingSubTask(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(), idEpic,
                name, description);
        Assertions.assertEquals(infoAllTasks, taskManager.printAllTasks(taskManager.getListOfTasks(),
                        taskManager.getListOfEpics(), taskManager.getSubTaskListOfEpic()),
                "Ошибка, задачи не совпадают с ожидаемыми.");
        Assertions.assertEquals(infoAllEpicSubTasks,
                taskManager.printAllEpicSubTasks(taskManager.getSubTaskListOfEpic(), idEpic),
                "Ошибка, Подзадачи не совпадают с ожидаемыми.");
        taskManager.deleteAllTasks(taskManager.getListOfTasks(), taskManager.getListOfEpics(),
                taskManager.getSubTaskListOfEpic());
        Assertions.assertEquals("", taskManager.printAllTasks(taskManager.getListOfTasks(),
                taskManager.getListOfEpics(), taskManager.getSubTaskListOfEpic()), "Ошибка, задачи не удалены.");
    }

    @Test
    public void sizeHistorySavingVersion() {
        String infoHistory;
        Task taskOne = new Task("Task 1", "Task 1");
        Task taskTwo = new Task("Task 2", "Task 2");
        Epic epic = new Epic("Epic 1", "Epic 1");
        infoHistory = taskOne + "\n" + epic + "\n" + taskTwo + "\n";
        name = "Task 1";
        description = "Task 1";
        idTask = taskManager.creatingTask(taskManager.getListOfTasks(), name, description);
        taskManager.printTask(taskManager.getListOfTasks(), idTask);
        taskManager.printTask(taskManager.getListOfTasks(), idTask);
        name = "Task 2";
        description = "Task 2";
        idTask = taskManager.creatingTask(taskManager.getListOfTasks(), name, description);
        taskManager.printTask(taskManager.getListOfTasks(), idTask);
        taskManager.printTask(taskManager.getListOfTasks(), idTask);
        name = "Epic 1";
        description = "Epic 1";
        idEpic = taskManager.createEpic(taskManager.getSubTaskListOfEpic(), taskManager.getListOfEpics(),
                name, description);
        taskManager.printEpic(taskManager.getListOfEpics(), idEpic);
        taskManager.printEpic(taskManager.getListOfEpics(), idEpic);
        taskManager.printTask(taskManager.getListOfTasks(), idTask);
        Assertions.assertEquals(infoHistory, taskManager.printHistory(),
                "Ошибка, выводимая история задач не совпадает с ожидаемым результатом.");
    }

}