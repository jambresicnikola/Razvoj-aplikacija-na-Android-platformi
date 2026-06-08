package hr.tvz.android.mvpjambresic

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.button.MaterialButton
import hr.tvz.android.mvpjambresic.model.Car
import hr.tvz.android.mvpjambresic.util.Constants

class DetailActivity : AppCompatActivity() {

    private lateinit var car: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_CAR, Car::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.EXTRA_CAR)!!
        }

        supportActionBar?.title = car.modelName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViews()
    }

    private fun setupViews() {
        val imgCarDetail = findViewById<SimpleDraweeView>(R.id.imgCarDetail)
        val tvModelName = findViewById<TextView>(R.id.tvDetailModelName)
        val tvSeries = findViewById<TextView>(R.id.tvDetailSeries)
        val tvYear = findViewById<TextView>(R.id.tvDetailYear)
        val tvEngine = findViewById<TextView>(R.id.tvDetailEngine)
        val tvHorsepower = findViewById<TextView>(R.id.tvDetailHorsepower)
        val tvDescription = findViewById<TextView>(R.id.tvDetailDescription)
        val btnWebsite = findViewById<MaterialButton>(R.id.btnVisitWebsite)

        imgCarDetail.setImageURI(Uri.parse(car.imageUrl))
        tvModelName.text = car.modelName
        tvSeries.text = car.series
        tvYear.text = car.year.toString()
        tvEngine.text = car.engineType
        tvHorsepower.text = "${car.horsepower} ${getString(R.string.label_hp)}"
        tvDescription.text = car.description

        imgCarDetail.setOnClickListener {
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra(Constants.EXTRA_CAR, car)
            startActivity(intent)
        }

        btnWebsite.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(car.websiteUrl)))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                showShareDialog()
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showShareDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.share_dialog_title))
            .setMessage(getString(R.string.share_dialog_message))
            .setPositiveButton(getString(R.string.share_dialog_yes)) { dialog, _ ->
                sendShareBroadcast()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.share_dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun sendShareBroadcast() {
        val intent = Intent(Constants.SHARE_ACTION).apply {
            setPackage(packageName)
            putExtra(Constants.EXTRA_CAR_MODEL, car.modelName)
            putExtra(Constants.EXTRA_CAR_SERIES, car.series)
            putExtra(Constants.EXTRA_CAR_YEAR, car.year)
        }
        sendBroadcast(intent)
    }
}