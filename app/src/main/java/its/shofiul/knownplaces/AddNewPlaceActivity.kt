package its.shofiul.knownplaces

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_add_new_place.*
import java.text.SimpleDateFormat
import java.util.*

class AddNewPlaceActivity : AppCompatActivity(), View.OnClickListener {

    private var calender = Calendar.getInstance()
    private lateinit var dateSetLister: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_place)

        setSupportActionBar(toolbar_add_places)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_add_places.setNavigationOnClickListener {
            onBackPressed()
        }

        dateSetLister = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calender.set(Calendar.YEAR, year)
            calender.set(Calendar.MONTH, month)
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        et_date.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.et_date -> {
                DatePickerDialog(
                    this@AddNewPlaceActivity,
                    dateSetLister,
                    calender.get(Calendar.YEAR),
                    calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd.MM.yyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        et_date.setText((sdf.format(calender.time).toString()))
    }

}