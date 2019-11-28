package com.example.congnitusfirma.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.congnitusfirma.R
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.activity_firma.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FirmaActivity : AppCompatActivity() {

    //se declara en companio object por ser constantes
    companion object{
        val TAG = "Permissos--"
        private const val  REQUEST_INTERNET = 200
    }
    private var locationManager : LocationManager? = null
    private val TAG = "Permisos"
    private val LOCATION_REQUEST = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firma)

        supportActionBar?.hide()
        //inicializar variable
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        val imgMenuFirma = findViewById<ImageView>(R.id.imgMenuFirma)
        val imgLocation = findViewById<ImageView>(R.id.imgLocatio)

        revisarPermisos()

        imgMenuFirma.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }

        //geolocalizacion
        imgLocation.setOnClickListener {
            revisarPermisoLocation()
        }


        //Evento al comenzar firma
        signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener{
            override fun onStartSigning() {
                /*cuando comienza a dibujar
                Toast.makeText(
                    applicationContext,
                    "Comineza firma",
                    Toast.LENGTH_SHORT
                ).show()*/
            }

            override fun onClear() {
                //deshabilita
                mSaveButton.isEnabled = false
                mClearButton.isEnabled = false
            }

            override fun onSigned() {
                //Al termianr la firnma
                mSaveButton.isEnabled = true
                mClearButton.isEnabled= true
            }
        })



        //guardar firma
        mSaveButton.setOnClickListener {
         val signatureBitmap:Bitmap = signaturePad.transparentSignatureBitmap
            if(addJPGSignatureToGalerry(signatureBitmap)){
                Toast.makeText(
                    this,
                    "Firma Guardada",
                    Toast.LENGTH_SHORT
                ).show()
                signaturePad.clear()
            }else{
                Toast.makeText(
                    this,
                    "No se puede almacenar la firma",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        mClearButton.setOnClickListener {
            signaturePad.clear()
            Log.d("TAG", "Limpiar evento")
        }


    }

    //Permisos location
    fun revisarPermisoLocation(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST
            )
            Log.i(TAG, "Pide permiso")
        }else{
            obtenerLocation()
        }
    }

    //Permisos
    fun revisarPermisos(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_INTERNET
            )
            Log.i(TAG, "Pide permiso")
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_INTERNET -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Si dio permiso")
                obtenerLocation()
            }else{
                Log.i(TAG, "No dio permiso")
                //Permisos necesarios
                revisarPermisos()
            }
        }
    }

    //Guardar en galeria
    fun addJPGSignatureToGalerry(signature: Bitmap):Boolean{
        var result = false
        try {
            val path = Environment.getExternalStorageDirectory().toString() + "/empleado"
            Log.d("Files", "path $path")
            val fileFirm = File(path)
            fileFirm.mkdir()
            val photo =
                File(fileFirm, "Firma.png")
            Log.d("Files", "path $photo")
            saveBitmapToPNG(signature, photo)
            result = true
        }catch (e:IOException){
            e.printStackTrace()
        }
        return result
    }

    @Throws(IOException::class)
   fun saveBitmapToPNG(bitmap: Bitmap, photo:File){
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(photo)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            try {
                out?.close()
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }

    //obtenerLocation
    fun obtenerLocation(){
        try {
            Toast.makeText(this, "Obteniendo....", Toast.LENGTH_SHORT).show()
            //Localizacion update
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        }catch (ex:SecurityException){
            Log.i(TAG, "Security Exception, on location availble")
        }
    }

    private val locationListener:LocationListener= object :LocationListener{
        override fun onLocationChanged(location: Location) {
            tVLocation.text= "${location.longitude}   :  ${location.longitude}"
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderDisabled(provider: String?) {}
        override fun onProviderEnabled(provider: String?) {}
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }
}
