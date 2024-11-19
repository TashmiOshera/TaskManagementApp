package com.example.todomanager

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class TaskListWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_task_list)

        // Retrieve tasks from SharedPreferences
        val sharedPreferences = context.getSharedPreferences("ProductivityAppPrefs", Context.MODE_PRIVATE)
        val taskList = sharedPreferences.getStringSet("task_list", setOf()) ?: setOf()

        views.setTextViewText(R.id.widget_text_view, if (taskList.isEmpty()) {
            "Upcoming Tasks: None"
        } else {
            "Upcoming Tasks:\n${taskList.joinToString("\n")}"
        })

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
