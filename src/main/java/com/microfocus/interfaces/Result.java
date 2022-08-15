package com.microfocus.interfaces;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Result {

    boolean isSuccess();

    boolean isDone();

    Object getValue();

    UUID getTaskId();

    LocalDateTime getStartTime();

    LocalDateTime getFinishTime();

    void setFinishTime(LocalDateTime timestamp);

}


