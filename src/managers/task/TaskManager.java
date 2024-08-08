package managers.task;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.status.Status;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    HashMap<Integer, Task> getListOfTasks();

    HashMap<Integer, Epic> getListOfEpics();

    HashMap<Integer, HashMap<Integer, SubTask>> getSubTaskListOfEpic();

    int creatingTask(HashMap<Integer, Task> taskList, String name, String description);


    // List<Task> getTasks();

    int creatingSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                        HashMap<Integer, Epic> epicList, int idEpic, String name, String description);

    void updateTask(HashMap<Integer, Task> taskList, int idTask, String name, String description,
                    Status status);

    void updateEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                    HashMap<Integer, Epic> epicList, int idEpic, String name, String description);

    void updateSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                       HashMap<Integer, Epic> epicList, int idEpic, int idSubTask, String name,
                       String description, Status status);

    String printAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                         HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic);

    String printAllEpicSubTasks(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic, int idEpic);

    String printTask(HashMap<Integer, Task> taskList, int idTask);
    int createEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                   HashMap<Integer, Epic> epicList, String name, String description);

    String printEpic(HashMap<Integer, Epic> epicList, int idEpic);

    String printSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                        int idEpic, int idSubTask);

    String printHistory();

    void deleteAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                        HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic);

    void deleteTask(HashMap<Integer, Task> taskList, int idTask);

    void deleteEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                    HashMap<Integer, Epic> epicList, int idEpic);

    void deleteSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                       HashMap<Integer, Epic> epicList, int idEpic, int idSubTask);

    void updateEpicStatus(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                          HashMap<Integer, Epic> epicList, int idEpic);
}