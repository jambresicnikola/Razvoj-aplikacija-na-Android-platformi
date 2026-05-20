package hr.tvz.android.listajambresic
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import hr.tvz.android.listajambresic.database.AppDatabase
import hr.tvz.android.listajambresic.database.CarRepository
import hr.tvz.android.listajambresic.model.Car
import hr.tvz.android.listajambresic.model.toCar
import hr.tvz.android.listajambresic.util.Constants
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CarListFragment.OnCarSelectedListener {

    private lateinit var shareReceiver: ShareReceiver

    // Provjera je li landscape (postoji li desni container)
    private val isLandscape: Boolean
        get() = findViewById<View>(R.id.fragmentContainerDetail) != null

    // Permission launcher za notifikacije (API 33+)
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* Korisnik je odabrao, nastavljamo bez obzira */ }

    private var savedState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        com.google.firebase.messaging.FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                android.util.Log.d("FCM_TOKEN", "Token: $token")
            }

        savedState = savedInstanceState

        shareReceiver = ShareReceiver()
        val filter = IntentFilter(Constants.SHARE_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(shareReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(shareReceiver, filter)
        }

        requestNotificationPermission()
        loadCarsFromDatabase()
    }

    private fun loadCarsFromDatabase() {
        val db = AppDatabase.getDatabase(this)
        val repository = CarRepository(db.carDao())

        lifecycleScope.launch {
            repository.initializeIfEmpty()
            val carEntities = repository.getAllCars()
            val cars = carEntities.map { it.toCar(this@MainActivity) }
            setupFragments(cars)
        }
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

            // Automatski prikaži prvi auto
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

    // Callback iz CarListFragment — klik na auto
    override fun onCarSelected(car: Car) {
        if (isLandscape) {
            // Traži po tagu umjesto po ID-u — pouzdanije
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
                != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(shareReceiver)
    }
}