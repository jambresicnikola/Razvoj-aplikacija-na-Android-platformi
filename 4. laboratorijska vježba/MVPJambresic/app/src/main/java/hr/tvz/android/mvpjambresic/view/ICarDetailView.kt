package hr.tvz.android.mvpjambresic.view

import hr.tvz.android.mvpjambresic.model.Car

interface ICarDetailView {
    fun showCarDetails(car: Car)
    fun showError(message: String)
}