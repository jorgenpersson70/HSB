package com.example.test_hsb

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.test_hsb.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.*

data class  ShopItem(var player: Int? = null, var row: Int? = null, var column: Int? = null){

}
var chosenGroups2 = ""

// I just set a value, the value is later read from database.
var numberOfGroups = 500

var checkedGlobalLagenhet2 = mutableListOf<Boolean>()
var numbers2 = mutableListOf<Boolean>()
var groups = mutableListOf<Int>()

var myshop = ShopItem()

var myshopwrite = ShopItem()

var BelongAllGroups = false
// var BelongGroupOne = false


var inCommingMessage = mutableListOf<String>()

enum class LoginResult {
    LOGINOK, LOGINFAIL, REGISTERFAIL
}

val loginOK: MutableLiveData<Boolean> by lazy {
    MutableLiveData<Boolean>()
}

var loggedIn = false

val loginStatus: MutableLiveData<LoginResult> by lazy {
    MutableLiveData<LoginResult>()
}

class MainActivity : AppCompatActivity() {
  //  private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityMainBinding
    var mediaPlayer : MediaPlayer? = null

    private var remoteView: RemoteViews? = null
    private var notification: Notification? = null
    private var notificationManager: NotificationManager? = null
    private val NotificationID = 1111
    private var builder: NotificationCompat.Builder? = null


    val database = Firebase.database("https://brf-grasloken-default-rtdb.europe-west1.firebasedatabase.app").reference

  //  val testref = database.child("Meddelande").child("Alla Grupper")

 //   var testrefwrite = database.child("Meddelande").child("Alla Grupper").child("TillDessaGrupper")

    var testref = database.child("Meddelande").child("Alla Grupper")

  //  val testref2 = database.child("Meddelande").child("Grupp 2")





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)


        playSoundMain(true)

        readNumberOfGroups()
        // tid måste flyta

        // Just to have group 0 that means that message for everyone is received
        getGroups2("0,")
        // If the logged in is found in database, then the groups are fetched
        checkLogin()

    //    var testar = binding.MyTV
        var testar = ""
        testar = "Hej"


       // testref = database.child("Tillhör Alla Grupper")

        // Om det är admin som startar
        testref.setValue("")

        // Grupp 1
        testref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var value = snapshot.getValue<String>()

                if (value != null) {

                    testar = value

                    var test = findViewById<View>(R.id.myView)

                    var countGroups = 0
                    var countGroups2 = 0

                   /* if ((testar == "Alla") && (BelongAllGroups)) {
                        notifyMe(test, 0, testar.toString())
                    }
                    else {*/
                     //   if ("," in value) {

                    if (value != "") {

                        // jag tror
                        inCommingMessage.clear()
                        groups.clear()
                        countGroups = getGroups(value)
                        println("hej")

    //                    getApartmentInfo()

                        if (isGroupPresent(0, countGroups) && BelongAllGroups) {
                //            inCommingMessage
                //            notifyMe(test, 0, binding.messageTV.text.toString())
                            notifyMe(test, 0, inCommingMessage[0])
                        } else {
                            var j = 0
                            var notify = false
                            for (i in 0..(numberOfGroups - 1)) {

                                // kanske kan checkedGlobalLagenhet2 försvinna
                                if ((groups[j] == i) && (checkedGlobalLagenhet2[i] == true)) {
                                    notify = true
                                    j += 1
                                }
                            }
                            if (notify) {

                       //         notifyMe(test, 1, binding.messageTV.text.toString())
                                notifyMe(test, 1, inCommingMessage[0])
                            }
                        }
                    }

                 //   }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed ", error.toException())
            }
        })

  /*      binding.writeBtn.setOnClickListener {
            clickrow()
        }*/

        binding.dinaNotiserBtn.setOnClickListener {
            val intent = Intent(this, DinaNotiser::class.java)
            startActivity(intent)
        }

        binding.allaNotiserBtn.setOnClickListener {
            val intent = Intent(this, AllaNotiser::class.java)
            startActivity(intent)
        }

        binding.laggInLagenhetBtn.setOnClickListener {
            val intent = Intent(this, LaggInLagenhet::class.java)
            startActivity(intent)
  /*          val intent = Intent(this, problem1::class.java)
            startActivity(intent)*/
        }

        binding.administratorBtn.setOnClickListener {
            val intent = Intent(this, Administrator::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }


  /*      binding.myBtn.setOnClickListener {
            getGroups("Hej")
        }*/



        checkAllGroups()

       // getApartmentInfo()
    }

    fun getApartmentInfo(apartmentNumberString : String) {
        var testar = ""

        val mString = apartmentNumberString!!.split("@").toTypedArray()

    //    var shopref = database.child("Lägenhet").child("101".toString()).get()

        var shopref = database.child("Lägenhet").child(mString[0]).get()
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

    fun getGroups(stringToCheck: String): Int{
        var str = stringToCheck

        val numbers = Regex("[0-9]+").findAll(str)
            .map(MatchResult::value)
            .toList()

        println(numbers)

        for (i in 0..(numbers.size-1)){
            groups.add(numbers[i].toInt())
        }

        findMessage(stringToCheck)

        return numbers.size
    }

    fun findMessage(stringToCheck: String){
        val mString = stringToCheck!!.split("/////").toTypedArray()
 //       binding.messageTV.text = mString[1]
        inCommingMessage.add(mString[1])
    }


    fun isGroupPresent(groupNumber: Int, numberOfGroups: Int): Boolean{
        if (numberOfGroups > 0) {
            for (i in 0..(numberOfGroups-1)) {
                if (groups[i] == groupNumber) {
                    return true
                }
            }
        }
        return false
    }

    fun clickrow(){
        myshopwrite.row = 1
        myshopwrite.column = 1
        myshopwrite.player = 1

        var shopref = database.child("Tillhör Grupp 1")
        shopref.setValue("JA")

        shopref = database.child("Tillhör Grupp 2")
        shopref.setValue("NEJ")

        shopref = database.child("Tillhör Grupp 3")
        shopref.setValue("NEJ")

        shopref = database.child("Tillhör Alla Grupper")
        shopref.setValue("NEJ")
    }

    fun playSoundMain(start : Boolean) {

        var resId = getResources().getIdentifier(
            R.raw.main_activity_startad.toString(),
            "raw", this?.packageName
        )

        if (start) {
            if(mediaPlayer == null)
            {
                mediaPlayer = MediaPlayer.create(this, resId)
                mediaPlayer!!.start()
            } else {
                mediaPlayer!!.seekTo(0)
                mediaPlayer!!.start()
            }
        } else {

            if(mediaPlayer != null)
            {
                mediaPlayer!!.pause()
            }
        }
    }

    fun playSound(start : Boolean) {

        var resId = getResources().getIdentifier(
            R.raw.ping.toString(),
            "raw", this?.packageName
        )

        if (start) {
            if(mediaPlayer == null)
            {
                mediaPlayer = MediaPlayer.create(this, resId)
                mediaPlayer!!.start()
            } else {
                mediaPlayer!!.seekTo(0)
                mediaPlayer!!.start()
            }
        } else {

            if(mediaPlayer != null)
            {
                mediaPlayer!!.pause()
            }
        }
    }

    fun notifyMe(view: View?, notis: Int, inString: String) {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        builder = NotificationCompat.Builder(applicationContext, "ChannelId-111")
        notificationManager!!.cancel(NotificationID)
        if (notis == 0) {
            remoteView = RemoteViews(packageName, R.layout.notification)
        }
        if (notis == 1) {
            remoteView = RemoteViews(packageName, R.layout.notification1)
        }

        val switchIntent = Intent(this, ny::class.java)
        val pendingSwitchIntent = PendingIntent.getBroadcast(this, 1212, switchIntent, 0)
        builder!!.setSmallIcon(R.drawable.ic_launcher_background)
        builder!!.setFullScreenIntent(pendingSwitchIntent, true)
        builder!!.priority = Notification.PRIORITY_HIGH
        builder!!.build().flags = Notification.FLAG_NO_CLEAR or Notification.PRIORITY_HIGH
        builder!!.setContent(remoteView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "ChannelId"
            val channel = NotificationChannel(channelId, "MyChannelName", NotificationManager.IMPORTANCE_HIGH)
            notificationManager!!.createNotificationChannel(channel)
            builder!!.setChannelId(channelId)
        }
        notification = builder!!.build()
        notificationManager!!.notify(NotificationID, notification)
    }

    fun readNumberOfGroups() {

        database.child("Meddelande").child("Antal Grupper").get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
       //         binding.gruppAllaTV.text = "NEJ"
                numberOfGroups = it.value.toString().toInt()
  /*              if (it.value.toString() == "JA"){
                    BelongAllGroups = true
                    binding.gruppAllaTV.text = "JA"
                }*/

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }


    }

    fun checkAllGroups() {
        var testar = ""

        database.child("Tillhör Alla Grupper").get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
 //               binding.gruppAllaTV.text = "NEJ"
                if (it.value.toString() == "JA"){
                    BelongAllGroups = true
  //                  binding.gruppAllaTV.text = "JA"
                }

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }








    fun checkLogin()
    {
        if(Firebase.auth.currentUser == null)
        {
 //           loginOK.value = false
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

  /*        getApartmentInfo(Firebase.auth.currentUser?.email.toString())

            val mString = Firebase.auth.currentUser?.email.toString()!!.split("@").toTypedArray()
            if (mString[0] == "9999"){
                binding.administratorBtn.visibility = View.VISIBLE
            }
            else{
                binding.administratorBtn.visibility = View.INVISIBLE
            }*/
        } else {
            loginOK.value = true
            loggedIn = true

            // Firebase.auth.currentUser?.email.toString()
            getApartmentInfo(Firebase.auth.currentUser?.email.toString())

            val mString = Firebase.auth.currentUser?.email.toString()!!.split("@").toTypedArray()
            if (mString[0] == "9999"){
                binding.administratorBtn.visibility = View.VISIBLE
            }
            else{
                binding.administratorBtn.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //other stuff
        getApartmentInfo(Firebase.auth.currentUser?.email.toString())

        val mString = Firebase.auth.currentUser?.email.toString()!!.split("@").toTypedArray()
        if (mString[0] == "9999"){
            binding.administratorBtn.visibility = View.VISIBLE
        }
        else{
            binding.administratorBtn.visibility = View.INVISIBLE
        }

    }


}