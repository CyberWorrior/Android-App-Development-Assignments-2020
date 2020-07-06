package com.dragon.camera_feature

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 1
    private val STORAGE_PERMISSION_CODE:Int = 1
    lateinit var btnPress:Button
    lateinit var imgImage:ImageView
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnPress = findViewById(R.id.btnPress)
        imgImage = findViewById(R.id.imgImageCamera)


        btnPress.setOnClickListener{

            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permisson Has Already been granted",Toast.LENGTH_SHORT).show()
            }else{
                requestStoragePermissions()
            }
            dispatchTakePictureIntent()
        }

    }


   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgImage.setImageBitmap(imageBitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }*/

    private fun requestStoragePermissions(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            val dialog = AlertDialog.Builder(this).setTitle("Permission Needed").setMessage("Requires External Storage Permission")
                .setPositiveButton("ok"){text,listener ->
                    ActivityCompat.requestPermissions(this,
                        Array<String?>(1){android.Manifest.permission.READ_EXTERNAL_STORAGE} ,STORAGE_PERMISSION_CODE )
                }
            dialog.setNegativeButton("cancel"){text,listener ->
            }
            dialog.create().show()
        }else{
            ActivityCompat.requestPermissions(this,
                Array<String?>(1){android.Manifest.permission.READ_EXTERNAL_STORAGE} ,STORAGE_PERMISSION_CODE )
        }
    }

    //funtion to save the photo to external file
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",",jpg",storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //invoke a event using Intent
    private fun dispatchTakePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile:File? = try{
                    createImageFile()
                }catch (e:IOException){
                    null
                }
                photoFile?.also{
                    val photoURI:Uri = FileProvider.getUriForFile(Objects.requireNonNull(applicationContext),BuildConfig.APPLICATION_ID+".provider",it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO)
                }
            }
        }
    }
}