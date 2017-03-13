package xyz.xyz0z0.things.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xyz.xyz0z0.things.R;
import xyz.xyz0z0.things.addedittask.AddEditTaskActivity;
import xyz.xyz0z0.things.data.Task;

import static xyz.xyz0z0.things.addedittask.AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class TasksFragment extends Fragment implements TasksContract.View {

    private TasksContract.Presenter mPresenter;
    TasksAdapter.TaskItemListener mItemListener = new TasksAdapter.TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            mPresenter.openTaskDetail(clickedTask);
        }

        @Override
        public void onTaskLongClick(Task longClickedTask) {
            showTaskToDelete(longClickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            mPresenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            mPresenter.activateTask(activatedTask);
        }
    };
    private TasksAdapter mTasksAdapter;
    private RecyclerView mRvTasks;
    private View mViewNoTasks;
    private ImageView mIvNoTasks;
    private TextView mTvNoTasks;
    private TextView mTvNoTasksAdd;
    private LinearLayout mLlTasksView;
    private ActionBar mActionBar;

    public TasksFragment() {

    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        mLlTasksView.setVisibility(View.GONE);
        mViewNoTasks.setVisibility(View.VISIBLE);

        mTvNoTasks.setText(mainText);
        mIvNoTasks.setImageResource(iconRes);
        mTvNoTasksAdd.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksAdapter = new TasksAdapter(new ArrayList<Task>(0), mItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_tasks, container, false);

        mRvTasks = (RecyclerView) root.findViewById(R.id.tasks_list);
        mRvTasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvTasks.setHasFixedSize(true);
//        mRvTasks.addItemDecoration();TODO
        mRvTasks.setAdapter(mTasksAdapter);

        mLlTasksView = (LinearLayout) root.findViewById(R.id.tasks);

        mViewNoTasks = root.findViewById(R.id.noTasks);
        mIvNoTasks = (ImageView) root.findViewById(R.id.noTasksIcon);
        mTvNoTasks = (TextView) root.findViewById(R.id.noTasksMain);
        mTvNoTasksAdd = (TextView) root.findViewById(R.id.noTasksAdd);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.tasksact_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTask();
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasks, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mPresenter.clearCompletedTask();
                break;
            case R.id.menu_refresh:
                mPresenter.loadTasks(true);
                break;
            case R.id.menu_filter:
                showFilteringPopupMenu();
                break;
            case R.id.menu_about:
                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    // TODO 直接获取这个接口
    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showTasks(List<Task> tasks) {
        mTasksAdapter.replaceData(tasks);
        mLlTasksView.setVisibility(View.VISIBLE);
        mViewNoTasks.setVisibility(View.GONE);
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
        intent.putExtra(ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showTaskToDelete(Task task) {
        Snackbar.make(getView(), getString(R.string.task_to_delete), Snackbar.LENGTH_LONG)
                .setAction(R.string.enter, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//TODO
                    }
                }).show();
    }

    @Override
    public void showTaskMarkedCompleted() {
        showMessage(getString(R.string.task_marked_complete));
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    @Override
    public void showNoCompletedTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );
    }

    @Override
    public void showNoActiveTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false
        );
    }

    @Override
    public void showFilteringPopupMenu() {
        PopupMenu popup = new PopupMenu(getActivity(),getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks,popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.active:
                        if (mActionBar !=null){
                            mActionBar.setTitle(getString(R.string.nav_active));
                        }
                        mPresenter.setFiltering(TaskFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        if (mActionBar != null){
                            mActionBar.setTitle(getString(R.string.nav_completed));
                        }
                        mPresenter.setFiltering(TaskFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        if (mActionBar!=null){
                            mActionBar.setTitle(getString(R.string.nav_all));
                        }
                        mPresenter.setFiltering(TaskFilterType.ALL_TASK);
                        break;
                }
                mPresenter.loadTasks(false);
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void showAllFilterLabel() {
//        mTvFilteringLabel.setText(getString(R.string.nav_all));
    }

    @Override
    public void showCompletedFilterLabel() {
//        mTvFilteringLabel.setText(getString(R.string.nav_completed));
    }

    @Override
    public void showActiveFilterLabel() {
//        mTvFilteringLabel.setText(getString(R.string.nav_active));
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


}
