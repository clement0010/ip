package duke;

import duke.command.Command;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.TextUi;

public class Duke {
    public static final String DEFAULT_FILE_NAME = "tasks.txt";

    private Storage storage;
    private TaskList tasks;

    public Duke(String fileName) {
        try {
            storage = new Storage(fileName);
            tasks = new TaskList(storage.loadAllTasks());
        } catch (DukeException e) {
            tasks = new TaskList();
        }
    }

    public Duke() {
        this(DEFAULT_FILE_NAME);
    }

    protected String getResponse(String fullCommand) {
        try {
            Command c = Parser.parse(fullCommand);
            return c.execute(tasks, storage);
        } catch (DukeException e) {
            return e.getMessage();
        }
    }
}
