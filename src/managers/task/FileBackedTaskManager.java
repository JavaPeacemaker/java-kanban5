package managers.task;

import excep.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.status.Status;
import tasks.type.Type;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private String filePath;

    public FileBackedTaskManager(String filePath) throws NullPointerException {
        if (filePath != null) {
            this.filePath = filePath;
        }
    }

    public void loadFromFile(String filePath) {
        Path pathTasksFile = Paths.get(filePath);
        HashMap<Integer, Integer> idEpicOldNew = new HashMap<>();
        try {
            if (!Files.exists(pathTasksFile)) {
                Files.createFile(pathTasksFile);
            }
        } catch (IOException exception) {
            System.out.println("Ошибка при создании файла для хранения задач.");
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String safeString = "";
            while ((safeString = bufferedReader.readLine()) != null) {
                String[] split = safeString.split(", ");
                if (split[1].equals(Type.TASK.toString())) {
                    int idTask = super.creatingTask(getListOfTasks(), split[2], split[4]);
                }
                if (split[1].equals(Type.EPIC.toString())) {
                    int idEpic = super.createEpic(getSubTaskListOfEpic(), getListOfEpics(), split[2], split[4]);
                    idEpicOldNew.put(Integer.parseInt(split[0]), idEpic);
                }
                if (split[1].equals(Type.SUBTASK.toString())) {
                    int idSubTask = super.creatingSubTask(getSubTaskListOfEpic(), getListOfEpics(),
                            idEpicOldNew.get(Integer.parseInt(split[5])), split[2], split[4]);
                }
            }
        } catch (NoSuchFileException exception) {
            System.out.println("\\033[31;4;4m" + "Ошибка, файл не найден." + "!\033[0m");
        } catch (IOException exception) {
            System.out.println("\\033[31;4;4m" + "Ошибка при чтении файла." + "!\033[0m");
        }
    }

    public void save() {
        try (BufferedWriter bufferedWriter
                     = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id, type, name, staus, description, epic");
            for (Task task : getListOfTasks().values()) {
                bufferedWriter.newLine();
                bufferedWriter.write(String.format("%d, %s, %s, %s, %s", task.hashCode(), task.getType(),
                        task.getName(), task.getTaskStatus(), task.getDescription()));
            }
            for (Epic epic : getListOfEpics().values()) {
                bufferedWriter.newLine();
                bufferedWriter.write(String.format("%d, %s, %s, %s, %s", epic.hashCode(), epic.getType(),
                        epic.getName(), epic.getTaskStatus(), epic.getDescription()));
            }
            for (Integer idEpic : getSubTaskListOfEpic().keySet()) {
                for (SubTask subTask : getSubTaskListOfEpic().get(idEpic).values()) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(String.format("%d, %s, %s, %s, %s, %d", subTask.hashCode(), subTask.getType(),
                            subTask.getName(), subTask.getTaskStatus(), subTask.getDescription(), idEpic));
                }
            }
        } catch (NoSuchFileException exception) {
            System.out.println("Ошибка, файл не найден.");
        } catch (IOException exception) {
            System.out.println("\\033[31;4;4m" + "Ошибка при записи задач в файл." + "!\033[0m");
            throw new ManagerSaveException(exception);
        }
    }

    @Override
    public int creatingTask(HashMap<Integer, Task> taskList, String name, String description) {
        int idTask = super.creatingTask(taskList, name, description);
        save();
        return idTask;
    }

    @Override
    public int createEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                          HashMap<Integer, Epic> epicList, String name, String description) {
        int idEpic = super.createEpic(subTaskListOfEpic, epicList, name, description);
        save();
        return idEpic;
    }

    @Override
    public int creatingSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                               HashMap<Integer, Epic> epicList, int idEpic, String name, String description) {
        int idSubTask = super.creatingSubTask(subTaskListOfEpic, epicList, idEpic, name, description);
        save();
        return idSubTask;
    }

    @Override
    public void updateTask(HashMap<Integer, Task> taskList, int idTask, String name, String description,
                           Status status) {
        super.updateTask(taskList, idTask, name, description, status);
        save();
    }

    @Override
    public void updateEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                           HashMap<Integer, Epic> epicList, int idEpic, String name, String description) {
        super.updateEpic(subTaskListOfEpic, epicList, idEpic, name, description);
        save();
    }

    @Override
    public void updateSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                              HashMap<Integer, Epic> epicList, int idEpic, int idSubTask, String name,
                              String description, Status status) {
        super.updateSubTask(subTaskListOfEpic, epicList, idEpic, idSubTask, name, description, status);
        save();
    }

    @Override
    public void deleteAllTasks(HashMap<Integer, Task> taskList, HashMap<Integer, Epic> epicList,
                               HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic) {
        super.deleteAllTasks(taskList, epicList, subTaskListOfEpic);
        save();
    }

    @Override
    public void deleteTask(HashMap<Integer, Task> taskList, int idTask) {
        super.deleteTask(taskList, idTask);
        save();
    }

    @Override
    public void deleteEpic(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                           HashMap<Integer, Epic> epicList, int idEpic) {
        super.deleteEpic(subTaskListOfEpic,epicList, idEpic);
        save();
    }

    @Override
    public void deleteSubTask(HashMap<Integer, HashMap<Integer, SubTask>> subTaskListOfEpic,
                              HashMap<Integer, Epic> epicList, int idEpic, int idSubTask) {
        super.deleteSubTask(subTaskListOfEpic,epicList, idEpic, idSubTask);
        save();
    }

}