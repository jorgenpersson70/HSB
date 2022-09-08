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

var checkedGlobalLagenhet = mutableListOf<Boolean>()

class AdapterLaggInlagenhet(var mContext: Context) : RecyclerView.Adapter< LaggInLagenhetViewHolder>() {

    var names = mutableListOf<String>()
    var description = mutableListOf<String>()
    var checked = mutableListOf<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  LaggInLagenhetViewHolder {
        val vh =  LaggInLagenhetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.lagg_in_lagenhet_rad, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return names.size
        //   return 1
    }

    override fun onBindViewHolder(holder: LaggInLagenhetViewHolder, position: Int){
        holder.radTextview.text = names[position]

        if (checked[position]) {
            holder.checkBox.text = ""
            holder.checkBox.isChecked=true
        }
        else {
            holder.checkBox.text = ""
            holder.checkBox.isChecked=false
        }

        holder.checkBox.setOnClickListener {
            // the first one cannot be changed
            if (position > 0) {
      //          checkedGlobalLagenhet[position] = checkedGlobalLagenhet[position] != true
                checkedGlobalLagenhet2[position] = checkedGlobalLagenhet2[position] != true
            }
            else{
                holder.checkBox.isChecked=true // force it back if it toggled to false
            }
        }

        holder.infoBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, InfoGroup::class.java)
            intent.putExtra("STRING_I_NEED", description[position])
            holder.itemView.context.startActivity(intent)
        }

    }

}

class LaggInLagenhetViewHolder (view: View) : RecyclerView.ViewHolder(view){
    var radTextview = view.findViewById<TextView>(R.id.lagg_in_lagenhet_tv)
    var checkBox = view.findViewById<CheckBox>(R.id.laggInLagenhetCheckBox)
    var infoBtn = view.findViewById<Button>(R.id.infoGroupLghBtn)
}
