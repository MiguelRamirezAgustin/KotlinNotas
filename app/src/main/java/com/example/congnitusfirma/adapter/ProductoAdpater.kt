package com.example.congnitusfirma.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.congnitusfirma.R
import com.example.congnitusfirma.activities.DetalleProductoActivity
import com.example.congnitusfirma.activities.ProductosActivity
import com.example.congnitusfirma.model.ProductosResponse
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.item_producto.view.*

class ProductoAdpater (private val context: ProductosActivity, private val productoList:List<ProductosResponse>) :
    RecyclerView.Adapter<ProductoAdpater.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false))
    }

    override fun getItemCount(): Int {
        return productoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productoModel =productoList.get(position)
        holder.tituloProducto?.text = productoModel.prodTit
        holder.precioProducto?.text = "$ " +productoModel.prodPre+ " MXN"
        val requesManager=  Glide.with(context)
        val imgUrl = productoModel.prodImg
        val requesBuilder = requesManager.load("http://35.155.161.8:8080/WSExample/$imgUrl")
        requesBuilder.into(holder.fotoProducto)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetalleProductoActivity::class.java)
            intent.putExtra("titleProducto", ""+productoModel.prodTit)
            intent.putExtra("img_url", ""+productoModel.prodImg)
            intent.putExtra("detalle_Producto", ""+productoModel.prodDes)
            intent.putExtra("detalle_precio", ""+productoModel.prodPre)
            intent.putExtra("id_producto", ""+productoModel.prodId)
            context.startActivity(intent)
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloProducto = view.tvTituloProducto
        val precioProducto = view.tvPrecioProducto
        val fotoProducto = view.ivFotoProducto
    }
}
