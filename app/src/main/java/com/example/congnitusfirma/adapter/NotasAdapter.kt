package com.example.congnitusfirma.adapter

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.congnitusfirma.R
import com.example.congnitusfirma.entity.NotasEntity


class NotasAdapter( private val tasks: List<NotasEntity>, private val checkTask: (NotasEntity) -> Unit,
                    private val deleteTask: (NotasEntity) -> Unit) : RecyclerView.Adapter<NotasAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_notas, parent, false))
    }
    override fun getItemCount(): Int {
        return tasks.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tasks[position]
        holder.bind(item, checkTask, deleteTask)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTask = view.findViewById<TextView>(R.id.tvTask)
        private val cbIsDone = view.findViewById<CheckBox>(R.id.cbIsDone)
        private val iVDelete = view.findViewById<ImageView>(R.id.iVDelete)
        private val fecha = view.findViewById<TextView>(R.id.tvFecha)

        /*va por toda la identidad
        */

        fun bind(
            task: NotasEntity,
            checkTask: (NotasEntity) -> Unit,
            deleteTask: (NotasEntity) -> Unit
        ) {
            tvTask.text = task.name
            cbIsDone.isChecked = task.isDone
            fecha.text = task.fecha
            cbIsDone.setOnClickListener { checkTask(task) }
            iVDelete.setOnClickListener {
                deleteTask(task)
            }

        }
    }
}