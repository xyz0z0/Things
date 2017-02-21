package xyz.xyz0z0.things.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import xyz.xyz0z0.things.data.Task;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public interface TasksDataSource {

    void getTasks(@NonNull LoadTasksCallback callback);

    void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback);

    void saveTask(@NonNull Task task);

    void completeTask(@NonNull Task task);

    void completeTask(@NonNull String taskId);

    void activateTask(@NonNull Task task);

    void activateTask(@NonNull String taskId);

    void clearCompletedTasks();

    void refreshTasks();

    void deleteAllTasks();

    void deleteTask(@NonNull String taskId);

    void updateTask(@NonNull Task task);

    interface LoadTasksCallback {

        void onTaskLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }
}
