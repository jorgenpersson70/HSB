package com.example.test_hsb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_hsb.databinding.ActivityMainBinding
import com.example.test_hsb.databinding.ActivityProblem1Binding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class problem1 : AppCompatActivity() {
    private lateinit var binding: ActivityProblem1Binding
    val database = Firebase.database("https://brf-grasloken-default-rtdb.europe-west1.firebasedatabase.app").reference

    var minAdapter = AdapterLaggInlagenhet(this)
 //var minAdapter = AdapterAllaNotiser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.activity_problem1)
        binding = ActivityProblem1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var myRecyclerView = findViewById<RecyclerView>(R.id.problemRV)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = minAdapter

        checkedGlobalLagenhet.clear()
        minAdapter.description.clear()
        minAdapter.names.clear()
        minAdapter.checked.clear()

        getAllGroups()


        minAdapter.notifyDataSetChanged()

        binding.saveInfoApartmentBtn.setOnClickListener {
            var stringToSend = ""

            for (i in 0..(minAdapter.names.size-1)){
                var character = '0'
                if (checkedGlobalLagenhet[i]) {
                    stringToSend += character + i
                    stringToSend += ","
                }
            }

            var shopref = database.child("Lägenhet")
            shopref.setValue(binding.apartmentNumberET.text.toString())
            shopref = database.child("Lägenhet").child(binding.apartmentNumberET.text.toString()).child("Namn")
            shopref.setValue(binding.nameET2.text.toString())
            shopref = database.child("Lägenhet").child(binding.apartmentNumberET.text.toString()).child("Adress")
            shopref.setValue(binding.adressET2.text.toString())
            shopref = database.child("Lägenhet").child(binding.apartmentNumberET.text.toString()).child("Telefon")
            shopref.setValue(binding.phoneNumberET.text.toString())
            shopref = database.child("Lägenhet").child(binding.apartmentNumberET.text.toString()).child("Email")
            shopref.setValue(binding.eMailET.text.toString())

            shopref = database.child("Lägenhet").child(binding.apartmentNumberET.text.toString()).child("Valda Grupper")
            shopref.setValue(stringToSend)

        }

              binding.readInfoApartmentBtn.setOnClickListener {
                  getApartmentInfo()
              }


    }

    fun getApartmentInfo() {
        var testar = ""

               var shopref = database.child("Lägenhet").child(binding.apartmentNumberET.text.toString()).get()
                   .addOnSuccessListener {
                       Log.i("firebase", "Got value ${it.value}")

                       for (snapchild in it.children) {


                           if (snapchild.key == "Namn"){
                               binding.nameET2.setText(snapchild.value.toString())
                           }
                           if (snapchild.key == "Adress"){
                               binding.adressET2.setText(snapchild.value.toString())
                           }
                           if (snapchild.key == "Telefon"){
                               binding.phoneNumberET.setText(snapchild.value.toString())
                           }
                           if (snapchild.key == "Email"){
                               binding.eMailET.setText(snapchild.value.toString())
                           }
                           if (snapchild.key == "Valda Grupper"){
                               chosenGroups = snapchild.value.toString()


                               var countGroups = 0

                               if ("," in chosenGroups) {
                                   countGroups = getGroups(chosenGroups)
                                   println("hej")
                               }
                           }
                       }

                   }.addOnFailureListener {
                       Log.e("firebase", "Error getting data", it)
                   }
    }

    fun getGroups(stringToCheck: String): Int{
        var str = stringToCheck

        val numbers = Regex("[0-9]+").findAll(str)
            .map(MatchResult::value)
            .toList()

        println(numbers)
        checkedGlobalLagenhet.clear()
        minAdapter.checked.clear()

        var j = 0
        for (i in 0..(minAdapter.names.size-1)){
            if (numbers[j].toInt() == i){
                checkedGlobalLagenhet.add(true)
                minAdapter.checked.add(true)
                j += 1
            }else{
                checkedGlobalLagenhet.add(false)
                minAdapter.checked.add(false)
            }
            //          groups.add(numbers[i].toInt())
        }
        minAdapter.notifyDataSetChanged()

        //       findMessage(stringToCheck)

        return numbers.size
    }

    fun getAllGroups() {

        var testar = ""

        database.child("Grupper").get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")

                for (snapchild in it.children) {

                    checkedGlobalLagenhet.add(true)
                    minAdapter.description.add(snapchild.value.toString())
                    minAdapter.names.add(snapchild.key.toString())
                    minAdapter.checked.add(true)

                }
                minAdapter.notifyDataSetChanged()


            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }
}

