package com.microfocus;

import com.microfocus.interfaces.Result;
import com.microfocus.interfaces.Task;

import java.util.function.BiConsumer;

interface Executor {

    /**

     * Run the task and invoke completionCallback when task is completed.

     * @param task a task to be executed

     * @param completionCallback a callback called on task completion

     */

    void execute(Task task, BiConsumer<Task, Result> completionCallback);

}


