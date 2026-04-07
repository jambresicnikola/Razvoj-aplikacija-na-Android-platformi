package hr.tvz.android.kalkulatorjambresic

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.tvz.android.kalkulatorjambresic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setButton()
        setSpinner()
    }

    fun setButton() {
        binding.calculateBMIButton.setOnClickListener { calculateBMI() }
    }

    fun calculateBMI() {
        val heightString = binding.heightEditText.text.toString()
        val weightString = binding.weightEditText.text.toString()

        var height = heightString.toDoubleOrNull()
        val weight = weightString.toDoubleOrNull()

        if (height == null || weight == null || height <= 0 || weight <= 0) {
            binding.errorMessageTextView.text = getString(R.string.inputError)
            binding.bmiResultValueTextView.text = "-"
            binding.categoryValueTextView.text = "-"
            return
        }

        binding.errorMessageTextView.text = ""

        height /= 100

        val bmi = weight / (height * height)

        binding.bmiResultValueTextView.text = String.format("%.1f", bmi)

        val category = when {
            bmi > 30 -> getString(R.string.obeseBMI)
            bmi > 25 -> getString(R.string.overweightBMI)
            bmi > 18.5 -> getString(R.string.normalBMI)
            else -> getString(R.string.tooSkinnyBMI)
        }

        binding.categoryValueTextView.text = category
    }

    fun setSpinner() {
        binding.themesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                applyTheme(p2 == 1)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    fun applyTheme(isDark: Boolean) {
        val backgroundColor = if (isDark) getColor(R.color.dark_background)
            else getColor(R.color.light_background)
        binding.rootLayout.setBackgroundColor(backgroundColor)

        val textPrimaryColor = if (isDark) getColor(R.color.dark_primary)
            else getColor(R.color.light_primary)
        binding.appNameTextView.setTextColor(textPrimaryColor)
        binding.bmiResultValueTextView.setTextColor(textPrimaryColor)
        binding.categoryValueTextView.setTextColor(textPrimaryColor)
        binding.calculateBMIButton.setBackgroundColor(textPrimaryColor)

        val textColor = if (isDark) getColor(R.color.dark_text)
            else getColor(R.color.light_text)
        binding.heightTextView.setTextColor(textColor)
        binding.weightTextView.setTextColor(textColor)
        binding.resultTextView.setTextColor(textColor)
        binding.categoryTextView.setTextColor(textColor)
        binding.heightEditText.setTextColor(textColor)
        binding.weightEditText.setTextColor(textColor)

        val textSecondaryColor = if (isDark) getColor(R.color.dark_text_secondary)
            else getColor(R.color.light_text_secondary)
        binding.themeTextView.setTextColor(textSecondaryColor)

        binding.tableLayout.background = if (isDark)
            getDrawable(R.drawable.table_background_dark)
        else
            getDrawable(R.drawable.table_background_light)

        val hintColor = if (isDark) getColor(R.color.dark_text_secondary)
            else getColor(R.color.light_text_secondary)
        binding.heightEditText.setHintTextColor(hintColor)
        binding.weightEditText.setHintTextColor(hintColor)
    }
}