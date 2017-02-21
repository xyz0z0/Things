package xyz.xyz0z0.things.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import xyz.xyz0z0.things.data.Task;
import xyz.xyz0z0.things.data.source.local.TasksLocalDataSource;
import xyz.xyz0z0.things.util.Utils;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class TasksRepository implements TasksDataSource {
    private static TasksRepository INSTANCE = null;
    private final TasksLocalDataSource mTasksLocalDataSource;
    Map<String, Task> mCachedTasks;
    boolean mCacheIsDirty = false;

    public TasksRepository(@NonNull TasksLocalDataSource tasksLocalDataSource) {
        mTasksLocalDataSource = Utils.checkNotNull(tasksLocalDataSource);
    }

    public static TasksRepository getInstance(TasksLocalDataSource tasksLocalDataSouce) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksLocalDataSouce);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void updateTask(@NonNull Task task) {
        Utils.checkNotNull(task);
        mTasksLocalDataSource.updateTask(task);
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        Utils.checkNotNull(callback);
        mTasksLocalDataSource.getTasks(callback);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        Utils.checkNotNull(taskId);
        Utils.checkNotNull(callback);

        Task cachedTask = getTaskWithId(taskId);
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }
        mTasksLocalDataSource.getTask(taskId, callback);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        Utils.checkNotNull(task);
        mTasksLocalDataSource.saveTask(task);

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        Utils.checkNotNull(task);
        mTasksLocalDataSource.completeTask(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        Utils.checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        Utils.checkNotNull(task);
        mTasksLocalDataSource.activateTask(task);

        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        Utils.checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        mTasksLocalDataSource.clearCompletedTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTasksLocalDataSource.deleteTask(taskId);

        mCachedTasks.remove(taskId);
    }

    @Nullable
    private Task getTaskWithId(@NonNull String id) {
        Utils.checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }
}
