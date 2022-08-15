package com.microfocus.implementation;

import com.microfocus.MicroFocusApplication;
import com.microfocus.interfaces.Result;
import com.microfocus.interfaces.Task;
import com.microfocus.interfaces.TaskScheduler;

import java.util.UUID;

public class TaskShedulerImpl implements TaskScheduler {
    @Override
    public void submitTask(Task task) {
        if (
                task == null
                || task.getTaskType() == null
                || task.getId() == null
                || task.getGid() == null
        ) {
            throw new RuntimeException("Impossible to submit Task. Data not completed");
        }
        if (MicroFocusApplication.resultMap == null) {
            throw new RuntimeException("The main application is not initialized");
        }
        if (MicroFocusApplication.resultMap.get(task.getId()) != null) {
            throw new RuntimeException(String.format("Task with ID %s already executed", task.getId()));
        }

        try {
            MicroFocusApplication.taskQueue.put(task);
            MicroFocusApplication.resultMap.put(task.getId(), new ResultImpl());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Result getResult(UUID uuid) {
        if (MicroFocusApplication.resultMap == null) {
            throw new RuntimeException("The main application is not initialized");
        }
        return MicroFocusApplication.resultMap.get(uuid);
    }
}
