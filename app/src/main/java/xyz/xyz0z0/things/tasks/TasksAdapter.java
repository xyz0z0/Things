package xyz.xyz0z0.things.tasks;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import xyz.xyz0z0.things.R;
import xyz.xyz0z0.things.data.Task;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<Task> mTasks;
    private TaskItemListener mListener;
    private Context mContext;

    public TasksAdapter(List<Task> tasks, TaskItemListener listener) {
        setList(tasks);
        mListener = listener;
    }

    private void setList(List<Task> tasks) {
        mTasks = tasks;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks_list, parent, false);
        mContext = parent.getContext();
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        if (mTasks == null) {
            return;
        }
        final Task task = mTasks.get(position);

        holder.chkComplete.setChecked(task.isCompleted());
        holder.tvTitle.setText(task.getTitleForList());
        if (task.isCompleted()) {
            holder.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvTitle.getPaint().setFlags(0);
        }

        holder.chkComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!task.isCompleted()) {
                    mListener.onCompleteTaskClick(task);
                } else {
                    mListener.onActivateTaskClick(task);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTaskClick(task);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onTaskLongClick(task);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    public void replaceData(List<Task> tasks){
        setList(tasks);
        notifyDataSetChanged();
    }

    public interface TaskItemListener {

        void onTaskClick(Task clickedTask);

        void onTaskLongClick(Task longClickedTask);

        void onCompleteTaskClick(Task completedTask);

        void onActivateTaskClick(Task activatedTask);
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {
        CheckBox chkComplete;
        TextView tvTitle;

        TasksViewHolder(View itemView) {
            super(itemView);
            chkComplete = (CheckBox) itemView.findViewById(R.id.chk_complete);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

}
