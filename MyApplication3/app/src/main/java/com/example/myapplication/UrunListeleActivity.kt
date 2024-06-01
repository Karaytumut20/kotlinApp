package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.urundetay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class UrunListeleActivity2 : AppCompatActivity() {
    private lateinit var categoryTextView: TextView
    private lateinit var logoImageView1: ImageView
    private lateinit var logoNameTextView1: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.urunlisteleme)

        val selectedCategoryId = intent.getStringExtra("selectedCategoryId")
        val selectedCategoryName = intent.getStringExtra("selectedCategoryName")

        categoryTextView = findViewById(R.id.categoryTextView)
        categoryTextView.text = selectedCategoryName

        logoImageView1 = findViewById(R.id.logoImageView1)
        logoNameTextView1 = findViewById(R.id.logoNameTextView1)

        selectedCategoryId?.let {
            fetchTariflerByCategory(it)
        }

        logoImageView1.setOnClickListener {
            val intent = Intent(this, urundetay::class.java)
            if (selectedCategoryId != null) {
                intent.putExtra("selectedCategoryId", selectedCategoryId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Kategori ID'si bulunamadı", Toast.LENGTH_SHORT).show()
            }
        }

        categoryTextView.setOnClickListener {
            val intent = Intent(this, urundetay::class.java)
            if (selectedCategoryId != null) {
                intent.putExtra("selectedCategoryId", selectedCategoryId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Kategori ID'si bulunamadı", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchTariflerByCategory(categoryId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://10.0.2.2:4000/tarif/kategori/$categoryId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000 // 10 saniye timeout
                connection.readTimeout = 10000 // 10 saniye timeout

                val responseCode = connection.responseCode
                Log.d("UrunListeleActivity2", "Response Code: $responseCode")
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    Log.d("UrunListeleActivity2", "Response: $response")

                    val jsonObject = JSONObject(response)
                    val productName = jsonObject.getString("adi")

                    withContext(Dispatchers.Main) {
                        logoNameTextView1.text = productName
                    }
                } else {
                    Log.e("UrunListeleActivity2", "Error: HTTP $responseCode")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@UrunListeleActivity2, "Veri alınamadı", Toast.LENGTH_SHORT).show()
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                Log.e("UrunListeleActivity2", "Error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UrunListeleActivity2, "Bağlantı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}