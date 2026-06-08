package hr.tvz.android.mvpjambresic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey val id: Int,
    val modelName: String,
    val series: String,
    val year: Int,
    val engineType: String,
    val horsepower: Int,
    val description: String,
    val imageResName: String,
    val websiteUrl: String
)