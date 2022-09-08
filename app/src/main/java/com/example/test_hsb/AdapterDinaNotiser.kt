package com.example.test_hsb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class AdapterDinaNotiser(var mContext: Context) : RecyclerView.Adapter<HappyViewHolder>() {

    var names = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HappyViewHolder {
        val vh = HappyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dina_notiser_rad, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
       return names.size
     //   return 1
    }

    override fun onBindViewHolder(holder: HappyViewHolder, position: Int){
        holder.radTextview.text = names[position]
    //    holder.radTextview.text = "Hallå"
    }

}

class HappyViewHolder (view: View) : RecyclerView.ViewHolder(view){
    var radTextview = view.findViewById<TextView>(R.id.dina_notis_rad_tv)
}