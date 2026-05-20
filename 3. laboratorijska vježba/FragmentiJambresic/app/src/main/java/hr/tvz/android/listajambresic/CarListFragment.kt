package hr.tvz.android.listajambresic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.listajambresic.model.Car

class CarListFragment : Fragment() {
    interface OnCarSelectedListener {
        fun onCarSelected(car: Car)
    }

    private var listener: OnCarSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnCarSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dohvat automobila iz argumenta (proslijeđenog iz MainActivity)
        val cars = arguments?.getParcelableArrayList<Car>(ARG_CARS) ?: emptyList()
        recyclerView.adapter = CarAdapter(cars) { car ->
            listener?.onCarSelected(car)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        const val ARG_CARS = "arg_cars"

        fun newInstance(cars: List<Car>): CarListFragment {
            return CarListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_CARS, ArrayList(cars))
                }
            }
        }
    }
}