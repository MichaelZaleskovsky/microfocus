package com.microfocus.interfaces;

import com.microfocus.enums.TaskType;

import java.util.UUID;

public interface Task {

    Result execute();

    UUID getId();

    UUID getGid();

    TaskType getTaskType();

    // add any other necessary methods

}


