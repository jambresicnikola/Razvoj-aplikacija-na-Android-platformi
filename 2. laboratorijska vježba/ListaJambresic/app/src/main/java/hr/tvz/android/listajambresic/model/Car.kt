package hr.tvz.android.listajambresic.model

import android.os.Parcel
import android.os.Parcelable

data class Car(
    val id: Int,
    val modelName: String,
    val series: String,
    val year: Int,
    val engineType: String,
    val horsepower: Int,
    val description: String,
    val imageResId: Int,
    val websiteUrl: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        modelName = parcel.readString() ?: "",
        series = parcel.readString() ?: "",
        year = parcel.readInt(),
        engineType = parcel.readString() ?: "",
        horsepower = parcel.readInt(),
        description = parcel.readString() ?: "",
        imageResId = parcel.readInt(),
        websiteUrl = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(modelName)
        parcel.writeString(series)
        parcel.writeInt(year)
        parcel.writeString(engineType)
        parcel.writeInt(horsepower)
        parcel.writeString(description)
        parcel.writeInt(imageResId)
        parcel.writeString(websiteUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car = Car(parcel)
        override fun newArray(size: Int): Array<Car?> = arrayOfNulls(size)
    }
}