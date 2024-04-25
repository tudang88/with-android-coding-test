package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ItemUserBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = okhttp3.Request.Builder()
                .url("https://raw.githubusercontent.com/withjp-inc/with_android_coding_test/main/api/users/users.json")
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw Exception("Unexpected code $response")

                val body = response.body!!.string()
                val users = Json.decodeFromString<List<User>>(body)

                withContext(Dispatchers.Main) {
                    binding.recyclerView.adapter = MyAdapter(users)
                }
            }
        }
    }
}

@Serializable
data class User(
    val id: Int,
    val nickname: String,
    val photo: String
)

class MyAdapter(
    val users: List<User>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemUserBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view.root)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.nicknameTextView.text = users[position].nickname
        holder.binding.thumbnailImageView.load(users[position].photo)
    }
}