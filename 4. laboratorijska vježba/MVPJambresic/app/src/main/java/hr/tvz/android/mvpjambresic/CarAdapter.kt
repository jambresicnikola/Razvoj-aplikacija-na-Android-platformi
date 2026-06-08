package hr.tvz.android.mvpjambresic

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import hr.tvz.android.mvpjambresic.model.Car

class CarAdapter(
    private val cars: List<Car>,
    private val onCarClick: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCarThumbnail: SimpleDraweeView = itemView.findViewById(R.id.imgCarThumbnail)
        val tvCarModel: TextView = itemView.findViewById(R.id.tvCarModel)
        val tvCarSeries: TextView = itemView.findViewById(R.id.tvCarSeries)
        val tvCarYear: TextView = itemView.findViewById(R.id.tvCarYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
        holder.tvCarModel.text = car.modelName
        holder.tvCarSeries.text = car.series
        holder.tvCarYear.text = car.year.toString()
        holder.imgCarThumbnail.setImageURI(Uri.parse(car.imageUrl))

        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)

        holder.itemView.setOnClickListener { onCarClick(car) }
    }

    override fun getItemCount(): Int = cars.size
}