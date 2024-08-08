package tasks;

import tasks.status.Status;
import tasks.type.Type;

import java.util.ArrayList;
import java.util.List;

import static jdk.internal.org.jline.utils.Status.getStatus;

public class Epic extends Task {
    // private final List<Integer> subTasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

   /*
   public List<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    */
    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        String result = "Tasks.Epic{" +
                "name='" + getName() + '\'';
        if (getDescription() != null) {
            result += ", description.lenght='" + getDescription().length() + '\'';
        } else {
            result += ", description.lenght='null" + '\'';
        }
        return result += ", status='" + getTaskStatus() + '\'' +
                '}';
    }
}