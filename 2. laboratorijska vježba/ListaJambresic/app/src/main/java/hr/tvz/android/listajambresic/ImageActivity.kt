package hr.tvz.android.listajambresic

import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.android.listajambresic.model.Car
import hr.tvz.android.listajambresic.util.Constants

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

        val imgCarFull = findViewById<ImageView>(R.id.imgCarFull)
        val tvImageCarName = findViewById<TextView>(R.id.tvImageCarName)

        imgCarFull.setImageResource(car.imageResId)
        tvImageCarName.text = car.modelName

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        imgCarFull.startAnimation(fadeIn)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}