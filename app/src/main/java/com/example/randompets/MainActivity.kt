package com.example.randompets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.randompets.databinding.ActivityMainBinding
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var petImageURL = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("petImageURL", "pet image URL set")

        var button = binding.getPhotoButton
        var image = binding.petImage
        getNextImage(button, image)
    }

    private fun getDogImageURL() {
        val client = AsyncHttpClient()
        client["https://dog.ceo/api/breeds/image/random", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Dog", "response successful")
                petImageURL = json.jsonObject.getString("message")

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Dog Error", errorResponse)
            }
        }]


    }

    private fun getCatImageURL() {
        val client = AsyncHttpClient()
        client["https://api.thecatapi.com/v1/images/search", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Cat", json.jsonArray.toString())
                val resultJSON = json.jsonArray.getJSONObject(0)
                petImageURL = resultJSON.getString("url")
                Log.d("cat url", petImageURL)

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Cat Error", errorResponse)
            }
        }]


    }

    private fun getNextImage(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            val choice = Random.nextInt(2)

            if (choice==0) {
                getDogImageURL()
            }
            else {
                getCatImageURL()
            }

            Glide.with(this)
                .load(petImageURL)
                .fitCenter()
                .into(imageView)
        }


    }
}

