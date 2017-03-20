package xyz.xyz0z0.things.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;
import android.widget.Toast;

import xyz.xyz0z0.things.R;
import xyz.xyz0z0.things.addedittask.AddEditTaskActivity;
import xyz.xyz0z0.things.addedittask.AddEditTaskFragment;
import xyz.xyz0z0.things.data.source.TasksRepository;
import xyz.xyz0z0.things.data.source.local.TasksLocalDataSource;
import xyz.xyz0z0.things.tasks.TasksActivity;

/**
 * Created by CC on 2017/3/16.
 */

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    public static final String ACTION_DONE = "xyz.xyz0z0.things.ACTION_DONE";
    public static final String EXTRA_STRING = "xyz.xyz0z0.things.EXTRA_STRING";
    public static final String EXTRA_DO = "xyz.xyz0z0.things.EXTRA_DO";
    public static final int DO_DONE = 1;
    public static final int DO_EDIT = 2;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_DONE)) {
            String taskId = intent.getExtras().getString(EXTRA_STRING);
            int doInt = intent.getExtras().getInt(EXTRA_DO);

            switch (doInt) {
                case DO_DONE:
                    // TODO 如果使用res操作数据，会有一个数据库问题
                    TasksLocalDataSource.getInstance(context).completeTask(taskId);
                    Toast.makeText(context, "完成一个事项", Toast.LENGTH_SHORT).show();
                    break;
                case DO_EDIT:
                    Intent editIntent = new Intent(context, AddEditTaskActivity.class);
                    editIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    editIntent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                    context.startActivity(editIntent);
                    break;
                default:
                    break;
            }
        }
        super.onReceive(context, intent);

        // 更新 Widget TODO
        final AppWidgetManager manager = AppWidgetManager.getInstance(context);
        final ComponentName cn = new ComponentName(context, AppWidgetProvider.class);
        manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(cn), R.id.lv_widget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = initViews(context, appWidgetManager, widgetId);

            // 设置 Appwidget Done 点击事件
            final Intent doneIntent = new Intent(context, AppWidgetProvider.class);
            doneIntent.setAction(ACTION_DONE);
            doneIntent.setData(Uri.parse(doneIntent.toUri(Intent.URI_INTENT_SCHEME)));
            final PendingIntent donePengdingIntent = PendingIntent
                    .getBroadcast(context, 0, doneIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.lv_widget, donePengdingIntent);

            // 设置 AppWidget Add 点击事件
            Intent addIntent = new Intent(context, AddEditTaskActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(AddEditTaskActivity.class);
            stackBuilder.addNextIntent(addIntent);
            PendingIntent addStackIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent addPendIntent = PendingIntent.getActivity(context, 0, addIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.ib_widget, addStackIntent);

            // 设置 AppWidget label 点击事件
            Intent labelIntent = new Intent(context, TasksActivity.class);
            PendingIntent labelPendingIntent = PendingIntent.getActivity(context, 0, labelIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.tv_widget, labelPendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    private RemoteViews initViews(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.lv_widget, intent);
        return remoteViews;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

}
