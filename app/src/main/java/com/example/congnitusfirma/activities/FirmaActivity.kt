package com.example.congnitusfirma.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Bitmap
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firma)

        supportActionBar?.hide()

        revisarPermisos()
        val imgMenuFirma = findViewById<ImageView>(R.id.imgMenuFirma)

        imgMenuFirma.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
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

    override fun onBackPressed() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }
}
