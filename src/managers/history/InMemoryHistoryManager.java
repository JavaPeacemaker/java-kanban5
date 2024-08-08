package managers.history;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> last;
    private HashMap<Integer, Node<Task>> historyTasksList = new HashMap<>();
    @Override
    public void addHistory(Task task) {
        if (!historyTasksList.containsKey(task.hashCode())) {
            historyTasksList.put(task.hashCode(), linkLast(task));
        } else {
            remove(task.hashCode());
            historyTasksList.put(task.hashCode(), linkLast(task));
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node<Task> oldFirst = null;
        if (!historyTasksList.isEmpty()) { // Если история задач не пустая
            while (history.size() != historyTasksList.size()) { // Переносим историю задач в ArrayList
                for (Node<Task> node : historyTasksList.values()) {
                    if (node.prev == oldFirst) { // Расставляем в правильном порядке задачи с помощью ссылок узла Node
                        history.add(node.element);
                        oldFirst = node;
                    }
                }
            }
        }
        return history;
    }

    @Override
    public void remove() {
        if (!historyTasksList.isEmpty()) {
            historyTasksList.clear();
            last = null;
        }
    }

    @Override
    public void remove(int idTask) {
        if (historyTasksList.containsKey(idTask)) {
            if (historyTasksList.get(idTask).prev != null) { // Если есть ссылка на предыдущий узел Node
                historyTasksList.get(idTask).prev.next = historyTasksList.get(idTask).next;
            }
            if (historyTasksList.get(idTask).next != null) { // Если есть ссылка на седующий узел Node
                historyTasksList.get(idTask).next.prev = historyTasksList.get(idTask).prev;
            } else { // Для корректной работы ссылки узла Node при добавлении в историю одинаковых задач
                last = historyTasksList.get(idTask).prev;
            }
            if (historyTasksList.get(idTask).next == null && historyTasksList.get(idTask).prev == null) {
                last = null; // Для корректной работы ссылки узла Node при добавлении в историю первой задачи
            }
            historyTasksList.remove(idTask);
        }
    }

    public Node<Task> linkLast(Task task) {
        final Node<Task> oldTail = last;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        last = newNode;
        if (oldTail != null) {
            oldTail.next = newNode;
        }
        return newNode;
    }

}