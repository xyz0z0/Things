package xyz.xyz0z0.things.addedittask;

import xyz.xyz0z0.things.BasePresenter;
import xyz.xyz0z0.things.BaseView;
import xyz.xyz0z0.things.data.Task;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public interface AddEditTaskContract {

    interface Presenter extends BasePresenter {

        void createTask(String title, String description);

        void updateTask(Task task);

        void populateTask();
    }

    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTaskTitle(String title);

        void setTaskDescription(String description);

        boolean isActive();
    }
}
