package com.example.test_hsb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_hsb.databinding.ActivityAdministratorBinding
import com.example.test_hsb.databinding.ActivityDinaNotiserBinding
import com.example.test_hsb.databinding.ActivityLaggInLagenhetBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

var chosenGroups = ""


class LaggInLagenhet : AppCompatActivity() {
    private lateinit var binding: ActivityLaggInLagenhetBinding
    val database = Firebase.database("https://brf-grasloken-default-rtdb.europe-west1.firebasedatabase.app").reference

    var minAdapter = AdapterLaggInlagenhet(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLaggInLagenhetBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

  //      setContentView(R.layout.activity_lagg_in_lagenhet)

        var myRecyclerView = findViewById<RecyclerView>(R.id.LagenhetRV)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = minAdapter


        checkedGlobalLagenhet.clear()
        minAdapter.description.clear()
        minAdapter.names.clear()
        minAdapter.checked.clear()


        getAllGroupsNew()
        alreadyIn()

        minAdapter.notifyDataSetChanged()


        binding.saveGroupsForApartmentBtn.setOnClickListener {
            var stringToSend = ""

            for (i in 0..(minAdapter.names.size-1)){
                        var character = '0'
                    if (checkedGlobalLagenhet2[i]) {
                        stringToSend += character + i
                        stringToSend += ","
                    }
            }

     /*       var shopref = database.child("Lägenhet")

            shopref.setValue(binding.ApartmentNbrET.text.toString())
            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Namn")
            shopref.setValue(binding.nameET.text.toString())
            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Adress")
            shopref.setValue(binding.adressET.text.toString())
            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Telefon")
            shopref.setValue(binding.telephoneET.text.toString())
            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Email")
            shopref.setValue(binding.emailET.text.toString())

            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Valda Grupper")
            shopref.setValue(stringToSend)

            shopref = database.child("Lägenhet")*/

    //        val database = Firebase.database("https://brf-grasloken-default-rtdb.europe-west1.firebasedatabase.app").reference


            var shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString())

            shopref.setValue(binding.ApartmentNbrET.text.toString())


            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Namn")
            shopref.setValue(binding.nameET.text.toString())
            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Adress")
            shopref.setValue(binding.adressET.text.toString())
            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Telefon")
            shopref.setValue(binding.telephoneET.text.toString())
            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Email")
            shopref.setValue(binding.emailET.text.toString())

            shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).child("Valda Grupper")
            shopref.setValue(stringToSend)

            shopref = database.child("Lägenhet")




        }

        binding.ApartmentNbrET.setOnClickListener {
            // möjligen skall lägenhetsnummer inte gå att ändra utan den kommer med inloggen
        }

       binding.readApartmentInfoBtn.setOnClickListener {
            getApartmentInfo()
        }

    }

    fun alreadyIn() {
        if (Firebase.auth.currentUser == null) {
            //           loginOK.value = false
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        } else {
 //           loginOK.value = true
 //           loggedIn = true

     // denna kan väl bort ?
            var editable = Editable.Factory.getInstance().newEditable(Firebase.auth.currentUser?.email.toString())
            val mString = Firebase.auth.currentUser?.email.toString()!!.split("@").toTypedArray()
            editable = Editable.Factory.getInstance().newEditable(mString[0])

        //    binding.loginEmailET.text = editable
            binding.ApartmentNbrET.text = editable

            getApartmentInfo()
        }
    }

    fun getApartmentInfo() {
        var testar = ""

        // var shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString())

        var shopref = database.child("Lägenhet").child(binding.ApartmentNbrET.text.toString()).get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")

                for (snapchild in it.children) {


                    if (snapchild.key == "Namn"){
                        binding.nameET.setText(snapchild.value.toString())
                    }
                    if (snapchild.key == "Adress"){
                        binding.adressET.setText(snapchild.value.toString())
                    }
                    if (snapchild.key == "Telefon"){
                        binding.telephoneET.setText(snapchild.value.toString())
                    }
                    if (snapchild.key == "Email"){
                        binding.emailET.setText(snapchild.value.toString())
                    }
                    if (snapchild.key == "Valda Grupper"){
                        chosenGroups = snapchild.value.toString()


                        var countGroups = 0

                        if ("," in chosenGroups) {
                            countGroups = getGroupsNew(chosenGroups)
                            println("hej")
                        }
                    }
                }

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }

    fun getGroupsOld(stringToCheck: String): Int{
        var str = stringToCheck

        val numbers = Regex("[0-9]+").findAll(str)
            .map(MatchResult::value)
            .toList()

        println(numbers)
        checkedGlobalLagenhet.clear()
        minAdapter.checked.clear()

        var j = 0
        for (i in 0..(minAdapter.names.size-1)){
            if (j < numbers.size) {
                if (numbers[j].toInt() == i){
                    checkedGlobalLagenhet.add(true)
                    minAdapter.checked.add(true)
                    j += 1
                }else{
                    checkedGlobalLagenhet.add(false)
                    minAdapter.checked.add(false)
                }
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

    fun getGroupsNew(stringToCheck: String): Int{
        var str = stringToCheck

        val numbers = Regex("[0-9]+").findAll(str)
            .map(MatchResult::value)
            .toList()

        println(numbers)
        checkedGlobalLagenhet2.clear()
        minAdapter.checked.clear()

   //     checkedGlobalLagenhet2.add(true)
  //      minAdapter.checked.add(true)

        var j = 0

      //  for (i in 0..(minAdapter.names.size-1)){
        for (i in 0..(minAdapter.names.size-1)){
            if (j < numbers.size) {

                if (numbers[j].toInt() == i) {
                    checkedGlobalLagenhet2.add(true)
                    minAdapter.checked.add(true)
                    //               minAdapter.checked.add(true)
                    j += 1
                } else {
                    checkedGlobalLagenhet2.add(false)
                    minAdapter.checked.add(false)
                    //               minAdapter.checked.add(false)
                }
            }
            else{
                checkedGlobalLagenhet2.add(false)
                minAdapter.checked.add(false)
            }
            //          groups.add(numbers[i].toInt())
        }
        minAdapter.notifyDataSetChanged()

        return numbers.size
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

    fun getAllGroups() {

        var testar = ""
        var first = true

        database.child("Grupper").get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")

                for (snapchild in it.children) {

                    if (first){
                        minAdapter.names.add("Meddelande till alla grupper")
                        first = false
                        minAdapter.checked.add(true)
                    }
                    else {
                        minAdapter.names.add(snapchild.key.toString())
                        minAdapter.checked.add(false)
                    }
                    minAdapter.description.add(snapchild.value.toString())

                }
                minAdapter.notifyDataSetChanged()

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }

    fun getAllGroupsNew() {
        var testar = ""
        var first = true

        for (i in 0..(numberOfGroups-1))
        {
            database.child("Grupper").child(i.toString()).get()
                .addOnSuccessListener {
                    Log.i("firebase", "Got value ${it.value}")

                    for (snapchild in it.children) {
                        //     minAdapter.name.add(snapchild.value.toString())
                        //      minAdapter.description.add(snapchild.value.toString())

                        if (snapchild.key == "Namn") {
                            if (first){
                                minAdapter.names.add("Meddelande till alla grupper")
                                first = false
                                minAdapter.checked.add(true)
                            }
                            else {
                                minAdapter.names.add(snapchild.value.toString())
                                minAdapter.checked.add(false)
                            }
                        }

                        if (snapchild.key == "Info") {
                            minAdapter.description.add(snapchild.value.toString())
                        }

                    }
                    minAdapter.notifyDataSetChanged()

                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }
        }
    }
}