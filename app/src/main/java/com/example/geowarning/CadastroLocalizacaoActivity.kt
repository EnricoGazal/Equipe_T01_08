package com.example.geowarning

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class CadastroLocalizacaoActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_localizacao)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val btnEnviar = findViewById<Button>(R.id.btnEnviarLocalizacao)
        btnEnviar.setOnClickListener {
            currentLocation?.let {
                val lat = it.latitude
                val lng = it.longitude
                Toast.makeText(this, "Lat: $lat, Lng: $lng", Toast.LENGTH_LONG).show()
                // Enviar localização para a API
                enviarLocalizacao(lat, lng)
            } ?: run {
                Toast.makeText(this, "Localização ainda não disponível", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para enviar a localização para a API
    private fun enviarLocalizacao(latitude: Double, longitude: Double) {
        Thread {
            try {
                val json = """
                    {
                        "latitude": $latitude,
                        "longitude": $longitude
                    }
                """.trimIndent()

                val url = URL("http://10.0.2.2:3000/api/localizacao")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                // Enviar o corpo da requisição
                val output = conn.outputStream
                val writer = OutputStreamWriter(output, "UTF-8")
                writer.write(json)
                writer.flush()
                writer.close()

                // Verificar o código de resposta da API
                val responseCode = conn.responseCode
                runOnUiThread {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(this, "Localização enviada com sucesso!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Erro ao enviar localização. Código: $responseCode", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Erro ao enviar localização", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Verificar permissões de localização
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        mMap.isMyLocationEnabled = true

        // Obter a última localização conhecida do usuário
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val userLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                mMap.addMarker(MarkerOptions().position(userLatLng).title("Minha Localização"))
            }
        }
    }
}