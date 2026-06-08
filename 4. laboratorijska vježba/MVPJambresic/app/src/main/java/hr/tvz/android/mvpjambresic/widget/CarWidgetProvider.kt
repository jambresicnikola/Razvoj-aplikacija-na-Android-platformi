package hr.tvz.android.mvpjambresic.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import hr.tvz.android.mvpjambresic.MainActivity
import hr.tvz.android.mvpjambresic.R
import hr.tvz.android.mvpjambresic.repository.CarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class CarWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_car)

        // Klik na widget otvara app
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widgetContainer, pendingIntent)

        // Dohvati zadnji auto s API-ja
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lastCar = CarRepository().getLastCar()
                if (lastCar != null) {
                    withContext(Dispatchers.Main) {
                        views.setTextViewText(R.id.tvWidgetModelName, lastCar.modelName)
                        views.setTextViewText(R.id.tvWidgetSeries, "${lastCar.series} • ${lastCar.year}")
                    }

                    // Učitaj sliku
                    val bitmap = loadBitmapFromUrl(lastCar.imageUrl)
                    if (bitmap != null) {
                        withContext(Dispatchers.Main) {
                            views.setImageViewBitmap(R.id.imgWidget, bitmap)
                        }
                    }
                }
            } catch (e: Exception) {
                // Ostavi default vrijednosti
            } finally {
                withContext(Dispatchers.Main) {
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }
    }

    private fun loadBitmapFromUrl(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.connect()
            BitmapFactory.decodeStream(connection.inputStream)
        } catch (e: Exception) {
            null
        }
    }
}