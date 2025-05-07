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
import com.example.geowarning.Location.LocationData
import com.example.geowarning.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CadastroLocalizacaoActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: LocationData? = null

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
        val locationData = LocationData(
            latitude = latitude,
            longitude = longitude,
            userId = "123"
        )

        val call = ApiClient.retrofit.sendLocation(locationData)

        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CadastroLocalizacaoActivity, "Localização enviada com sucesso!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@CadastroLocalizacaoActivity, "Erro ao enviar. Código: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CadastroLocalizacaoActivity, "Erro ao enviar: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
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
                currentLocation = LocationData(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    userId = "123"
                )
                val userLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                mMap.addMarker(MarkerOptions().position(userLatLng).title("Minha Localização"))
            }
        }
    }
}