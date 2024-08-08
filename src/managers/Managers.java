package managers;

import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.task.FileBackedTaskManager;
import managers.task.InMemoryTaskManager;
import managers.task.TaskManager;

public class Managers {

    public TaskManager getFileBackedTaskManager(String filePath) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(filePath);
        fileBackedTaskManager.loadFromFile(filePath);
        return fileBackedTaskManager;
    }
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}