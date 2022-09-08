package com.example.test_hsb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_hsb.databinding.ActivityAdministratorBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class question {
    var str1 : String? = null
    var str2 : String? = null
    var str3 : String? = null
    var str4 : String? = null
    var str5 : String? = null
    var str6 : String? = null

}

class test {
    var str1: String? = null
    var str2: String? = null
}
class Administrator : AppCompatActivity() {
    private lateinit var binding: ActivityAdministratorBinding
    val database = Firebase.database("https://brf-grasloken-default-rtdb.europe-west1.firebasedatabase.app").reference

    var minAdapter = AdaptorAdministrator(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministratorBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

     //   setContentView(R.layout.activity_administrator)

        var myRecyclerView = findViewById<RecyclerView>(R.id.AdministratorRV)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = minAdapter

  // testar
      //  getAllGroups()

        binding.adminSendBtn.setOnClickListener {
            // All
            var stringToSend = ""
            if (checkedGlobal[0]){
                writeToAllGroups("0,")
            }
            else {

                    for (i in 1..(minAdapter.namesNumber.size - 1)){
                        if (checkedGlobal[i]){
                          //  return true
                            stringToSend += i.toString()+","
                        }
                    }

                    writeToCertainGroups(stringToSend)
            }
        }

        binding.jumpToCreateGroupBtn.setOnClickListener {
            val intent = Intent(this, SkapaGrupp::class.java)
            startActivity(intent)
        }
    }

    fun sendToGroups():Boolean{
        for (i in 1..3){
            if (checkedGlobal[i]){
                return true
            }
        }
        return false
    }

    fun getAllGroups() {
        var testar = ""

        var hejtest = test()

        // jag tror att detta skall vara här
        minAdapter.description.clear()
        minAdapter.namesNumber.clear()

        minAdapter.checked.clear()
        checkedGlobal.clear()
        minAdapter.notifyDataSetChanged()


        for (i in 0..(numberOfGroups-1))
        {
            database.child("Grupper").child(i.toString()).get()
                .addOnSuccessListener {
                    Log.i("firebase", "Got value ${it.value}")

                    minAdapter.namesNumber.add(i.toString())


                    minAdapter.checked.add(false)
                    checkedGlobal.add(false)

                    for (snapchild in it.children) {
                        //     minAdapter.name.add(snapchild.value.toString())
                        //      minAdapter.description.add(snapchild.value.toString())

                        if (snapchild.key == "Namn") {
                            minAdapter.name.add(snapchild.value.toString())
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

    fun writeMessage(inString: String) {
        var shopref = database.child("Meddelande").child("Alla Grupper").child("Meddelande")
        shopref.setValue("hej du glade")
    }
    fun writeToAllGroups(inString: String) {
        var shopref = database.child("Meddelande").child("Alla Grupper")
            //.child("TillDessaGrupper")
        shopref.setValue(inString+"/////"+binding.AdminET.text)
        shopref = database.child("Sparade Meddelanden").child("Alla").push()

     //   val sdf = SimpleDateFormat("yyyy/M/dd ")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  "+currentDate)

        shopref.setValue(currentDate+": " +binding.AdminET.text)
    }

    fun writeToCertainGroups(inString: String) {
        var shopref = database.child("Meddelande").child("Alla Grupper")
            //.child("TillDessaGrupper")
        shopref.setValue(inString+"/////"+binding.AdminET.text)
        shopref = database.child("Sparade Meddelanden").child("Utvalda").push()

     //   val sdf = SimpleDateFormat("yyyy/M/dd ")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  "+currentDate)

        shopref.setValue(inString+"/////"+currentDate+": " +binding.AdminET.text)
    }

    override fun onResume() {
        super.onResume()
        getAllGroups()
    }
}