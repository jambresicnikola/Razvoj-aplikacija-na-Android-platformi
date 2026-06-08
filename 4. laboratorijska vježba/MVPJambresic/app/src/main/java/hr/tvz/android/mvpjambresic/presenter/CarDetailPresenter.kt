package hr.tvz.android.mvpjambresic.presenter

import hr.tvz.android.mvpjambresic.model.Car
import hr.tvz.android.mvpjambresic.view.ICarDetailView

class CarDetailPresenter(private val view: ICarDetailView) {

    fun loadCar(car: Car) {
        view.showCarDetails(car)
    }
}