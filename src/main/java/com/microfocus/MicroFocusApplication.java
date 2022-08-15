package com.microfocus;

import com.microfocus.enums.TaskType;
import com.microfocus.interfaces.Result;
import com.microfocus.interfaces.Task;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class MicroFocusApplication {
    public static LinkedBlockingQueue<Task> taskQueue;
    public static ConcurrentHashMap<UUID, Result> resultMap;
    public static ExecutorService threadPoolExecutor;
    public static final int DELAY = 500;
    public static volatile boolean NEXT_TASK_AVAILABLE;

    public static void main(String[] args) {
        taskQueue = new LinkedBlockingQueue<>();
        resultMap = new ConcurrentHashMap<>();
        threadPoolExecutor = Executors.newCachedThreadPool();
        NEXT_TASK_AVAILABLE = true;

        threadPoolExecutor.execute(MicroFocusApplication::start);
    }

    private static void start() {

        Executor executor = new ExecutorImpl();

        List<UUID> gidList = new CopyOnWriteArrayList<>();
        List<TaskType> typeList = new CopyOnWriteArrayList<>();
        Task task = null;
        boolean skip = false;

        while (true) {
            try {
                if (!skip) {
                    task = taskQueue.take();
                }
                if (gidList.contains(task.getGid()) || typeList.contains(task.getTaskType())) {
                    skip = true;
                } else {
                    UUID taskId = task.getId();
                    UUID gid = task.getGid();
                    TaskType type = typeInvert(task.getTaskType());

                    gidList.add(gid);
                    typeList.add(type);

                    while (!NEXT_TASK_AVAILABLE) {}

                    NEXT_TASK_AVAILABLE = false;
                    executor.execute(task,
                            (t, r) -> {
                                resultMap.put(taskId, r);
                                gidList.remove(gid);
                                typeList.remove(type);
                            });
                    skip = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static TaskType typeInvert(TaskType taskType) {
        return taskType.equals(TaskType.READ) ? TaskType.WRITE : TaskType.READ;
    }
}
