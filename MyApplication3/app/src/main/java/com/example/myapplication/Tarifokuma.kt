package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

data class Category(val id: String, val name: String) {
    override fun toString(): String {
        return "$id - $name"
    }
}

class Tarifokuma : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var categories: ArrayList<Category>
    private lateinit var adapter: ArrayAdapter<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tarifoku)

        // Spinner'ı tanımla
        categorySpinner = findViewById(R.id.categorySpinner)
        categorySpinner.visibility = View.VISIBLE

        // Adapteri ayarla
        categories = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Kategorileri yükle
        loadCategories()

        // Spinner'daki öğeleri seçmek için bir dinleyici ekle
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != AdapterView.INVALID_POSITION && position != 0) {
                    val selectedCategory = categories[position]
                    val intent = Intent(this@Tarifokuma, UrunListeleActivity2::class.java)
                    intent.putExtra("selectedCategoryId", selectedCategory.id)
                    intent.putExtra("selectedCategoryName", selectedCategory.name)

                    startActivity(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Bir şey seçilmediğinde yapılacak bir işlem yok
            }
        }
    }

    private fun loadCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val serverUrl = "http://10.0.2.2:4000/kategori"
                val url = URL(serverUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val bufferedReader = connection.inputStream.bufferedReader()
                    val response = StringBuilder()
                    var line: String?

                    while (bufferedReader.readLine().also { line = it } != null) {
                        response.append(line)
                    }

                    bufferedReader.close()

                    val jsonArray = JSONArray(response.toString())
                    categories.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val categoryId = jsonObject.getString("id")
                        val categoryName = jsonObject.getString("name")
                        categories.add(Category(categoryId, categoryName))
                    }

                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Tarifokuma, "Kategorileri yüklerken bir hata oluştu.", Toast.LENGTH_SHORT).show()
                    }
                }

                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Tarifokuma, "Bağlantı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
