package xyz.xyz0z0.things.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import xyz.xyz0z0.things.R;
import xyz.xyz0z0.things.data.Task;
import xyz.xyz0z0.things.data.source.TasksDataSource;
import xyz.xyz0z0.things.data.source.TasksRepository;
import xyz.xyz0z0.things.data.source.local.TasksLocalDataSource;

/**
 * Created by CC on 2017/3/16.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        List<Task> mTasks = new ArrayList<>();
        Context mContext = null;

        public RemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {
            initData();
        }

        @Override
        public void onDataSetChanged() {
            Log.d("cxg", "onDataSetChanged: ");
            initData();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Task task = mTasks.get(position);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_list);
            remoteViews.setTextViewText(R.id.tv_widget_title, task.getTitleForList());
            remoteViews.setTextColor(R.id.tv_widget_title, Color.BLACK);

            final Intent doneIntent = new Intent();
            doneIntent.putExtra(AppWidgetProvider.EXTRA_STRING, task.getId());
            doneIntent.putExtra(AppWidgetProvider.EXTRA_DO, AppWidgetProvider.DO_DONE);
            remoteViews.setOnClickFillInIntent(R.id.ib_widget_done, doneIntent);

            final Intent fillInIntent = new Intent();
            fillInIntent.putExtra(AppWidgetProvider.EXTRA_STRING, task.getId());
            fillInIntent.putExtra(AppWidgetProvider.EXTRA_DO, AppWidgetProvider.DO_EDIT);
            remoteViews.setOnClickFillInIntent(R.id.tv_widget_title, fillInIntent);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void initData() {
            mTasks.clear();
            TasksLocalDataSource.getInstance(mContext).getCompletedTasks(new TasksDataSource.LoadTasksCallback() {
                @Override
                public void onTaskLoaded(List<Task> tasks) {
                    mTasks = tasks;
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
    }
}
