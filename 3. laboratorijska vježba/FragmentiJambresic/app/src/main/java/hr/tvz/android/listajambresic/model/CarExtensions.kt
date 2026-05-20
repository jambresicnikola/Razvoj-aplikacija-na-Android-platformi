package hr.tvz.android.listajambresic.model

import android.content.Context
import hr.tvz.android.listajambresic.R

fun CarEntity.toCar(context: Context): Car {
    val resId = context.resources.getIdentifier(imageResName, "drawable", context.packageName)
    return Car(
        id = id,
        modelName = modelName,
        series = series,
        year = year,
        engineType = engineType,
        horsepower = horsepower,
        description = description,
        imageResId = if (resId != 0) resId else R.drawable.bmw_car_bg,
        websiteUrl = websiteUrl
    )
}