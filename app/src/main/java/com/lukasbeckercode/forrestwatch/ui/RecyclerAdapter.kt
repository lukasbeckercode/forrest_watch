package com.lukasbeckercode.forrestwatch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lukasbeckercode.forrestwatch.R

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.ViewHandler>() {
    //TODO: Replace with actual data
    private val mockTitles = arrayOf("Tree 1", "Tree 2", "Tree 3")
    private val mockDetails = arrayOf("user 1", "user 2", "user3")

    inner class ViewHandler(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvTitle:TextView = itemView.findViewById(R.id.card_view_tree_cl_title)
        val tvDetails:TextView = itemView.findViewById(R.id.card_view_tree_cl_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHandler {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_tree,parent,false)
        return ViewHandler(view)
    }

    override fun onBindViewHolder(holder: ViewHandler, position: Int) {
        holder.tvTitle.text = mockTitles[position]
        holder.tvDetails.text = mockDetails[position]
    }

    override fun getItemCount(): Int {
        return mockTitles.size
    }
}