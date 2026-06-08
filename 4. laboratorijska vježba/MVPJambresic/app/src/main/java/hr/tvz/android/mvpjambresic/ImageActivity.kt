package hr.tvz.android.mvpjambresic

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.view.SimpleDraweeView
import hr.tvz.android.mvpjambresic.model.Car
import hr.tvz.android.mvpjambresic.util.Constants

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_CAR, Car::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.EXTRA_CAR)!!
        }

        supportActionBar?.title = car.modelName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imgCarFull = findViewById<SimpleDraweeView>(R.id.imgCarFull)
        val tvImageCarName = findViewById<TextView>(R.id.tvImageCarName)

        imgCarFull.setImageURI(Uri.parse(car.imageUrl))
        tvImageCarName.text = car.modelName

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        imgCarFull.startAnimation(fadeIn)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}