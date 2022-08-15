package com.microfocus;

import com.microfocus.interfaces.Result;
import com.microfocus.interfaces.Task;

import java.util.function.BiConsumer;

public class ExecutorImpl implements Executor {
    @Override
    public void execute(Task task, BiConsumer<Task, Result> completionCallback) {
        MicroFocusApplication.threadPoolExecutor.execute(
                () -> {completionCallback.accept(task, task.execute());}
        );
    }
}
