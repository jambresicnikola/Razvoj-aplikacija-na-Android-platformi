package hr.tvz.android.listajambresic

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.material.button.MaterialButton
import hr.tvz.android.listajambresic.model.Car
import hr.tvz.android.listajambresic.util.Constants

class CarDetailFragment : Fragment() {

    private var car: Car? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_CAR, Car::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_CAR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Prikaži auto ako je već postavljen prije kreiranja viewa
        car?.let { loadCar(view, it) }

        // Share menu za fragment
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == R.id.action_share) {
                    car?.let { showShareDialog(it) }
                    true
                } else false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun loadCar(car: Car) {
        this.car = car
        // Ako je view već kreiran, odmah prikaži; inače će onViewCreated to napraviti
        if (isAdded && view != null) {
            loadCar(requireView(), car)
        }
    }

    private fun loadCar(view: View, car: Car) {
        view.findViewById<ImageView>(R.id.imgCarDetail).setImageResource(car.imageResId)
        view.findViewById<TextView>(R.id.tvDetailModelName).text = car.modelName
        view.findViewById<TextView>(R.id.tvDetailSeries).text = car.series
        view.findViewById<TextView>(R.id.tvDetailYear).text = car.year.toString()
        view.findViewById<TextView>(R.id.tvDetailEngine).text = car.engineType
        view.findViewById<TextView>(R.id.tvDetailHorsepower).text =
            "${car.horsepower} ${getString(R.string.label_hp)}"
        view.findViewById<TextView>(R.id.tvDetailDescription).text = car.description

        view.findViewById<ImageView>(R.id.imgCarDetail).setOnClickListener {
            val intent = Intent(requireContext(), ImageActivity::class.java)
            intent.putExtra(Constants.EXTRA_CAR, car)
            startActivity(intent)
        }

        view.findViewById<MaterialButton>(R.id.btnVisitWebsite).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(car.websiteUrl)))
        }
    }

    private fun showShareDialog(car: Car) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.share_dialog_title))
            .setMessage(getString(R.string.share_dialog_message))
            .setPositiveButton(getString(R.string.share_dialog_yes)) { dialog, _ ->
                val intent = Intent(Constants.SHARE_ACTION).apply {
                    setPackage(requireContext().packageName)
                    putExtra(Constants.EXTRA_CAR_MODEL, car.modelName)
                    putExtra(Constants.EXTRA_CAR_SERIES, car.series)
                    putExtra(Constants.EXTRA_CAR_YEAR, car.year)
                }
                requireContext().sendBroadcast(intent)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.share_dialog_no)) { d, _ -> d.dismiss() }
            .show()
    }

    companion object {
        const val ARG_CAR = "arg_car"

        fun newInstance(car: Car): CarDetailFragment {
            return CarDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CAR, car)
                }
            }
        }
    }
}