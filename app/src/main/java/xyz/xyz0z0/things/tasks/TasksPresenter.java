package xyz.xyz0z0.things.tasks;

import java.util.ArrayList;
import java.util.List;

import xyz.xyz0z0.things.data.Task;
import xyz.xyz0z0.things.data.source.TasksDataSource;
import xyz.xyz0z0.things.data.source.TasksRepository;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Gang on 2017/2/20.
 */

public class TasksPresenter implements TasksContract.Presenter {

    private static final String TAG = "TasksPresenter";

    private final TasksRepository mTasksRepository;

    private final TasksContract.View mTasksView;

    private TaskFilterType mCurrentFiltering = TaskFilterType.ALL_TASK;

    private boolean mFirstLoad = true;

    public TasksPresenter(TasksRepository tasksRepository, TasksContract.View tasksView) {
        mTasksRepository = tasksRepository;
        mTasksView = tasksView;
        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (RESULT_OK == resultCode) {
            mTasksView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    public void loadTasks(boolean forceUpdate, final boolean showLoadingUi) {
        if (showLoadingUi) {
            mTasksView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }
        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTaskLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<Task>();
                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASK:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }
                if (!mTasksView.isActive()) {
                    return;
                }
                if (showLoadingUi) {
                    mTasksView.setLoadingIndicator(false);
                }
                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (!mTasksView.isActive()) {
                    return;
                }
                switch (mCurrentFiltering) {
                    case ACTIVE_TASKS:
                        mTasksView.showNoActiveTasks();
                        break;
                    case COMPLETED_TASKS:
                        mTasksView.showNoCompletedTasks();
                        break;
                    default:
                        mTasksView.showNoTasks();
                        break;
                }
            }
        });
    }

    private void processTasks(List<Task> tasksToShow) {
        if (tasksToShow.isEmpty()) {
            processEmptyTasks();
        } else {
            mTasksView.showTasks(tasksToShow);
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ALL_TASK:
                mTasksView.showAllFilterLabel();
                break;
            case ACTIVE_TASKS:
                mTasksView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mTasksView.showCompletedFilterLabel();
                break;
        }
    }

    private void processEmptyTasks() {
        switch (mCurrentFiltering) {
            case ALL_TASK:
                mTasksView.showNoTasks();
                break;
            case ACTIVE_TASKS:
                mTasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTasksView.showNoCompletedTasks();
                break;
        }
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    @Override
    public void openTaskDetail(Task requestedTask) {
        mTasksView.showTaskDetailsUi(requestedTask.getId());
    }

    @Override
    public void deleteTask(Task deleteTask) {
        mTasksRepository.deleteTask(deleteTask.getId());
        loadTasks(false, false);
    }

    @Override
    public void completeTask(Task completeTask) {
        mTasksRepository.completeTask(completeTask.getId());
        mTasksView.showTaskMarkedCompleted();
        loadTasks(false, false);
    }

    @Override
    public void activateTask(Task activateTask) {
        mTasksRepository.activateTask(activateTask.getId());
        mTasksView.showTaskMarkedActive();
        loadTasks(false, false);
    }

    @Override
    public void clearCompletedTask() {
        mTasksRepository.clearCompletedTasks();
        mTasksView.showCompletedTasksCleared();
        loadTasks(false, false);
    }

    @Override
    public TaskFilterType getFiltering() {
        return mCurrentFiltering;
    }

    @Override
    public void setFiltering(TaskFilterType requestType) {
        mCurrentFiltering = requestType;
    }


}
