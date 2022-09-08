package com.example.test_hsb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_hsb.databinding.ActivityAllaNotiserBinding
import com.example.test_hsb.databinding.ActivityDinaNotiserBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AllaNotiser : AppCompatActivity() {
    private lateinit var binding: ActivityAllaNotiserBinding
    val database = Firebase.database("https://brf-grasloken-default-rtdb.europe-west1.firebasedatabase.app").reference

    var minAdapter = AdapterAllaNotiser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllaNotiserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        var myRecyclerView = findViewById<RecyclerView>(R.id.AllaNotiserRV)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = minAdapter

    /*    if (inCommingMessage.isNotEmpty()){
            binding.allaNotisTV.text = inCommingMessage[0]
        }*/

        getAllMessages()


    }

    fun getAllMessages() {
        var testar = ""

        database.child("Sparade Meddelanden").child("Alla").get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")

                for (snapchild in it.children) {
                    minAdapter.names.add(snapchild.value.toString())
                }
                minAdapter.notifyDataSetChanged()

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }


}