package duke.ui;

import duke.DukeException;
import duke.common.Messages;
import duke.parser.Parser;
import duke.task.TaskList;

public class TextUi {
    private static final String LOGO = " ____        _        \n"
            + "|  _ \\ _   _| | _____ \n"
            + "| | | | | | | |/ / _ \\\n"
            + "| |_| | |_| |   <  __/\n"
            + "|____/ \\__,_|_|\\_\\___|\n";


    public static String showWelcome() {
        return showToUser(
                Messages.MESSAGE_WELCOME,
                Messages.MESSAGE_PROMPT);
    }

    public static String showExecutionMessage(String message) {
        return showToUser(message);
    }

    public static String showExecutionMessage(String message, String taskString, int size) {
        String displaySize = String.format("Now you have %s task%s in the list.", size, Parser.parseTaskSize(size));
        return showToUser(message, taskString, displaySize);
    }

    public static String showLoadingError(String message) {
        return showToUser("Error loading duke.Duke: " + message);
    }

    public static String showError(String message) {
        return showToUser(message);
    }

    public static String showTasks(TaskList tasks) throws DukeException {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < tasks.getSize(); i++) {
            str.append(i + 1).append(". ").append(tasks.getTask(i)).append("\n");
        }
        return str.toString();
    }

    public static String showToUser(String... message) {
        StringBuilder str = new StringBuilder();
        for (String m : message) {
            str.append(m);
        }
        return str.toString();
    }
}
