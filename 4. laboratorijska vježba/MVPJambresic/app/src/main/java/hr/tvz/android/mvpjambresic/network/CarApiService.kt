package hr.tvz.android.mvpjambresic.network

import hr.tvz.android.mvpjambresic.model.Car
import retrofit2.http.GET

interface CarApiService {

    @GET("cars")
    suspend fun getCars(): List<Car>
}