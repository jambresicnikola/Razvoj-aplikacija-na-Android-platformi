package hr.tvz.android.mvpjambresic

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import hr.tvz.android.mvpjambresic.model.Car
import hr.tvz.android.mvpjambresic.presenter.CarListPresenter
import hr.tvz.android.mvpjambresic.util.Constants
import hr.tvz.android.mvpjambresic.view.ICarListView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ICarListView, CarListFragment.OnCarSelectedListener {

    private lateinit var presenter: CarListPresenter
    private lateinit var shareReceiver: ShareReceiver
    private var mediaPlayer: MediaPlayer? = null
    private var savedState: Bundle? = null

    private val isLandscape: Boolean
        get() = findViewById<View>(R.id.fragmentContainerDetail) != null

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedState = savedInstanceState

        // Broadcast receiver
        shareReceiver = ShareReceiver()
        val filter = IntentFilter(Constants.SHARE_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(shareReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(shareReceiver, filter)
        }

        requestNotificationPermission()

        // MVP Presenter
        presenter = CarListPresenter(this)
        presenter.loadCars()

        // MediaPlayer — pozadinska glazba
        setupMediaPlayer()
    }

    private fun setupMediaPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.bmw_theme)
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(0.3f, 0.3f)
        } catch (e: Exception) {
            // Ako nema audio fajla, ignoriraj
        }
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        unregisterReceiver(shareReceiver)
        presenter.onDestroy()
    }

    // ICarListView metode
    override fun showCars(cars: List<Car>) {
        setupFragments(cars)
    }

    override fun showLoading() {
        findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setupFragments(cars: List<Car>) {
        if (savedState != null) return

        val listFragment = CarListFragment.newInstance(cars)

        if (isLandscape) {
            val detailFragment = CarDetailFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerList, listFragment, "LIST_FRAGMENT")
                .replace(R.id.fragmentContainerDetail, detailFragment, "DETAIL_FRAGMENT")
                .commitAllowingStateLoss()

            if (cars.isNotEmpty()) {
                supportFragmentManager.executePendingTransactions()
                val detail = supportFragmentManager
                    .findFragmentByTag("DETAIL_FRAGMENT") as? CarDetailFragment
                detail?.loadCar(cars.first())
            }
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerList, listFragment, "LIST_FRAGMENT")
                .commitAllowingStateLoss()
        }
    }

    override fun onCarSelected(car: Car) {
        if (isLandscape) {
            val detailFragment = supportFragmentManager
                .findFragmentByTag("DETAIL_FRAGMENT") as? CarDetailFragment
            detailFragment?.loadCar(car)
        } else {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_CAR, car)
            startActivity(intent)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}