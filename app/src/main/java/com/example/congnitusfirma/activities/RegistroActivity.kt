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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.example.congnitusfirma.R
import com.example.congnitusfirma.dao.APIService
import com.example.congnitusfirma.model.ResponseRegistro
import com.example.congnitusfirma.model.ServerResponse
import com.example.congnitusfirma.model.UsuariosResponse
import com.example.congnitusfirma.utilities.Utils
import kotlinx.android.synthetic.main.activity_perfil.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class RegistroActivity : AppCompatActivity() {

    private val TAKE_PHOTO_REQUEST = 101
    private val PERMISSION_CODE = 1001
    private  val GALLERY = 1
    private val CAMERA = 2
    private val IMAGE_DIRECTORY = "/demonuts"
    private var mediaPath: String? = null
    private var postPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        supportActionBar?.hide()

        val imgMenu = findViewById<ImageView>(R.id.imgMenuRegistro)
        val tvSesion = findViewById<TextView>(R.id.tVSesion)
        val email = findViewById<EditText>(R.id.eTCorreoRegistro)
        val nombre = findViewById<EditText>(R.id.eTNombreRegistro)
        val apellidoP = findViewById<EditText>(R.id.eTApellidoPaternoRegistro)
        val apellidoM = findViewById<EditText>(R.id.eTApellidoMaternoRegistro)
        val contrasenia = findViewById<EditText>(R.id.eTContraseniaRegistro)
        val contraseniaRepetir = findViewById<EditText>(R.id.eTContraseniaRegistroVerificar)
        val btnResitro = findViewById<Button>(R.id.btnRegistro)


        //evento menu
        imgMenu.setOnClickListener {
            startActivity(Intent(this@RegistroActivity, MainActivity::class.java))
            finish()
        }

        //evento menu
        tvSesion.setOnClickListener {
            startActivity(Intent(this@RegistroActivity, MainActivity::class.java))
            finish()
        }

        //evento para camara
        imgPerfil.setOnClickListener {
            showPictureDialg()
        }

        //Registro de usuario con Imagen
        btnResitro.setOnClickListener {
            if (email.text.isEmpty() || nombre.text.isEmpty() || apellidoP.text.isEmpty() ||
                    apellidoM.text.isEmpty() || contrasenia.text.isEmpty() || contraseniaRepetir.text.isEmpty()){
                Toast.makeText(
                        this,
                        "Verifique que los campos no esten vacios",
                        Toast.LENGTH_SHORT
                ).show()
            }else{
                if (Utils.isEmailValid(""+ email.text)){
                    Log.d("TAG","Evento 1")
                    if(contrasenia.text.toString().equals(contraseniaRepetir.text.toString())){
                        if (postPath == null || postPath == "") {
                            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show()
                        } else {
                            doAsync {
                                var photoFile: File? = null
                                photoFile = File(postPath)  //se optiene la ruta de la imagen postPath y la convierte en File
                                val partes = ArrayList<MultipartBody.Part>()
                                partes.add(MultipartBody.Part.createFormData("ApiCall", "setRegisterUsr"))
                                partes.add(MultipartBody.Part.createFormData("email", email.text.toString()))
                                partes.add(MultipartBody.Part.createFormData("nombre", nombre.text.toString()))
                                partes.add(MultipartBody.Part.createFormData("app", apellidoP.text.toString()))
                                partes.add(MultipartBody.Part.createFormData("apm", apellidoM.text.toString()))
                                partes.add(MultipartBody.Part.createFormData("nip", contrasenia.text.toString()))
                                partes.add(MultipartBody.Part.createFormData("archivo", photoFile?.name,
                                        photoFile?.crearMultiparte()))

                                val call = getRetrofit().create(APIService::class.java).registroPerfil(partes)?.execute()
                                val respuesta = call?.body() as ServerResponse
                                Log.i("Resp", "${respuesta.valido} ")
                                Log.i("Resp", "${respuesta.user.ruta} ")
                                uiThread{
                                    if(respuesta.valido == "1"){
                                        val usrIds = respuesta.user.id
                                        val usrNombre = respuesta.user.nombre
                                        val usrEmail = respuesta.user.email
                                        val usrApp = respuesta.user.app
                                        val usrApm = respuesta.user.apm
                                        val usrImg = respuesta.user.ruta

                                        val ms = respuesta.mensaje

                                        Log.d("UserPreferenses ", usrIds+ " - "+ usrNombre+ " "+ usrApp+ " "+usrApm+" "+usrEmail )

                                        val sharedPreferences= getSharedPreferences("my_aplicacion_firma", Context.MODE_PRIVATE)
                                        var editor = sharedPreferences.edit()
                                        editor.putString("usr_id", usrIds)
                                        editor.putString("usr_name", usrNombre)
                                        editor.putString("usr_email", usrEmail)
                                        editor.putString("usr_App", usrApp)
                                        editor.putString("usr_Apm", usrApm)
                                        editor.putString("usr_Img", usrImg)
                                        editor.commit()

                                        val intent = Intent(this@RegistroActivity, MenuActivity::class.java)
                                        intent.putExtra("usr_id", usrIds)
                                        intent.putExtra("usr_nombre", usrNombre)
                                        intent.putExtra("MS", ms)
                                        startActivity(intent)
                                    }else{
                                        showErrorDialog()
                                    }
                                }
                            }
                        }
                    }else{
                        Toast.makeText(
                                this,
                                "Las contraseñas  son incorrectas",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    Toast.makeText(
                            this,
                            "El correo no cumple con el formato correcto",
                            Toast.LENGTH_SHORT
                    ).show()
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
                }catch (e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this@RegistroActivity, "Fallo", Toast.LENGTH_SHORT).show()
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


    fun saveImage(myButmap: Bitmap):String{
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
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())
        }catch (e: IOException){
            e.printStackTrace()
        }
        return  ""
    }



    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://35.155.161.8:8080/WSExample/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun showErrorDialog(){
        alert ( "Ha ocurrido un error, al registrarse inténtelo de nuevo." ){
            yesButton { "Si" }
        }.show()
    }

}


/* response de registro
*{
    "valido": "1",
    "mensaje": "Bienvenido",
    "registro": {
        "usr_email": "miguel@hola.com.mx",
        "usr_id": "11",
        "usr_apm": "ramirez",
        "usr_nombre": "miguel",
        "usr_app": "ramirez"
    }
}
*
**/




//RegistroUsuario sin imagen
/*btnResitro.setOnClickListener {
    if (email.text.isEmpty() || nombre.text.isEmpty() || apellidoP.text.isEmpty() ||
        apellidoM.text.isEmpty() || contrasenia.text.isEmpty() || contraseniaRepetir.text.isEmpty()){
        Toast.makeText(
            this,
            "Verifique que los campos no esten vacios",
            Toast.LENGTH_SHORT
        ).show()
    }else{
      if (Utils.isEmailValid(""+ email.text)){
          Log.d("TAG","Evento 1")
          if(contrasenia.text.toString().equals(contraseniaRepetir.text.toString())){
              doAsync {
                  val call = getRetrofit().create(APIService::class.java).registroUser(email.text.toString(),nombre.text.toString(),
                      apellidoP.text.toString(),apellidoM.text.toString(),contrasenia.text.toString()).execute()
                  val respuesta = call.body() as ResponseRegistro
                  //el ejemplo de la respuesta esta comentado abajo
                  Log.i("Valor", "respuesta--- "+respuesta.valido)
                  uiThread{
                      if(respuesta.valido == "1"){
                          val usrIds = respuesta.registroU.usrid
                          val usrNombre = respuesta.registroU.usrNombre
                          val usrEmail = respuesta.registroU.usrEmail
                          val usrApp = respuesta.registroU.usrApp
                          val usrApm = respuesta.registroU.usrApm
                          val usrImg = respuesta.registroU.usrImg

                          val ms = respuesta.mensaje

                          Log.d("UserPreferenses ", usrIds+ " - "+ usrNombre+ " "+ usrApp+ " "+usrApm+" "+usrEmail )

                          val sharedPreferences= getSharedPreferences("my_aplicacion_firma", Context.MODE_PRIVATE)
                          var editor = sharedPreferences.edit()
                          editor.putString("usr_id", usrIds)
                          editor.putString("usr_name", usrNombre)
                          editor.putString("usr_email", usrEmail)
                          editor.putString("usr_App", usrApp)
                          editor.putString("usr_Apm", usrApm)
                          editor.putString("usr_Img", usrImg)
                          editor.commit()

                          val intent = Intent(this@RegistroActivity, MenuActivity::class.java)
                          intent.putExtra("usr_id", usrIds)
                          intent.putExtra("usr_nombre", usrNombre)
                          intent.putExtra("MS", ms)
                          startActivity(intent)
                      }else{
                          showErrorDialog()
                      }
                  }
              }
          }else{
              Toast.makeText(
                  this,
                  "Las contraseñas  son incorrectas",
                  Toast.LENGTH_SHORT
              ).show()
          }
      }else{
          Toast.makeText(
              this,
              "El correo no cumple con el formato correcto",
              Toast.LENGTH_SHORT
          ).show()
      }
    }
}
*/