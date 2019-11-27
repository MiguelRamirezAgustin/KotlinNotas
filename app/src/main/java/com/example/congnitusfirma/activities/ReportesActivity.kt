package com.example.congnitusfirma.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.congnitusfirma.adapter.NotasAdapter
import com.example.congnitusfirma.entity.NotasEntity
import kotlinx.android.synthetic.main.activity_reportes.*
import kotlinx.android.synthetic.main.item_notas.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReportesActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NotasAdapter
    lateinit var tasks: MutableList<NotasEntity>

    val CERO:String=  "0"
    val BARRA:String= "/"
    var cal = Calendar.getInstance()
    var mes = cal.get(Calendar.MONTH)
    var dia = cal.get(Calendar.DAY_OF_MONTH)
    var anio = cal.get(Calendar.YEAR)

    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.congnitusfirma.R.layout.activity_reportes)

        supportActionBar?.hide()

       // val textView = findViewById<TextView>(R.id.etFecha)
        val textView: TextView  = findViewById(com.example.congnitusfirma.R.id.etFecha)


        textView.setOnClickListener {
            val recogerFecha = DatePickerDialog(
                this@ReportesActivity,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                    val mesActual = month + 1
                    //Formateo el día obtenido: antepone el 0 si son menores de 10
                    val diaFormateado =
                        if (dayOfMonth < 10) CERO + dayOfMonth.toString() else dayOfMonth.toString()
                    //Formateo el mes obtenido: antepone el 0 si son menores de 10
                    val mesFormateado =
                        if (mesActual < 10) CERO + mesActual.toString() else mesActual.toString()
                    //Muestro la fecha con el formato deseado
                    etFecha.text = diaFormateado + BARRA + mesFormateado + BARRA + year
                    Log.d("Fecha: ", etFecha.text.toString())

                },
                //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                anio, mes, dia
            )
            //Muestro el widget
            recogerFecha.show()
        }


        tasks = ArrayList()
        getTasks()
        //agregar una tarea
        btnAddTask.setOnClickListener {
            addTask(NotasEntity(name = etTask.text.toString(), fecha =  textView.text.toString()  ))
        }
    }



    //limpi el focus
    fun clearFocus(){
        etTask.setText("")
    }

    fun Context.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    fun getTasks() {
        doAsync {
            tasks = MisNotasApp.database.notaskDao().getAllTasks()
            uiThread {
                setUpRecyclerView(tasks)
            }
        }
    }



    //proceso asincrono
    fun addTask(task:NotasEntity){
        doAsync {
            val id = MisNotasApp.database.notaskDao().addTask(task)
            val recoveryTask = MisNotasApp.database.notaskDao().getTaskById(id)
            Log.d("Agregar", recoveryTask.toString())
            uiThread {

                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val strDate: Date = sdf.parse(task.fecha)
                var before = false
                for (i in 0 until tasks.size){
                    val dateComparate = tasks.get(i).fecha
                    val strDatel : Date = sdf.parse(dateComparate)
                    if(strDate.before(strDatel)){
                        tasks.add(i,recoveryTask)
                        adapter.notifyItemChanged(i)
                        before= true
                        break
                    }
                }
                if (!before){
                    tasks.add(recoveryTask)
                    adapter.notifyItemChanged(tasks.size)
                    hideKeyboard()   //oculta el teclado
                }
            }
        }
    }

    fun updateTask(task: NotasEntity) {
        doAsync {
            task.isDone = !task.isDone
            MisNotasApp.database.notaskDao().updateTask(task)
        }
    }

    fun deleteTask(task: NotasEntity){
        doAsync {
            val position = tasks.indexOf(task)
            MisNotasApp.database.notaskDao().deleteTask(task)  //linea de coneccion
            tasks.remove(task)
            uiThread {
                //                toast("delete ${tasks[position].name}")
                adapter.notifyItemRemoved(position)
            }
        }
    }

    fun setUpRecyclerView(tasks: List<NotasEntity>) {
        adapter = NotasAdapter(tasks, { updateTask(it) }, {deleteTask(it)})
        recyclerView = findViewById(com.example.congnitusfirma.R.id.rvNotas)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}



