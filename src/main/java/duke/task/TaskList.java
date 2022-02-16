package duke.task;

import java.util.ArrayList;
import java.util.stream.Collectors;

import duke.DukeException;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void markTask(int taskId, boolean isDone) throws DukeException {
        assert taskId > 0 : "Task id should be greater than 0";
        try {
            if (isDone) {
                tasks.get(taskId).markAsDone();
            } else {
                tasks.get(taskId).markAsNotDone();
            }
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException(e.getMessage());
        }
    }

    public void addTask(Task task) {
        assert task != null : "Cannot add empty task to task list";
        tasks.add(task);
    }

    public TaskList filterTasks(String keywords) {
        return new TaskList(this.tasks.stream().filter(task -> task.hasKeywords(keywords))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    public void deleteTask(int taskId) throws DukeException {
        try {
            assert taskId > 0 : "Task id should be greater than 0";
            tasks.remove(taskId);
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException(e.getMessage());
        }
    }

    public Task getTask(int taskId) throws DukeException {
        try {
            assert taskId > 0 : "Task id should be greater than 0";
            return this.tasks.get(taskId);
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException(e.getMessage());
        }
    }

    public int getSize() {
        return tasks.size();
    }
}
