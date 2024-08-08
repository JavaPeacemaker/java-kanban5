package managers.task;

import managers.Managers;
import managers.history.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.status.Status;

import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    Managers managers = new Managers();
    HistoryManager historyManager = managers.getDefaultHistory();

    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic = new HashMap<>();

    @Override
    public HashMap<Integer, Task> getListOfTasks() {
        return taskList;
    }

    @Override
    public HashMap<Integer, Epic> getListOfEpics() {
        return epicList;
    }

    @Override
    public HashMap<Integer, HashMap<Integer, SubTask>> getSubTaskListOfEpic() {
        return subTaskListOfEpic;
    }

    @Override
    public int creatingTask(HashMap<Integer, Task> taskList, String name, String description) {
        Task task = new Task(name, description);
        taskList.put(task.hashCode(), task);
        return task.hashCode();
    }

    @Override
    public int createEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                          HashMap<Integer, Epic> epicList, String name, String description) {
        Epic epic = new Epic(name, description);
        epicList.put(epic.hashCode(), epic);
        HashMap<Integer, SubTask> subTaskList = new HashMap<>(); // Создаём хеш-таблицу для хранения Подзадач
        subTaskListOfEpic.put(epic.hashCode(), subTaskList); // для конкретной задачи типа Эпик
        return epic.hashCode();
    }

    @Override
    public int creatingSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                               HashMap<Integer, Epic> epicList, int idEpic, String name, String description) {
        if (subTaskListOfEpic.containsKey(idEpic)) { // Если создана задача типа Эпик
            SubTask subTask = new SubTask(name, description);
            subTaskListOfEpic.get(idEpic).put(subTask.hashCode(), subTask);
            updateEpicStatus(subTaskListOfEpic, epicList, idEpic);
            return subTask.hashCode();
        } else {
            return 0;
        }
    }

    @Override
    public void updateTask(HashMap<Integer, Task> taskList, int idTask, String name, String description,
                           Status status) {
        if (taskList.containsKey(idTask)) { // Если создана обычная Задача
            Task task = new Task(name, description);
            task.setTaskStatus(status);
            taskList.put(idTask, task);
        }
    }

    @Override
    public void updateEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                           HashMap<Integer, Epic> epicList, int idEpic, String name, String description) {
        if (epicList.containsKey(idEpic)) { // Если создана задача типа Эпик
            Epic epic = new Epic(name, description);
            epicList.put(idEpic, epic);
            if (!subTaskListOfEpic.get(idEpic).isEmpty()) { // Если Подзадачи есть
                updateEpicStatus(subTaskListOfEpic, epicList, idEpic);
            }
        }
    }

    @Override
    public void updateSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                              HashMap<Integer, Epic> epicList, int idEpic, int idSubTask, String name,
                              String description, Status status) {
        if (subTaskListOfEpic.containsKey(idEpic) && subTaskListOfEpic.get(idEpic).containsKey(idSubTask)) {
            SubTask subTask = new SubTask(name, description); // Если создана задача типа Эпик и Подзадача
            subTask.setTaskStatus(status);
            subTaskListOfEpic.get(idEpic).put(idSubTask, subTask);
            updateEpicStatus(subTaskListOfEpic, epicList, idEpic);
        }
    }

    @Override
    public String printAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                                HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic) {
        String infoAllTasks = "";
        if (!taskList.isEmpty()) { // Если список обычных Задач не пустой
            for (Task task : taskList.values()) {
                infoAllTasks += task + "\n";
                historyManager.addHistory(task);
            }
        }
        if (!epicList.isEmpty()) { // Если список задач типа Эпик не пустой
            for (Epic epic : epicList.values()) {
                infoAllTasks += epic + "\n";
                historyManager.addHistory(epic);
            }
        }
        if (!subTaskListOfEpic.isEmpty()) { // Если список Подзадач не пустой
            for (Integer id : subTaskListOfEpic.keySet()) {
                for (SubTask subTask : subTaskListOfEpic.get(id).values()) {
                    if (subTask != null) {
                        infoAllTasks += subTask + "\n";
                        historyManager.addHistory(subTask);
                    }
                }
            }
        }
        return infoAllTasks;
    }

    @Override
    public String printAllEpicSubTasks(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic, int idEpic) {
        String infoAllSubTaskOfEpic = ""; // Если задача типа Эпик создана и список Подзадач не пустой
        if (subTaskListOfEpic.containsKey(idEpic) && !subTaskListOfEpic.get(idEpic).isEmpty()) {
            for (SubTask subTask : subTaskListOfEpic.get(idEpic).values()) {
                infoAllSubTaskOfEpic += subTask + "\n";
                historyManager.addHistory(subTask);
            }
        }
        return infoAllSubTaskOfEpic;
    }

    @Override
    public String printTask(HashMap<Integer, Task> taskList, int idTask) {
        if (taskList.containsKey(idTask)) { // Если обычная Задача создана
            historyManager.addHistory(taskList.get(idTask));
            return String.valueOf(taskList.get(idTask));
        }
        return null;
    }

    @Override
    public String printEpic(HashMap<Integer, Epic> epicList, int idEpic) {
        if (epicList.containsKey(idEpic)) { // Если задача типа Эпик создана
            historyManager.addHistory(epicList.get(idEpic));
            return String.valueOf(epicList.get(idEpic));
        }
        return null;
    }

    @Override
    public String printSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                               int idEpic, int idSubTask) {
        if (subTaskListOfEpic.get(idEpic).containsKey(idSubTask)) { // Если Подзадача создана
            historyManager.addHistory(subTaskListOfEpic.get(idEpic).get(idSubTask));
            return String.valueOf(subTaskListOfEpic.get(idEpic).get(idSubTask));
        }
        return null;
    }

    @Override
    public String printHistory() {
        String infoHistory = "";
        if (!historyManager.getHistory().isEmpty()) {
            for (Task task : historyManager.getHistory()) {
                infoHistory += task + "\n";
            }
        }
        return infoHistory;
    }

    @Override
    public void deleteAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                            HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic) {
        if (!taskList.isEmpty()) { // Если список обычных Задач не пустой
            taskList.clear();
        }
        if (!epicList.isEmpty()) { // Если список задач типа Эпик не пустой
            epicList.clear();
        }
        if (!subTaskListOfEpic.isEmpty()) { // Если список Подзадач не пустой
            subTaskListOfEpic.clear();
        }
        historyManager.remove(); // Удаляем все задачи из истории
    }

    @Override
    public void deleteTask(HashMap<Integer, Task> taskList, int idTask) {
        if (taskList.containsKey(idTask)) { // Если обычная Задача создана
            taskList.remove(idTask);
        }
        historyManager.remove(idTask); // Удаляем обычную задачу из истории
    }

    @Override
    public void deleteEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                        HashMap<Integer, Epic> epicList, int idEpic) {
        if (epicList.containsKey(idEpic)) { // Если задач типа Эпик создана
            epicList.remove(idEpic);
            for (Integer id : subTaskListOfEpic.get(idEpic).keySet()) {
                historyManager.remove(id); // Удаляем из истории все Подзадачи, которые относятся к задаче типа Эпик
            }
            if (subTaskListOfEpic.containsKey(idEpic)) { // Если список Подзадач конктерной задачи типа Эпик создан
                subTaskListOfEpic.remove(idEpic);
            }
            historyManager.remove(idEpic); // Удаляем задачу типа Эпик из истории
        }
    }

    @Override
    public void deleteSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                           HashMap<Integer, Epic> epicList, int idEpic, int idSubTask) {
        if (subTaskListOfEpic.get(idEpic).containsKey(idSubTask)) { // Если Подзадача создана
            subTaskListOfEpic.get(idEpic).remove(idSubTask);
            updateEpicStatus(subTaskListOfEpic, epicList, idEpic);
        }
        historyManager.remove(idSubTask); // Удаляем Подзадачу из истории
    }

    @Override
    public void updateEpicStatus(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                                 HashMap<Integer, Epic> epicList, int idEpic) {
        int statusNew = 0;
        int statusDone = 0;
        for (SubTask subTask : subTaskListOfEpic.get(idEpic).values()) {
            if (subTask.getTaskStatus() == Status.NEW) {
                statusNew++;
            } else if (subTask.getTaskStatus() == Status.DONE) {
                statusDone++;
            }
        }
        if (statusNew == subTaskListOfEpic.get(idEpic).size()) {
            epicList.get(idEpic).setTaskStatus(Status.NEW);
        } else if (statusDone == subTaskListOfEpic.get(idEpic).size()) {
            epicList.get(idEpic).setTaskStatus(Status.DONE);
        } else {
            epicList.get(idEpic).setTaskStatus(Status.IN_PROGRESS);
        }
    }

}