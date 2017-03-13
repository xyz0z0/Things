package xyz.xyz0z0.things.tasks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import xyz.xyz0z0.things.R;
import xyz.xyz0z0.things.data.source.TasksRepository;
import xyz.xyz0z0.things.data.source.local.TasksLocalDataSource;
import xyz.xyz0z0.things.util.ActivityUtils;

public class TasksActivity extends AppCompatActivity {

    public ActionBar mActionBar;
    private TasksPresenter mTasksPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();

        // 初始化，此时tasksFragment为null
        TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (tasksFragment == null) {
            // 实例
            tasksFragment = TasksFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),tasksFragment,R.id.content);
        }

        mTasksPresenter = new TasksPresenter(
                TasksRepository.getInstance(
                        TasksLocalDataSource.getInstance(getApplicationContext())),tasksFragment);
    }
}
