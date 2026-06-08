package hr.tvz.android.mvpjambresic

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class BmwApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}