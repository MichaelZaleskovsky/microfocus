package com.microfocus;

import com.microfocus.enums.TaskType;
import com.microfocus.implementation.TaskImpl;
import com.microfocus.implementation.TaskShedulerImpl;
import com.microfocus.interfaces.Result;
import com.microfocus.interfaces.Task;
import com.microfocus.interfaces.TaskScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SubmitAndGetResultTestCase {

    TaskScheduler taskScheduler;

    @BeforeEach
    void setUp() {
        MicroFocusApplication.main(new String[]{""});

        taskScheduler = new TaskShedulerImpl();
    }

    @Test
    void getResultOfTask() {

        UUID id = UUID.randomUUID();
        Task task = new TaskImpl(id, UUID.randomUUID(), TaskType.READ);
        taskScheduler.submitTask(task);

        while (!taskScheduler.getResult(id).isDone()) {}
        Result result1 =  taskScheduler.getResult(id);
        assertEquals(id, result1.getTaskId());

    }

    @Test
    void runSameGidNotConcurrently() {

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID gid = UUID.randomUUID();
        Task task1 = new TaskImpl(id1, gid, TaskType.WRITE);
        Task task2 = new TaskImpl(id2, gid, TaskType.WRITE);
        taskScheduler.submitTask(task1);
        taskScheduler.submitTask(task2);

        while (!taskScheduler.getResult(id1).isDone()) {}
        Result result1 =  taskScheduler.getResult(id1);

        while (!taskScheduler.getResult(id2).isDone()) {}
        Result result2 =  taskScheduler.getResult(id2);

        assertTrue(result2.getStartTime().isAfter(result1.getFinishTime()));
    }

    @Test
    void runReadWriteNotConcurrently() {

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID gid1 = UUID.randomUUID();
        UUID gid2 = UUID.randomUUID();
        Task task1 = new TaskImpl(id1, gid1, TaskType.READ);
        Task task2 = new TaskImpl(id2, gid2, TaskType.WRITE);
        taskScheduler.submitTask(task1);
        taskScheduler.submitTask(task2);

        while (!taskScheduler.getResult(id1).isDone()) {
        }
        Result result1 = taskScheduler.getResult(id1);

        while (!taskScheduler.getResult(id2).isDone()) {
        }
        Result result2 = taskScheduler.getResult(id2);

        assertTrue(result2.getStartTime().isAfter(result1.getFinishTime())
                || result2.getStartTime().equals(result1.getFinishTime()));

    }

    @Test
    void saveOrderOfTask() {

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        UUID id4 = UUID.randomUUID();
        UUID gid1 = UUID.randomUUID();
        UUID gid2 = UUID.randomUUID();
        UUID gid3 = UUID.randomUUID();
        Task task1 = new TaskImpl(id1, gid1, TaskType.READ);
        Task task2 = new TaskImpl(id2, gid2, TaskType.WRITE);
        Task task3 = new TaskImpl(id3, gid3, TaskType.WRITE);
        Task task4 = new TaskImpl(id4, gid3, TaskType.WRITE);
        taskScheduler.submitTask(task1);
        taskScheduler.submitTask(task2);
        taskScheduler.submitTask(task3);
        taskScheduler.submitTask(task4);

        while (!taskScheduler.getResult(id1).isDone()) {}
        Result result1 =  taskScheduler.getResult(id1);

        while (!taskScheduler.getResult(id2).isDone()) {}
        Result result2 =  taskScheduler.getResult(id2);

        while (!taskScheduler.getResult(id3).isDone()) {}
        Result result3 =  taskScheduler.getResult(id3);

        while (!taskScheduler.getResult(id4).isDone()) {}
        Result result4 =  taskScheduler.getResult(id4);

        assertTrue(
                result4.getStartTime().isAfter(result3.getStartTime())
                && result3.getStartTime().isAfter(result2.getStartTime())
                && result2.getStartTime().isAfter(result1.getStartTime())
        );
    }

    @Test
    void submitRepeatTask() {
        UUID id1 = UUID.randomUUID();
        UUID gid1 = UUID.randomUUID();
        Task task1 = new TaskImpl(id1, gid1, TaskType.READ);
        taskScheduler.submitTask(task1);

        try {
            taskScheduler.submitTask(task1);
        } catch (Exception e) {
            assertEquals(String.format("Task with ID %s already executed", task1.getId()), e.getMessage());
        }
    }

    @Test
    void submitWrongTask() {
        UUID id1 = UUID.randomUUID();
        UUID gid1 = UUID.randomUUID();
        Task task1 = new TaskImpl(id1, gid1, null);

        try {
            taskScheduler.submitTask(task1);
        } catch (Exception e) {
            assertEquals("Impossible to submit Task. Data not completed", e.getMessage());
        }
    }
}
