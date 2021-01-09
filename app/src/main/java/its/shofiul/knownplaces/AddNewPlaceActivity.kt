package its.shofiul.knownplaces

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_new_place.*
import java.text.SimpleDateFormat
import java.util.*

class AddNewPlaceActivity : AppCompatActivity(), View.OnClickListener {
    private final val TAG = "AddNewPlaceActivity"

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
        tv_add_image.setOnClickListener(this)
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

            R.id.tv_add_image -> {
                Log.d(TAG, "onClick: AddImage")
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItem = arrayOf(
                    "Select photo from Gallery",
                    "Capture photo from camera"
                )
                pictureDialog.setItems(pictureDialogItem) {
                        dialog, which ->
                    when (which) {
                        0 -> chosePhotoFromGallery()
                        1 -> Toast.makeText(
                            this@AddNewPlaceActivity,
                                "Camera selection coming soon...",
                                Toast.LENGTH_LONG
                            ).show()
                    }
                }

                pictureDialog.show()
            }
        }
    }

    private fun chosePhotoFromGallery() {
        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if(report!!.areAllPermissionsGranted()){
                    Toast.makeText(this@AddNewPlaceActivity,"Storage permission granted",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>,
                token: PermissionToken
            ) {
                showRationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    fun showRationalDialogForPermission(){
        AlertDialog.Builder(this).setMessage("Its look like you have turned of permission for this features")
            .setPositiveButton("GO TO SETTINGS"){
                _,_ -> try{
                    val intent = Intent (Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",packageName,null)
                intent.data = uri
                startActivity(intent)
                }catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                Log.d(TAG, "showRationalDialogForPermission: ")
                }
            }.setNegativeButton("Cancel"){
                dialog,whic ->
                dialog.dismiss()
            }.show()

    }

    private fun updateDateInView() {
        val myFormat = "dd.MM.yyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        et_date.setText((sdf.format(calender.time).toString()))
    }

}