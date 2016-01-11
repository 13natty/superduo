package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

public class WidgetProvider extends AppWidgetProvider {
    public static final String DEBUG_TAG = "WidgetProvider";

    public static String getCurrentTime() {
        String currentTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        return currentTime;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        Log.i(DEBUG_TAG, "OnUpdate");
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            String currentTime = getCurrentTime();
            views.setTextViewText(R.id.textViewWidgetTime, currentTime);

            views.setTextViewText(R.id.textViewWidgetLabel, context.getString(R.string.title_widget_scores));

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.buttonWidget, pendingIntent);

            views.setRemoteAdapter(R.id.scores_list, new Intent(context, ScoresWidgetRemoteViewsService.class));
            views.setEmptyView(R.id.scores_list, R.id.scores_empty);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /*our method to update the widget*/
    static void updateOurWidget(Context context, AppWidgetManager appWidgetManager,
                                int widgetId, String widgetLabel) {
        Log.i(DEBUG_TAG, "updating widget");

        String currentTime = getCurrentTime();
//            set up the pending intent for the button to open a new activity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.textViewWidgetLabel, widgetLabel);
        views.setTextViewText(R.id.textViewWidgetTime, currentTime);
        views.setOnClickPendingIntent(R.id.buttonWidget, pendingIntent);

        // Tell the widget manager to update the widget
        appWidgetManager.updateAppWidget(widgetId, views);
    }

}