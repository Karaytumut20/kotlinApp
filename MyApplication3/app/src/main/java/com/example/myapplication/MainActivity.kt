package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // POST isteği oluşturma
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val serverUrl = "http://10.0.2.2:4000/kullanici/login" // Sunucu adresini buraya yazın
                    val url = URL(serverUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true

                    val postData = JSONObject().apply {
                        put("email", username)
                        put("sifre", password)
                    }

                    val outputStreamWriter = OutputStreamWriter(connection.outputStream)
                    outputStreamWriter.write(postData.toString())
                    outputStreamWriter.flush()

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Başarılı giriş
                        val intent = Intent(this@MainActivity, Sayfa1::class.java)
                        startActivity(intent)
                    } else {
                        // Hatalı giriş
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show()
                        }
                    }

                    connection.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Bağlantı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        registerButton.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
