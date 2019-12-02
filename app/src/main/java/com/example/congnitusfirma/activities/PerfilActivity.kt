package com.example.congnitusfirma.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.congnitusfirma.R
import com.example.congnitusfirma.dao.APIService
import com.example.congnitusfirma.model.ResponseUpdateImg
import kotlinx.android.synthetic.main.activity_perfil.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.*

class PerfilActivity : AppCompatActivity() {

    private val TAKE_PHOTO_REQUEST = 101
    private val PERMISSION_CODE = 1001
    private  val GALLERY = 1
    private val CAMERA = 2
    private val IMAGE_DIRECTORY = "/demosImg"
    private var imgP :String? = null

    private var mediaPath: String? = null
    private var postPath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        supportActionBar?.hide()

        //recupera id de sharedPreferences
        val sharedPreferences= getSharedPreferences("my_aplicacion_firma", Context.MODE_PRIVATE)
        val usrId = sharedPreferences.getString("usr_id", "")
        val usrNombre = sharedPreferences.getString("usr_name","")
        val usrEmail = sharedPreferences.getString("usr_email","")
        val usrApp = sharedPreferences.getString("usr_App","")
        val usrApm = sharedPreferences.getString("usr_Apm","")
        imgP = sharedPreferences.getString("usr_Img", "")

        Log.d("--->", usrId+" "+ imgP)

        val requesManager = Glide.with(this)
        val requesBuilder= requesManager.load("http://35.155.161.8:8080/WSExample/"+imgP)
        Log.d("TAG", "ImgURL "+ requesBuilder)
        requesBuilder.into(imgPerfil)



        val imgBakc = findViewById<ImageView>(R.id.imgBackPerfil)
        val nombre = findViewById<EditText>(R.id.eTNombrePerfil)
        val apellidoP= findViewById<EditText>(R.id.eTApellidoPaternoPerfil)
        val apellidoM = findViewById<EditText>(R.id.eTApellidoMaternoPerfil)
        val email = findViewById<EditText>(R.id.eTCorreoPerfil)
        val contrasenia = findViewById<EditText>(R.id.eTContraseniaPerfil)


        //Mostrar datos en editText
        nombre.setText(usrNombre)
        apellidoM.setText(usrApm)
        apellidoP.setText(usrApp)
        email.setText(usrEmail)


        //evento para camara
        imgCamara.setOnClickListener{
            showPictureDialg()
        }

        //evento regreso a menu
        imgBakc.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }

        //Evento actualizar
        btnActualizarP.setOnClickListener {
            doAsync {
                var photoFile: File? = null
                photoFile = File(postPath)  //contiene la ruta de la imagen postPath y la convierte en File
                val partes = ArrayList<MultipartBody.Part>()
                partes.add(MultipartBody.Part.createFormData("ApiCall", "editRegisterUsr"))
                partes.add(MultipartBody.Part.createFormData("archivo", photoFile?.name,
                        photoFile?.crearMultiparte()))
                partes.add(MultipartBody.Part.createFormData("nombre", nombre.text.toString()))
                partes.add(MultipartBody.Part.createFormData("apm", apellidoP.text.toString()))
                partes.add(MultipartBody.Part.createFormData("app", apellidoM.text.toString()))
                partes.add(MultipartBody.Part.createFormData("email", email.text.toString()))
                partes.add(MultipartBody.Part.createFormData("nip", contrasenia.text.toString()))
                partes.add(MultipartBody.Part.createFormData("usrid", contrasenia.text.toString()))

                val call = updateRetrofit().create(APIService::class.java).updateUserImg(partes)?.execute()
                val respuesta = call.body() as ResponseUpdateImg
                Log.i("TAG", "Response update "+ respuesta.mensaje)
                uiThread {
                    if(respuesta.valido == "1"){

                        val usrImg = respuesta.update.usrImg
                        val sharedPreferences= getSharedPreferences("my_aplicacion_firma", Context.MODE_PRIVATE)
                        var editor = sharedPreferences.edit()
                        editor.putString("usr_Img", usrImg)
                        editor.commit()

                        val alerDialog = AlertDialogBuilder(this@PerfilActivity)
                        alerDialog.title("Alerta")
                        alerDialog.message(""+respuesta.mensaje)
                        alerDialog.yesButton { "Si"
                            nombre.setText("")
                            email.setText("")
                            contrasenia.setText("")
                            apellidoM.setText("")
                            apellidoP.setText("")
                        }
                        alerDialog.show()
                    }else if(respuesta.valido == "0"){
                        val alerDialog = AlertDialogBuilder(this@PerfilActivity)
                        alerDialog.title("Alert")
                        alerDialog.message(""+respuesta.mensaje)
                        alerDialog.yesButton { "Si"
                        }
                        alerDialog.show()
                    }
                }
            }
        }


    }

    fun File.crearMultiparte(): RequestBody {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(this).toString())
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        Log.d("ImagenUtil", "uri: " + Uri.fromFile(this))
        Log.d("ImagenUtil", "type: " + type!!)
        return RequestBody.create(
                MediaType.parse("image/*"), this
        )
    }

    //dialogo para galeria y camara
    private  fun showPictureDialg(){
        val pictureDoalog = AlertDialog.Builder(this)
        pictureDoalog.setTitle("Seleccione opcion para cambiar imagen de perfil")
        val dialogItem = arrayOf("Acceder a galeria", "Acceder a camara")
        pictureDoalog.setItems(
                dialogItem
        ) { dialog, which ->
            when (which) {
                0 -> permisoGaleria()
                1 -> permisoCamara()
            }
        }
        pictureDoalog.show()
    }

    fun permisoGaleria(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                //permission requerido
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                //permission otorgados
                choosePhotoFromGallary()
            }
        }
        else{
            //system OS  < Marshmallow
            choosePhotoFromGallary()
        }
    }

    fun choosePhotoFromGallary(){
        val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
       startActivityForResult(galleryIntent, GALLERY)
    }

    fun permisoCamara(){
        revisaPermiso()
    }

    fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY){
            if (data != null){
                val contentURI = data!!.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                try {
                    val cursor = contentResolver.query(contentURI!!, filePathColumn, null, null, null)
                    assert(cursor != null)
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    // Set the Image in ImageView for Previewing the Media
                    imgPerfil.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()
                    cursor.close()
                    //postPath contiene la ruta de la imagen
                    postPath = mediaPath
                }catch (e:IOException){
                 e.printStackTrace()
                    Toast.makeText(this@PerfilActivity, "Fallo", Toast.LENGTH_SHORT).show()
                }
            }
        }else if( requestCode == CAMERA){
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            imgPerfil.setImageBitmap(thumbnail)
            saveImage(thumbnail)
        }
    }

    fun revisaPermiso(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    TAKE_PHOTO_REQUEST
            )
            Log.i("Permisos", "Pide Permiso")
        }else{
            //ya tinene permisos
            takePhotoFromCamera()
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            TAKE_PHOTO_REQUEST -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoFromCamera()
            }else{
                Log.i("TAG", "No dio permiso")
            }
        }
    }


    fun saveImage(myButmap:Bitmap):String{
        val bytes = ByteArrayOutputStream()
        myButmap.compress(Bitmap.CompressFormat.JPEG, 90 , bytes)
        val wallpaperDirectory = File((Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        )

        //buil the directory structure
        Log.d("file", wallpaperDirectory.toString())
        if(!wallpaperDirectory.exists()){
            wallpaperDirectory.mkdir()
        }
        try {
            val f = File(
                    wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                    this,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null
            )
            fo.close()
            postPath = f.getAbsolutePath()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())
        }catch (e:IOException){
            e.printStackTrace()
        }
        return  ""
    }


    private fun updateRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://35.155.161.8:8080/WSExample/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun showErrorDialog() {
        alert("Ha ocurrido un error, al actualizar intente de nuevo.") {
            yesButton { }
        }.show()
    }



}




/*Evento actualizar  sin foto
btnActualizarP.setOnClickListener {
    doAsync {
        val call = updateRetrofit().create(APIService::class.java).updateUser(
                ""+usrId,""+correo.text.toString(), ""+contrasenia.text.toString(),
                ""+nombre.text.toString(), ""+apellidoP.text.toString(), ""+apellidoM.text.toString()).execute()
        val respuesta = call.body() as ResponseUpdate
        Log.i("TAG", "Response update "+ respuesta.mensaje)
        /* response de actualizar
        {
            "valido": "1",
            "mensaje": "Actualizado correctamente"
        }
        * */
        uiThread{
            if(respuesta.valido == "1"){
                val alerDialog = AlertDialogBuilder(this@PerfilActivity)
                alerDialog.title("Alerta")
                alerDialog.message(""+respuesta.mensaje)
                alerDialog.yesButton { "Si"
                    nombre.setText("")
                    correo.setText("")
                    contrasenia.setText("")
                    apellidoM.setText("")
                    apellidoP.setText("")
                }
                alerDialog.show()

            }else if(respuesta.valido == "0"){
                val alerDialog = AlertDialogBuilder(this@PerfilActivity)
                alerDialog.title("Alert")
                alerDialog.message(""+respuesta.mensaje)
                alerDialog.yesButton { "Si"
                }
                alerDialog.show()

            }
        }

    }
}*/