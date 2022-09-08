package com.example.test_hsb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test_hsb.databinding.ActivityInfoGroupBinding
import com.example.test_hsb.databinding.ActivityInfoMessageBinding

class InfoMessage : AppCompatActivity() {
    private lateinit var binding: ActivityInfoMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoMessageBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val newString: String?
        newString = if (savedInstanceState == null) {
            val extras = intent.extras
            extras?.getString("STRING_I_NEED")
        } else {
            savedInstanceState.getSerializable("STRING_I_NEED") as String?
        }

        binding.infoMessageTV.text = newString

    }
}