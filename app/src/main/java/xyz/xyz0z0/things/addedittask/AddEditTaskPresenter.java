package xyz.xyz0z0.things.addedittask;

import xyz.xyz0z0.things.data.Task;
import xyz.xyz0z0.things.data.source.TasksDataSource;
import xyz.xyz0z0.things.data.source.TasksRepository;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter {

    private TasksRepository mTasksRepository;

    private AddEditTaskContract.View mAddTaskView;

    private String mTaskId;

    public AddEditTaskPresenter(TasksRepository tasksRepository,
                                AddEditTaskContract.View addTaskView, String taskId) {
        mTasksRepository = tasksRepository;
        mAddTaskView = addTaskView;
        mTaskId = taskId;

        mAddTaskView.setPresenter(this);
    }


    @Override
    public void start() {
        if (mTaskId != null) {
            populateTask();
        }
    }

    @Override
    public void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty()) {
            mAddTaskView.showEmptyTaskError();
        } else {
            mTasksRepository.saveTask(newTask);
            mAddTaskView.showTasksList();
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task.isEmpty()) {
            mAddTaskView.showEmptyTaskError();
        } else {
            mTasksRepository.updateTask(task);
            mAddTaskView.showTasksList();
        }
    }

    @Override
    public void populateTask() {
        mTasksRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (mAddTaskView.isActive()) {
                    mAddTaskView.setTaskTitle(task.getTitle());
                    mAddTaskView.setTaskDescription(task.getDescription());
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mAddTaskView.isActive()) {
                    mAddTaskView.showEmptyTaskError();
                }
            }
        });
    }

}
