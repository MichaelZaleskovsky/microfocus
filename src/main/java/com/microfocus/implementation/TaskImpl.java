package com.microfocus.implementation;

import com.microfocus.MicroFocusApplication;
import com.microfocus.enums.TaskType;
import com.microfocus.interfaces.Result;
import com.microfocus.interfaces.Task;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskImpl implements Task {

    UUID id;
    UUID gid;
    TaskType type;

    public TaskImpl(UUID id, UUID gid, TaskType type) {
        this.id = id;
        this.gid = gid;
        this.type = type;
    }

    @Override
    public Result execute() {
        ResultImpl result = new ResultImpl(id);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
/*
        Here is place for methods, which must be invoked before next task start
*/
        MicroFocusApplication.NEXT_TASK_AVAILABLE = true;
        try {
            Thread.sleep(MicroFocusApplication.DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result.setFinishTime(LocalDateTime.now());
        return result;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getGid() {
        return gid;
    }

    @Override
    public TaskType getTaskType() {
        return type;
    }
}
