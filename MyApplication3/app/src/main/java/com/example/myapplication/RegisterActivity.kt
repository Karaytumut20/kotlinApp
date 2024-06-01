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

class RegisterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val registerButton = findViewById<Button>(R.id.loginButton)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // POST isteği oluşturma
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val serverUrl = "http://10.0.2.2:4000/kullanici/register" // Sunucu adresini buraya yazın
                    val url = URL(serverUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true

                    val postData = JSONObject().apply {
                        put("kullanici_adi", username)
                        put("email", email)
                        put("sifre", password)
                    }

                    val outputStreamWriter = OutputStreamWriter(connection.outputStream)
                    outputStreamWriter.write(postData.toString())
                    outputStreamWriter.flush()

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Başarılı kayıt
                        runOnUiThread {
                            Toast.makeText(this@RegisterActivity, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                            // MainActivity'ye geçiş yap
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Bu activity'i kapat
                        }
                    } else {
                        // Hatalı kayıt
                        runOnUiThread {
                            Toast.makeText(this@RegisterActivity, "Kayıt oluşturulamadı", Toast.LENGTH_SHORT).show()
                        }
                    }

                    connection.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Bağlantı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
