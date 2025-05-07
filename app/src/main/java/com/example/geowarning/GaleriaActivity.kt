package com.example.geowarning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geowarning.Registro.Registro
import com.example.geowarning.Registro.RegistroAdapter

class GaleriaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RegistroAdapter
    private lateinit var registros: List<Registro>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galeria)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Exemplo fixo - você irá buscar do banco
        registros = listOf(
            Registro("Título 1", "https://via.placeholder.com/150", -23.5629, -46.6544),
            Registro("Título 2", "https://via.placeholder.com/150", -23.5677, -46.6488)
        )

        adapter = RegistroAdapter(registros) { registro ->
            val intent = Intent(this, DetalheRegistroActivity::class.java)
            intent.putExtra("lat", registro.latitude)
            intent.putExtra("lng", registro.longitude)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        val btnCadastrar = findViewById<Button>(R.id.btnCadastrarLocal)
        btnCadastrar.setOnClickListener {
            val intent = Intent(this, CadastroLocalizacaoActivity::class.java)
            startActivity(intent)
        }
    }
}