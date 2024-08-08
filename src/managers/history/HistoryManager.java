package managers.history;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);
    List<Task> getHistory();
    void remove();
    void remove(int idTask);
}