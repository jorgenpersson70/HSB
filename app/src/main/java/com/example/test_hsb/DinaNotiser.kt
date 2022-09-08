package com.example.test_hsb

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_hsb.databinding.ActivityDinaNotiserBinding
import com.example.test_hsb.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

var messages = mutableListOf<String>()

var groups2 = mutableListOf<Int>()

var messageSplit = mutableListOf<String>()

class DinaNotiser : AppCompatActivity() {
    private lateinit var binding: ActivityDinaNotiserBinding
    val database = Firebase.database("https://brf-grasloken-default-rtdb.europe-west1.firebasedatabase.app").reference

    var minAdapter = AdapterDinaNotiser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDinaNotiserBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        var myRecyclerView = findViewById<RecyclerView>(R.id.DinaNotiserRV)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = minAdapter

  /*      if (inCommingMessage.isNotEmpty()) {
            binding.dinNotisTV.text = inCommingMessage[0]
        }*/

        getApartmentInfo()

        minAdapter.names.clear()
        getAllMessages()
      //  getGroups()
    }

    fun getAllMessages() {
        var testar = ""

        database.child("Sparade Meddelanden").child("Utvalda").get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")

                var countGroups = 0

                for (snapchild in it.children) {
                    groups2.clear()
                    messageSplit.clear()


               //     groups2.add(0)
                  //  groups2.removeAll()
                    countGroups = getGroups(snapchild.value.toString())


                    // groups2 now contains all groups that are adressed
                  //  checkedGlobalLagenhet2 contains if this apartment is interested in the group
                  for (i in 0..(countGroups-1)) {

                      // This should never happen that groups[] is to high, but just in case we check it
                      if (groups2[i] <= numberOfGroups) {
                          if (checkedGlobalLagenhet2[groups2[i]]) {
                              minAdapter.names.add(messageSplit[1])
                          }
                      }
                  }
                }
                minAdapter.notifyDataSetChanged()

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }

    fun isGroupPresent(groupNumber: Int, numberOfGroups: Int): Boolean{
        if (numberOfGroups > 0) {
            for (i in 0..(numberOfGroups-1)) {
                if (groups2[i] == groupNumber) {
                    return true
                }
            }
        }
        return false
    }

    fun getGroups(stringToCheck: String): Int{
        var str = stringToCheck

        findMessage(str)

        val numbers = Regex("[0-9]+").findAll(messageSplit[0])
  //      val numbers = Regex("[0-9]+").findAll(stringToCheck)
            .map(MatchResult::value)
            .toList()

        println(numbers)

        for (i in 0..(numbers.size-1)){
            groups2.add(numbers[i].toInt())
        }

        return numbers.size
    }

    fun findMessage(stringToCheck: String){
        val mString = stringToCheck!!.split("/////").toTypedArray()
   //     binding.messageTV.text = mString[1]
        messageSplit.add(mString[0])
        messageSplit.add(mString[1])
    }

    fun getApartmentInfo() {
        var testar = ""

        val mString = Firebase.auth.currentUser?.email.toString()!!.split("@").toTypedArray()

        var shopref = database.child("Lägenhet").child(mString[0]).get()
  //      var shopref = database.child("Lägenhet").child("101".toString()).get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")

                for (snapchild in it.children) {

                    if (snapchild.key == "Valda Grupper"){
                        chosenGroups2 = snapchild.value.toString()

                        var countGroups2 = 0

                        if ("," in chosenGroups2) {
                            countGroups2 = getGroups2(chosenGroups2)
                            println("hej")
                        }
                    }
                }

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }


    fun getGroups2(stringToCheck: String): Int{
        var str = stringToCheck

        val numbers = Regex("[0-9]+").findAll(str)
            .map(MatchResult::value)
            .toList()

        println(numbers)
        checkedGlobalLagenhet2.clear()


        var j = 0
        for (i in 0..(numberOfGroups-1)){
            if (j < numbers.size) {
                if (numbers[j].toInt() == i) {
                    checkedGlobalLagenhet2.add(true)
                    //               minAdapter.checked.add(true)
                    j += 1
                } else {
                    checkedGlobalLagenhet2.add(false)
                    //               minAdapter.checked.add(false)
                }
            }
            else{
                checkedGlobalLagenhet2.add(false)
            }
            //          groups.add(numbers[i].toInt())
        }
        //      minAdapter.notifyDataSetChanged()

        //       findMessage(stringToCheck)

        return numbers.size
    }
}