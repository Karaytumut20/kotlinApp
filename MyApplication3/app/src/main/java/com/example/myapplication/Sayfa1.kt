package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class Sayfa1 : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bugunnepisirsem)

        val tarifEkleButton = findViewById<Button>(R.id.button)
        tarifEkleButton.setOnClickListener {
            val intent = Intent(this, TarifEkle::class.java)
            startActivity(intent)
        }


        val tarifOkuButton = findViewById<Button>(R.id.button2)
        tarifOkuButton.setOnClickListener {
            val intent = Intent(this, Tarifokuma::class.java)
            startActivity(intent)
        }

        val kategoriEkle = findViewById<Button>(R.id.kategoriEkleButton)
        kategoriEkle.setOnClickListener {
            val intent = Intent(this, KategoriEkle::class.java)
            startActivity(intent)
        }

    }
}
