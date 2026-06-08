package hr.tvz.android.mvpjambresic.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.tvz.android.mvpjambresic.model.CarEntity

@Dao
interface CarDao {

    @Query("SELECT * FROM cars")
    suspend fun getAllCars(): List<CarEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cars: List<CarEntity>)

    @Query("SELECT COUNT(*) FROM cars")
    suspend fun getCarCount(): Int
}