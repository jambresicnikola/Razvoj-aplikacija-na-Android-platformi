package hr.tvz.android.mvpjambresic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import hr.tvz.android.mvpjambresic.util.Constants

class ShareReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Constants.SHARE_ACTION) {
            val model = intent.getStringExtra(Constants.EXTRA_CAR_MODEL) ?: "Unknown"
            val series = intent.getStringExtra(Constants.EXTRA_CAR_SERIES) ?: ""
            val year = intent.getIntExtra(Constants.EXTRA_CAR_YEAR, 0)

            Toast.makeText(
                context,
                "📡 Broadcast received: $model ($series, $year)",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}