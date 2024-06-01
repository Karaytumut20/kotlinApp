package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

private const val s = "POST"

class KategoriEkle : AppCompatActivity() {

    private lateinit var categoryNameEditText: EditText
    private lateinit var addCategoryButton: Button
    private lateinit var loadCategoriesButton: Button
    private lateinit var categorySpinner: Spinner
    private lateinit var categories: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kategoriekle)

        // UI bileşenlerini tanımlama
        categoryNameEditText = findViewById(R.id.categoryNameEditText)
        addCategoryButton = findViewById(R.id.addCategoryButton)
        loadCategoriesButton = findViewById(R.id.loadCategoriesButton)
        categorySpinner = findViewById(R.id.categorySpinner)

        // Spinner'ı ilk başta gizle
        categorySpinner.visibility = View.GONE

        // Adapteri ayarlama
        categories = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Kategori ekleme butonuna tıklama olayı
        addCategoryButton.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString()
            if (categoryName.isNotEmpty()) {
                addCategory(categoryName)
            } else {
                Toast.makeText(this, "Kategori adını girin.", Toast.LENGTH_SHORT).show()
            }
        }

        // Kategorileri yükleme butonuna tıklama olayı
        loadCategoriesButton.setOnClickListener {
            loadCategories()
            // Kategoriler yüklendikten sonra Spinner'ı göster
            categorySpinner.visibility = View.VISIBLE
        }
    }

    private fun addCategory(categoryName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val serverUrl = "http://10.0.2.2:4000/kategori"
                val url = URL(serverUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"

                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val postData = JSONObject().apply {
                    put("name", categoryName)
                }.toString()
                val outputStreamWriter = OutputStreamWriter(connection.outputStream)
                outputStreamWriter.write(postData)
                outputStreamWriter.flush()
                outputStreamWriter.close()

                val responseCode = connection.responseCode
                withContext(Dispatchers.Main) {
                    if (responseCode == HttpURLConnection.HTTP_CREATED) {
                        Toast.makeText(this@KategoriEkle, "Kategori başarıyla eklendi!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@KategoriEkle, "Bir hata oluştu.", Toast.LENGTH_SHORT).show()
                    }
                }

                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@KategoriEkle, "Bağlantı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                }
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
                        val categoryName = jsonObject.getString("name")
                        categories.add(categoryName)
                    }

                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@KategoriEkle, "Kategorileri yüklerken bir hata oluştu.", Toast.LENGTH_SHORT).show()
                    }
                }

                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@KategoriEkle, "Bağlantı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
