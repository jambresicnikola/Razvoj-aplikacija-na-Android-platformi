package hr.tvz.android.mvpjambresic.view

import hr.tvz.android.mvpjambresic.model.Car

interface ICarListView {
    fun showCars(cars: List<Car>)
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
}