package com.salva.cobadatabase

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = ColorDatabase.getInstance(this)
        val executor = Executors.newSingleThreadExecutor() // Buat background thread

        executor.execute {
            val dbColorDao = db.colorDao()
            val colors = dbColorDao.getAll()

            // Cek apakah warna "Red" sudah ada di database berdasarkan hexColor
            val exists = colors.any { it.hexColor == "#ff0000" }

            if (!exists) {
                val colorRed = Color(hexColor = "#ff0000", name = "Red")
                dbColorDao.insert(colorRed)
            }

            // Cek apakah data masuk dengan menampilkan isi database di Log
            val updatedColors = dbColorDao.getAll()
            Log.d("Database", "Colors in DB: $updatedColors")
        }
    }
}
