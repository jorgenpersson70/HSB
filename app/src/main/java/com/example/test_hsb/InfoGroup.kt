package com.example.test_hsb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test_hsb.databinding.ActivityInfoGroupBinding
import com.example.test_hsb.databinding.ActivityMainBinding

class InfoGroup : AppCompatActivity() {
    private lateinit var binding: ActivityInfoGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoGroupBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val newString: String?
        newString = if (savedInstanceState == null) {
            val extras = intent.extras
            extras?.getString("STRING_I_NEED")
        } else {
            savedInstanceState.getSerializable("STRING_I_NEED") as String?
        }

        binding.infoGroupTV.text = newString

        /* java

        String newString;
if (savedInstanceState == null) {
    Bundle extras = getIntent().getExtras();
    if(extras == null) {
        newString= null;
    } else {
        newString= extras.getString("STRING_I_NEED");
    }
} else {
    newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");
}
         */


    }
}