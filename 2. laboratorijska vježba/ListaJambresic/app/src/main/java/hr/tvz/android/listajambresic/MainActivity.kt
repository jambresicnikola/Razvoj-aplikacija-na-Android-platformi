package hr.tvz.android.listajambresic

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.listajambresic.model.Car
import hr.tvz.android.listajambresic.util.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var shareReceiver: ShareReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shareReceiver = ShareReceiver()
        val filter = IntentFilter(Constants.SHARE_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(shareReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(shareReceiver, filter)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val cars = createBmwCars()
        val adapter = CarAdapter(cars) { car ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_CAR, car)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(shareReceiver)
    }

    private fun createBmwCars(): List<Car> = listOf(
        Car(
            id = 1,
            modelName = "BMW M3 Competition",
            series = "M3",
            year = 2024,
            engineType = "3.0L Twin-Turbo I6",
            horsepower = 503,
            description = "The BMW M3 Competition is the pinnacle of sports sedan performance. Powered by the S58 engine, it delivers breathtaking acceleration while remaining surprisingly livable as a daily driver.",
            imageResId = R.drawable.bmw_m3,
            websiteUrl = "https://www.bmw.com/en/all-models/m-series/m3-sedan/2020/bmw-m3-sedan.html"
        ),
        Car(
            id = 2,
            modelName = "BMW M5 CS",
            series = "M5",
            year = 2022,
            engineType = "4.4L Twin-Turbo V8",
            horsepower = 627,
            description = "The BMW M5 CS is the most powerful production BMW ever built. Lightweight construction and an aggressive tune make it a true track weapon wrapped in luxury.",
            imageResId = R.drawable.bmw_m5,
            websiteUrl = "https://www.bmw.com/en/all-models/m-series/m5-sedan/2020/bmw-m5-sedan.html"
        ),
        Car(
            id = 3,
            modelName = "BMW X5 M Competition",
            series = "X5 M",
            year = 2024,
            engineType = "4.4L Twin-Turbo V8",
            horsepower = 617,
            description = "The BMW X5 M Competition proves that SUVs can be thrilling. With supercar performance and everyday practicality, it redefines the performance SUV segment.",
            imageResId = R.drawable.bmw_x5m,
            websiteUrl = "https://www.bmw.com/en/all-models/x-series/x5-m/2020/bmw-x5-m.html"
        ),
        Car(
            id = 4,
            modelName = "BMW i4 M50",
            series = "i4",
            year = 2024,
            engineType = "Dual Electric Motor",
            horsepower = 536,
            description = "The BMW i4 M50 is BMW's first fully electric M car. Instant torque, zero emissions and a range of over 500 km make it the future of performance driving.",
            imageResId = R.drawable.bmw_i4,
            websiteUrl = "https://www.bmw.com/en/all-models/bmw-i/i4/2021/bmw-i4.html"
        ),
        Car(
            id = 5,
            modelName = "BMW Z4 M40i",
            series = "Z4",
            year = 2024,
            engineType = "3.0L Twin-Turbo I6",
            horsepower = 382,
            description = "The BMW Z4 M40i is the quintessential roadster experience. A retractable soft-top roof, rear-wheel drive and a sweet-sounding straight-six engine deliver pure driving joy.",
            imageResId = R.drawable.bmw_z4,
            websiteUrl = "https://www.bmw.com/en/all-models/z-series/z4/2018/bmw-z4.html"
        ),
        Car(
            id = 6,
            modelName = "BMW M8 Competition",
            series = "M8",
            year = 2023,
            engineType = "4.4L Twin-Turbo V8",
            horsepower = 617,
            description = "The BMW M8 Competition Gran Coupe is the ultimate expression of luxury and performance. Four doors, four seats, and enough power to humble almost any supercar.",
            imageResId = R.drawable.bmw_m8,
            websiteUrl = "https://www.bmw.com/en/all-models/m-series/m8-coupe/2018/bmw-m8-coupe.html"
        )
    )
}