package com.example.test_hsb

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterAllaNotiser(var mContext: Context) : RecyclerView.Adapter<AllaNotiserViewHolder>() {

    var names = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllaNotiserViewHolder {
        val vh = AllaNotiserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.alla_notiser_rad, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return names.size
        //   return 1
    }

    override fun onBindViewHolder(holder: AllaNotiserViewHolder, position: Int){
        holder.radTextview.text = names[position]
        //    holder.radTextview.text = "Hallå"

        holder.infoMessagesBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, InfoMessage::class.java)
            intent.putExtra("STRING_I_NEED", names[position])
            holder.itemView.context.startActivity(intent)
        }
    }

}

class AllaNotiserViewHolder (view: View) : RecyclerView.ViewHolder(view){
    var radTextview = view.findViewById<TextView>(R.id.alla_notis_rad_tv)
    var infoMessagesBtn = view.findViewById<Button>(R.id.infoAllMessBtn)
}