package duke.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import duke.DukeException;
import duke.command.AddDeadlineCommand;
import duke.command.AddEventCommand;
import duke.command.AddTodoCommand;
import duke.command.Command;
import duke.command.DeleteCommand;
import duke.command.ExitCommand;
import duke.command.FilterTaskCommand;
import duke.command.FindTaskCommand;
import duke.command.ListCommand;
import duke.command.MarkTaskCommand;
import duke.command.SortTaskCommand;
import duke.common.Messages;
import duke.common.TaskType;

public class Parser {
    private static final String DEFAULT_DATE_FORMAT = "d/MM/yyyy HHmm";

    /**
     * Parses user input for command.
     *
     * @param userInput The user input.
     * @return The command to be executed based on the user input.
     * @throws DukeException If the user input is invalid, it throws
     * a DukeException.
     */
    public static Command parse(String userInput) throws DukeException {
        String[] userInputArr = userInput.split(" ", 2);
        switch (userInputArr[0]) {
        case "mark":
            return new MarkTaskCommand(Integer.parseInt(userInputArr[1]) - 1, true);
        case "unmark":
            return new MarkTaskCommand(Integer.parseInt(userInputArr[1]) - 1, false);
        case "delete":
            return new DeleteCommand(Integer.parseInt(userInputArr[1]) - 1);
        case "find":
            userInput = String.join(" ",
                    Arrays.copyOfRange(userInputArr, 1, userInputArr.length));
            return new FindTaskCommand(userInput);
        case "sort":
            String sortType = userInputArr.length < 2 ? "" : userInputArr[1];
            return prepareSortTasksCommand(sortType);
        case "filter":
            String filterType = userInputArr.length < 2 ? "" : userInputArr[1];
            return prepareFilterTasksCommand(filterType);
        case "list":
            return new ListCommand();
        case "bye":
            return new ExitCommand();
        case "todo":
            return prepareAddTodoCommand(userInputArr[1]);
        case "event":
            userInput = String.join(" ",
                    Arrays.copyOfRange(userInputArr, 1, userInputArr.length));
            return prepareAddEventCommand(userInput);
        case "deadline":
            userInput = String.join(" ",
                    Arrays.copyOfRange(userInputArr, 1, userInputArr.length));
            return prepareAddDeadlineCommand(userInput);
        default:
            throw new DukeException(Messages.MESSAGE_ERROR_INVALID_COMMAND);
        }
    }

    /**
     * Parses the datetime string of an event or deadline based on the
     * default date format.
     *
     * @param dateTime datetime string of the event or deadline.
     * @return LocalDateTime format of the given datetime.
     * @throws DukeException if datetime string is invalid format.
     */
    public static LocalDateTime parseDateTime(String dateTime) throws DukeException {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
            return LocalDateTime.parse(dateTime, format);
        } catch (DateTimeParseException e) {
            throw new DukeException(Messages.MESSAGE_ERROR_INVALID_DATETIME_FORMAT);
        }
    }

    /**
     * Parses the task word as singular or plural noun.
     *
     * @param size the size of the task list.
     * @return task word in singular or plural.
     */
    public static String parseTaskSize(int size) {
        return size > 1 ? "s" : "";
    }

    private static Command prepareAddTodoCommand(String title) throws DukeException {
        if (title.isEmpty()) {
            throw new DukeException(Messages.MESSAGE_ERROR_EMPTY_TITLE);
        }
        return new AddTodoCommand(title);
    }

    private static Command prepareAddEventCommand(String userInput) throws DukeException {
        String[] taskArr = userInput.split(" /at ");
        if (!validateCommandInput(taskArr)) {
            throw new DukeException(Messages.MESSAGE_ERROR_INVALID_COMMAND);
        }
        String title = taskArr[0];
        String eventAt = taskArr[1];

        return new AddEventCommand(title, parseDateTime(eventAt));
    }

    private static Command prepareAddDeadlineCommand(String userInput) throws DukeException {
        String[] taskArr = userInput.split(" /by ");
        if (!validateCommandInput(taskArr)) {
            throw new DukeException(Messages.MESSAGE_ERROR_INVALID_COMMAND);
        }
        String title = taskArr[0];
        String dueBy = taskArr[1];

        return new AddDeadlineCommand(title, parseDateTime(dueBy));
    }

    private static Command prepareSortTasksCommand(String userInput) throws DukeException {
        TaskType taskType = TaskType.parseTaskType(userInput);
        if (taskType == TaskType.TASK || taskType == TaskType.TODO || taskType == TaskType.INVALID) {
            throw new DukeException(Messages.MESSAGE_ERROR_INVALID_SORT_TYPE);
        }
        return new SortTaskCommand(taskType);
    }

    private static Command prepareFilterTasksCommand(String userInput) throws DukeException {
        TaskType taskType = TaskType.parseTaskType(userInput);
        if (taskType == TaskType.TASK || taskType == TaskType.INVALID) {
            throw new DukeException(Messages.MESSAGE_ERROR_INVALID_FILTER_TYPE);
        }
        return new FilterTaskCommand(taskType);
    }

    private static boolean validateCommandInput(String[] inputWithDate) {
        return inputWithDate.length == 2 && !inputWithDate[0].isEmpty() && !inputWithDate[1].isEmpty();
    }
}
