package hr.tvz.android.mvpjambresic.model

import android.os.Parcel
import android.os.Parcelable

data class Car(
    val id: String = "",
    val modelName: String = "",
    val series: String = "",
    val year: Int = 0,
    val engineType: String = "",
    val horsepower: Int = 0,
    val description: String = "",
    val imageUrl: String = "",
    val websiteUrl: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        modelName = parcel.readString() ?: "",
        series = parcel.readString() ?: "",
        year = parcel.readInt(),
        engineType = parcel.readString() ?: "",
        horsepower = parcel.readInt(),
        description = parcel.readString() ?: "",
        imageUrl = parcel.readString() ?: "",
        websiteUrl = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(modelName)
        parcel.writeString(series)
        parcel.writeInt(year)
        parcel.writeString(engineType)
        parcel.writeInt(horsepower)
        parcel.writeString(description)
        parcel.writeString(imageUrl)
        parcel.writeString(websiteUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car = Car(parcel)
        override fun newArray(size: Int): Array<Car?> = arrayOfNulls(size)
    }
}