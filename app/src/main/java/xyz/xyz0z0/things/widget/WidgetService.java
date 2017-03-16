package xyz.xyz0z0.things.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import xyz.xyz0z0.things.data.Task;
import xyz.xyz0z0.things.data.source.TasksRepository;
import xyz.xyz0z0.things.data.source.local.TasksLocalDataSource;

/**
 * Created by CC on 2017/3/16.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory();
    }

    class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        List<Task> mTasks;
        Context mContext = null;
        

        @Override
        public void onCreate() {
            mTasks.clear();
            mTasks = TasksRepository.getInstance(TasksLocalDataSource.getInstance(mContext)).getTasks();
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
