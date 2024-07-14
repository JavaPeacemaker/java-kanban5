package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static InMemoryHistoryManager historyManager;
    private static Task task;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Task", "Description", TaskStatus.NEW);
    }
    @Test
    void shouldAddAnyTasksInHistory() {
        Epic epic = new Epic("Epic", "Description");
        SubTask subTask = new SubTask(epic, "SubTask", "Description", TaskStatus.NEW);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertEquals(history.size(), 3);
    }

}