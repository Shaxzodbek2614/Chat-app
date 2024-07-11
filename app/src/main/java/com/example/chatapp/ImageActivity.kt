package com.example.chatapp

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import com.example.chatapp.databinding.ActivityImageBinding
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.models.Message
import com.squareup.picasso.Picasso

class ImageActivity : AppCompatActivity() {
    lateinit var back:ImageView
    lateinit var more:ImageView
    lateinit var rasm:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        back = findViewById(R.id.back)
        more = findViewById(R.id.more)
        rasm = findViewById(R.id.rasm)

        back.setOnClickListener {
            finish()
        }
        val message = intent.getSerializableExtra("key") as Message
        Picasso.get().load(message.imageUri).into(rasm)

    }
}