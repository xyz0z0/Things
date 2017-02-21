package xyz.xyz0z0.things.tasks;

import java.util.List;

import xyz.xyz0z0.things.BasePresenter;
import xyz.xyz0z0.things.BaseView;
import xyz.xyz0z0.things.data.Task;

/**
 * Created by Gang on 2017/2/20.
 */

public interface TasksContract {

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openTaskDetail(Task requestedTask);

        void deleteTask(Task deleteTask);

        void completeTask(Task completeTask);

        void activateTask(Task activateTask);

        void clearCompletedTask();

        TaskFilterType getFiltering();

        void setFiltering(TaskFilterType requestType);
    }

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showAddTask();

        void showTaskDetailsUi(String taskId);

        void showTaskToDelete(Task task);

        void showTaskMarkedCompleted();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTasksError();

        void showNoTasks();

        void showNoCompletedTasks();

        void showNoActiveTasks();

        void showFilteringPopupMenu();

        void showAllFilterLabel();

        void showCompletedFilterLabel();

        void showActiveFilterLabel();

        void showSuccessfullySavedMessage();

        boolean isActive();
    }
}
