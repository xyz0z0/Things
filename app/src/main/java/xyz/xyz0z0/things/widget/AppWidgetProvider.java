package xyz.xyz0z0.things.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.RemoteViews;

import xyz.xyz0z0.things.R;

/**
 * Created by CC on 2017/3/16.
 */

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int widgetId:appWidgetIds){
            RemoteViews remoteViews = initViews(context,appWidgetManager,widgetId);
        }

    }

    private RemoteViews initViews(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

//        remoteViews.setRemoteAdapter;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

}
