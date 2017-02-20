package xyz.xyz0z0.things.data.source;

import android.support.annotation.NonNull;

import xyz.xyz0z0.things.data.Task;
import xyz.xyz0z0.things.data.source.local.TasksLocalDataSource;
import xyz.xyz0z0.things.util.Utils;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private final TasksLocalDataSource mTasksLocalDataSource;

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
    public void getTasks(@NonNull LoadTasksCallback callback) {
        Utils.checkNotNull(callback);
        mTasksLocalDataSource.getTasks(callback);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        Utils.checkNotNull(taskId);
        Utils.checkNotNull(callback);
        mTasksLocalDataSource.getTask(taskId,callback);
    }

    @Override
    public void saveTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull Task task) {

    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {

    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {

    }

    @Override
    public void deleteTask(@NonNull String taskId) {

    }
}
