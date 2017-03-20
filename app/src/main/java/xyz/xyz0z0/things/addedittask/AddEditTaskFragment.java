package xyz.xyz0z0.things.addedittask;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import xyz.xyz0z0.things.R;
import xyz.xyz0z0.things.data.Task;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddEditTaskContract.Presenter mPresenter;

    private EditText mEtTitle;

    private EditText mEtDescription;

    private String mEditedTaskId;

    public AddEditTaskFragment() {

    }

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_addededittask, container, false);

        mEtTitle = (EditText) root.findViewById(R.id.et_task_title);
        mEtDescription = (EditText) root.findViewById(R.id.et_task_description);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTaskIdIfAny();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_addeditact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewTask()) {
                    mPresenter.createTask(mEtTitle.getText().toString(), mEtDescription.getText().toString());
                } else {
                    mPresenter.updateTask(new Task(mEtTitle.getText().toString(), mEtDescription.getText().toString(), mEditedTaskId, false));
                }
                // 发送广播给AppWidget
                Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                getActivity().sendBroadcast(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPresenter(AddEditTaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(mEtTitle,getString(R.string.empty_task_message),Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTaskTitle(String title) {
        mEtTitle.setText(title);
    }

    @Override
    public void setTaskDescription(String description) {
        mEtDescription.setText(description);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void setTaskIdIfAny() {
        if (getArguments() != null && getArguments().containsKey(ARGUMENT_EDIT_TASK_ID)) {
            mEditedTaskId = getArguments().getString(ARGUMENT_EDIT_TASK_ID);
        }
    }

    private boolean isNewTask() {
        return mEditedTaskId == null;
    }

}
