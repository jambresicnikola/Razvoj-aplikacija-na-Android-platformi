package hr.tvz.android.mvpjambresic.repository

import hr.tvz.android.mvpjambresic.model.Car
import hr.tvz.android.mvpjambresic.network.RetrofitClient

class CarRepository {

    private val apiService = RetrofitClient.instance

    suspend fun getAllCars(): List<Car> = apiService.getCars()

    suspend fun getLastCar(): Car? = apiService.getCars().lastOrNull()
}