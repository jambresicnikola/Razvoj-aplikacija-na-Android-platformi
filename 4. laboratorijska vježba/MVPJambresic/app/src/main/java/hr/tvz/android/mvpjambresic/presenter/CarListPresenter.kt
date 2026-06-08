package hr.tvz.android.mvpjambresic.presenter

import hr.tvz.android.mvpjambresic.repository.CarRepository
import hr.tvz.android.mvpjambresic.view.ICarListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarListPresenter(private val view: ICarListView) {

    private val repository = CarRepository()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun loadCars() {
        view.showLoading()
        scope.launch {
            try {
                val cars = withContext(Dispatchers.IO) { repository.getAllCars() }
                view.showCars(cars)
            } catch (e: Exception) {
                view.showError("Failed to load cars: ${e.message}")
            } finally {
                view.hideLoading()
            }
        }
    }

    fun onDestroy() {
        scope.cancel()
    }
}