package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class TarifEkle : Activity() {
    private lateinit var categorySpinner: Spinner
    private lateinit var categories: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tarifekle)

        val recipeNameEditText = findViewById<EditText>(R.id.recipeNameEditText)
        val preparationTimeEditText = findViewById<EditText>(R.id.preparationTimeEditText)
        val ingredientsEditText = findViewById<EditText>(R.id.ingredientsEditText)
        val preparationEditText = findViewById<EditText>(R.id.preparationEditText)
        val addRecipeButton = findViewById<Button>(R.id.addRecipeButton)

        // Initialize category spinner
        categorySpinner = findViewById(R.id.categorySpinner)
        categorySpinner.visibility = View.VISIBLE

        categories = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Load categories
        loadCategories()

        addRecipeButton.setOnClickListener {
            val name = recipeNameEditText.text.toString()
            val preparationTime = preparationTimeEditText.text.toString()
            val ingredients = ingredientsEditText.text.toString()
            val preparation = preparationEditText.text.toString()
            val selectedCategory = categorySpinner.selectedItem?.toString() ?: ""
            val categoryId = selectedCategory.substringAfterLast("-").toIntOrNull() ?: 0

            // Ensure all fields are filled
            if (name.isEmpty() || preparationTime.isEmpty() || ingredients.isEmpty() || preparation.isEmpty() || selectedCategory.isEmpty()) {
                Toast.makeText(this@TarifEkle, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create POST request
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val serverUrl = "http://10.0.2.2:4000/tarif"
                    val url = URL(serverUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true

                    val postData = JSONObject().apply {
                        put("adi", name)
                        put("hazirlanma_zamani", preparationTime)
                        put("malzemeler", ingredients)
                        put("hazirlanisi", preparation)
                        put("kategori_id", categoryId) // Use category ID in post data
                    }

                    OutputStreamWriter(connection.outputStream).use { outputStreamWriter ->
                        outputStreamWriter.write(postData.toString())
                        outputStreamWriter.flush()
                    }

                    val responseCode = connection.responseCode
                    withContext(Dispatchers.Main) {
                        if (responseCode == HttpURLConnection.HTTP_CREATED) {
                            Toast.makeText(this@TarifEkle, "Tarif başarıyla eklendi!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@TarifEkle, "Bir hata oluştu.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    connection.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@TarifEkle, "Bağlantı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
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
                        val categoryId = jsonObject.getInt("id")
                        categories.add("$categoryName-$categoryId") // Kategori adı ve ID'si ekleniyor
                    }

                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@TarifEkle, "Kategorileri yüklerken bir hata oluştu.", Toast.LENGTH_SHORT).show()
                    }
                }

                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TarifEkle, "Bağlantı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
