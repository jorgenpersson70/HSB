package com.example.test_hsb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test_hsb.databinding.ActivitySkapaGruppBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SkapaGrupp : AppCompatActivity() {
    private lateinit var binding: ActivitySkapaGruppBinding
    val database = Firebase.database("https://brf-grasloken-default-rtdb.europe-west1.firebasedatabase.app").reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_skapa_grupp)
        binding = ActivitySkapaGruppBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        binding.adminSaveGroupBtn.setOnClickListener {

 /*           var shopref = database.child("Grupper").child(binding.adminAddGroupNameET.text.toString())
            shopref.setValue(binding.adminGroupTextET.text.toString())*/

            var shopref = database.child("Grupper").child(numberOfGroups.toString())
                //.child(binding.adminAddGroupNameET.text.toString())
       //     shopref.setValue(binding.adminGroupTextET.text.toString())
            shopref.child("Namn").setValue(binding.adminAddGroupNameET.text.toString())
            shopref.child("Info").setValue(binding.adminGroupTextET.text.toString())

            numberOfGroups += 1
            writeNumberOfGroups()
        }



        binding.changeGroupBtn.setOnClickListener {
            val intent = Intent(this, BytGruppInfo::class.java)
            startActivity(intent)
        }

    }




    fun writeNumberOfGroups() {
        var shopref = database.child("Meddelande").child("Antal Grupper")
        //.child("TillDessaGrupper")
        shopref.setValue(numberOfGroups.toString())
    }
}