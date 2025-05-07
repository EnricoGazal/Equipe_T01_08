package com.example.geowarning.Registro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.geowarning.R

class RegistroAdapter(
    private val registros: List<Registro>,
    private val onClick: (Registro) -> Unit
) : RecyclerView.Adapter<RegistroAdapter.RegistroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_registro, parent, false)
        return RegistroViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegistroViewHolder, position: Int) {
        val registro = registros[position]
        holder.bind(registro)
        holder.itemView.setOnClickListener { onClick(registro) }
    }

    override fun getItemCount(): Int = registros.size

    class RegistroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imagem: ImageView = view.findViewById(R.id.imageView)
        private val descricao: TextView = view.findViewById(R.id.textViewDescricao)

        fun bind(registro: Registro) {
            descricao.text = registro.descricao
            Glide.with(imagem.context).load(registro.imageUrl).into(imagem)
        }
    }
}