package com.seuprojeto.exemplo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class EnvioActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_captura_envio)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA),
            100
        )

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturarLocalizacao()
            } else {
                Toast.makeText(this, "Permissões negadas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun capturarLocalizacao() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    enviarLocalizacaoParaBackend()
                } else {
                    Toast.makeText(this, "Não conseguiu pegar localização", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun enviarLocalizacaoParaBackend() {
        val json = JSONObject()
        json.put("latitude", latitude)
        json.put("longitude", longitude)
        json.put("timestamp", System.currentTimeMillis())

        val queue = Volley.newRequestQueue(this)
        val url = "https://suaapi.com/uploadLocation"

        val request = JsonObjectRequest(
            com.android.volley.Request.Method.POST, url, json,
            { response ->
                abrirCamera()
            },
            { error ->
                Toast.makeText(this, "Erro ao enviar localização", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }

    private fun abrirCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                enviarImagemParaBackend(imageBitmap)
            }
        }
    }

    private fun enviarImagemParaBackend(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        val json = JSONObject()
        json.put("image", encodedImage)
        json.put("timestamp", System.currentTimeMillis())

        val queue = Volley.newRequestQueue(this)
        val url = "https://suaapi.com/uploadImage"

        val request = JsonObjectRequest(
            com.android.volley.Request.Method.POST, url, json,
            { response ->
                abrirGaleriaActivity()
            },
            { error ->
                Toast.makeText(this, "Erro ao enviar imagem", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }

    private fun abrirGaleriaActivity() {
        val intent = Intent(this, GaleriaActivity::class.java)
        startActivity(intent)
        finish()
    }
}
