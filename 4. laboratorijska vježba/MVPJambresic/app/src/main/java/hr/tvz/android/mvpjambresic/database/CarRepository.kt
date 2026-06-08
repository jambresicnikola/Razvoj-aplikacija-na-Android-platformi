package hr.tvz.android.mvpjambresic.database

import hr.tvz.android.mvpjambresic.model.CarEntity

class CarRepository(private val carDao: CarDao) {

    suspend fun getAllCars(): List<CarEntity> = carDao.getAllCars()

    suspend fun initializeIfEmpty() {
        if (carDao.getCarCount() == 0) {
            carDao.insertAll(getInitialCars())
        }
    }

    private fun getInitialCars(): List<CarEntity> = listOf(
        CarEntity(1, "BMW M3 Competition", "M3", 2024, "3.0L Twin-Turbo I6", 503,
            "The BMW M3 Competition is the pinnacle of sports sedan performance. Powered by the S58 engine, it delivers breathtaking acceleration while remaining surprisingly livable as a daily driver.",
            "bmw_m3", "https://www.bmw.com/en/all-models/m-series/m3-sedan/2020/bmw-m3-sedan.html"),
        CarEntity(2, "BMW M5 CS", "M5", 2022, "4.4L Twin-Turbo V8", 627,
            "The BMW M5 CS is the most powerful production BMW ever built. Lightweight construction and an aggressive tune make it a true track weapon wrapped in luxury.",
            "bmw_m5", "https://www.bmw.com/en/all-models/m-series/m5-sedan/2020/bmw-m5-sedan.html"),
        CarEntity(3, "BMW X5 M Competition", "X5 M", 2024, "4.4L Twin-Turbo V8", 617,
            "The BMW X5 M Competition proves that SUVs can be thrilling. With supercar performance and everyday practicality, it redefines the performance SUV segment.",
            "bmw_x5m", "https://www.bmw.com/en/all-models/x-series/x5-m/2020/bmw-x5-m.html"),
        CarEntity(4, "BMW i4 M50", "i4", 2024, "Dual Electric Motor", 536,
            "The BMW i4 M50 is BMW's first fully electric M car. Instant torque, zero emissions and a range of over 500 km make it the future of performance driving.",
            "bmw_i4", "https://www.bmw.com/en/all-models/bmw-i/i4/2021/bmw-i4.html"),
        CarEntity(5, "BMW Z4 M40i", "Z4", 2024, "3.0L Twin-Turbo I6", 382,
            "The BMW Z4 M40i is the quintessential roadster experience. A retractable soft-top roof, rear-wheel drive and a sweet-sounding straight-six deliver pure driving joy.",
            "bmw_z4", "https://www.bmw.com/en/all-models/z-series/z4/2018/bmw-z4.html"),
        CarEntity(6, "BMW M8 Competition", "M8", 2023, "4.4L Twin-Turbo V8", 617,
            "The BMW M8 Competition Gran Coupe is the ultimate expression of luxury and performance. Four doors, four seats, and enough power to humble almost any supercar.",
            "bmw_m8", "https://www.bmw.com/en/all-models/m-series/m8-coupe/2018/bmw-m8-coupe.html")
    )
}