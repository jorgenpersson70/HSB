package com.example.test_hsb

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


var checkedGlobal = mutableListOf<Boolean>()

class AdaptorAdministrator(var mContext: Context) : RecyclerView.Adapter<AdministratorViewHolder>() {

    var namesNumber = mutableListOf<String>()
    var name = mutableListOf<String>()
    var description = mutableListOf<String>()
    var checked = mutableListOf<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdministratorViewHolder {
        val vh = AdministratorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.administrator_rad, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return namesNumber.size
        //   return 1
    }

    override fun onBindViewHolder(holder: AdministratorViewHolder, position: Int){
        holder.radTextview.text = namesNumber[position]
        holder.radTextGruppNamnTV.text = name[position]

        if (checked[position]) {
 //           holder.checkBox.visibility = View.VISIBLE
            holder.checkBox.text = ""
   //         holder.checkBox.setCheck(true)
            holder.checkBox.isChecked=true
          //  holder.checkBox.setCh
        }
        else {
 //           holder.checkBox.visibility = View.INVISIBLE
            holder.checkBox.text = ""
            holder.checkBox.isChecked=false
        }

        holder.checkBox.setOnClickListener {
            checkedGlobal[position] = checkedGlobal[position] != true
        }
        holder.infoBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, InfoGroup::class.java)
            intent.putExtra("STRING_I_NEED", description[position])
            holder.itemView.context.startActivity(intent)
        }
    }



}

class AdministratorViewHolder (view: View) : RecyclerView.ViewHolder(view){
    var radTextview = view.findViewById<TextView>(R.id.admin_rad_tv)
    var radTextGruppNamnTV = view.findViewById<TextView>(R.id.admin_rad_TV2)
    var checkBox = view.findViewById<CheckBox>(R.id.admincheckBox)
    var infoBtn = view.findViewById<Button>(R.id.infoForGroupBtn)
}