package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class urundetay : AppCompatActivity() {
    private lateinit var productImageView: ImageView
    private lateinit var productTitleTextView: TextView
    private lateinit var prepTimeTitleTextView: TextView
    private lateinit var prepTimeTextView: TextView
    private lateinit var ingredientsTitleTextView: TextView
    private lateinit var ingredientsTextView: TextView
    private lateinit var preparationTitleTextView: TextView
    private lateinit var preparationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.urundetay)

        val selectedCategoryId = intent.getStringExtra("selectedCategoryId")

        productImageView = findViewById(R.id.productImageView)
        productTitleTextView = findViewById(R.id.productTitleTextView)
        prepTimeTitleTextView = findViewById(R.id.prepTimeTitleTextView)
        prepTimeTextView = findViewById(R.id.prepTimeTextView)
        ingredientsTitleTextView = findViewById(R.id.ingredientsTitleTextView)
        ingredientsTextView = findViewById(R.id.ingredientsTextView)
        preparationTitleTextView = findViewById(R.id.preparationTitleTextView)
        preparationTextView = findViewById(R.id.preparationTextView)

        selectedCategoryId?.let {
            fetchTariflerByCategory(it)
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
                Log.d("urundetay", "Response Code: $responseCode")
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    Log.d("urundetay", "Response: $response")

                    val jsonObject = JSONObject(response)
                    val productName = jsonObject.getString("adi")
                    val prepTime = jsonObject.getString("hazirlanma_zamani")
                    val ingredients = jsonObject.getString("malzemeler")
                    val preparation = jsonObject.getString("hazirlanisi")

                    withContext(Dispatchers.Main) {
                        productTitleTextView.text = productName
                        prepTimeTextView.text = prepTime
                        ingredientsTextView.text = ingredients
                        preparationTextView.text = preparation
                    }
                } else {
                    Log.e("urundetay", "Error: HTTP $responseCode")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@urundetay, "Veri alınamadı", Toast.LENGTH_SHORT).show()
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                Log.e("urundetay", "Error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@urundetay, "Bağlantı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
