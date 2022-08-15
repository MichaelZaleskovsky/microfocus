package com.microfocus.implementation;

import com.microfocus.interfaces.Result;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class ResultImpl implements Result {

    boolean success;
    boolean done;
    String value;
    UUID taskId;
    LocalDateTime startTime;
    LocalDateTime finishTime;

    public ResultImpl() {
        this.done = false;
    }

    public ResultImpl(UUID taskId) {
        this.success = true;
        this.done = true;
        this.value = "The result is " + UUID.randomUUID();
        this.taskId = taskId;
        this.startTime = LocalDateTime.now();
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public UUID getTaskId() {
        return taskId;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    @Override
    public void setFinishTime(LocalDateTime timestamp) {
        this.finishTime = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultImpl result = (ResultImpl) o;
        return success == result.success
                && done == result.done
                && Objects.equals(value, result.value)
                && Objects.equals(taskId, result.taskId)
                && Objects.equals(startTime, result.startTime)
                && Objects.equals(finishTime, result.finishTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, done, value, taskId, startTime, finishTime);
    }
}
