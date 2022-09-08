package com.example.test_hsb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.example.test_hsb.databinding.ActivityBytGruppInfoBinding
import com.example.test_hsb.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class BytGruppInfo : AppCompatActivity() {
    private lateinit var binding: ActivityBytGruppInfoBinding

    val database = Firebase.database("https://brf-grasloken-default-rtdb.europe-west1.firebasedatabase.app").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBytGruppInfoBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)


        binding.readGroup.setOnClickListener {
            getGroup()
        }

        binding.saveGroup.setOnClickListener {

                var shopref = database.child("Grupper").child(binding.oldGroupNameET.text.toString())
                //.child(binding.adminAddGroupNameET.text.toString())
                //     shopref.setValue(binding.adminGroupTextET.text.toString())
                shopref.child("Namn").setValue(binding.newGroupNameET.text.toString())
                shopref.child("Info").setValue(binding.groupMoreInfoET.text.toString())

            //    numberOfGroups += 1
           //     writeNumberOfGroups()

        }

        binding.removeGroupBtn.setOnClickListener {
            removeGroup()
        }


    }

    fun removeGroup(){

        var shopref = database.child("Grupper").child(binding.oldGroupNameET.text.toString())


     /*   var shopref = database.child("Grupper").child(binding.oldGroupNameET.text.toString())
        shopref.setValue(null)
        shopref = database.child("Meddelande").child("Antal Grupper")
        numberOfGroups -= 1
        shopref.setValue(numberOfGroups.toString())*/
    }

    fun getGroup() {

        database.child("Grupper").child(binding.oldGroupNameET.text.toString()).get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")

  //              for (snapchild in it.children) {

                   // snapchild.value.toString()
                  //  snapchild.key.toString()

                for (snapchild in it.children) {
                    //     minAdapter.name.add(snapchild.value.toString())
                    //      minAdapter.description.add(snapchild.value.toString())

                    if (snapchild.key == "Namn") {

                        var editable = Editable.Factory.getInstance().newEditable(snapchild.value.toString())
                        binding.newGroupNameET.text = editable
                        binding.oldGroupNameTV.text = binding.newGroupNameET.text
                    }


                    if (snapchild.key == "Info") {
                        var editable = Editable.Factory.getInstance().newEditable(snapchild.value.toString())
                        binding.groupMoreInfoET.text = editable
                    }

                }



      //          editable = Editable.Factory.getInstance().newEditable(it.value.toString())
     //           binding.groupMoreInfoET.text = editable

           //     binding.groupMoreInfoET.text = editable

 //               }

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
 //                               minAdapter.names.add("Meddelande till alla grupper")
                                first = false
 //                               minAdapter.checked.add(true)
                            }
                            else {
 //                               minAdapter.names.add(snapchild.value.toString())
  //                              minAdapter.checked.add(false)
                            }
                        }

                        if (snapchild.key == "Info") {
  //                          minAdapter.description.add(snapchild.value.toString())
                        }

                    }
  //                  minAdapter.notifyDataSetChanged()

                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }
        }
    }
}