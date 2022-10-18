package com.lukasbeckercode.forrestwatch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.models.MarkedTree

interface OnItemClickListener{
    fun setOnClickListener(pos:Int,treeView: TreeView)
    fun setOnLongClickListener(pos:Int,treeView: TreeView)
}
class RecyclerAdapter(var treeView: TreeView):RecyclerView.Adapter<RecyclerAdapter.ViewHandler>() {
    var trees:List<MarkedTree>? = null
    lateinit var listener: OnItemClickListener

    inner class ViewHandler(itemView:View, var listener: OnItemClickListener,):RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
        val tvTitle:TextView = itemView.findViewById(R.id.card_view_tree_cl_title)
        val tvDetails:TextView = itemView.findViewById(R.id.card_view_tree_cl_details)

        override fun onClick(p0: View?) {
            listener.setOnClickListener(adapterPosition,treeView)
        }

        override fun onLongClick(p0: View?): Boolean {
            listener.setOnLongClickListener(adapterPosition, treeView)
            return true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHandler {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_tree,parent,false)
        return ViewHandler(view,listener,)
    }

    override fun onBindViewHolder(holder: ViewHandler, position: Int) {
        if(trees!=null){
            holder.tvTitle.text = trees!![position].name

            val builder:StringBuilder = java.lang.StringBuilder()

            builder.append("Added by: ")
            builder.append(trees!![position].user!!.firstname)
            builder.appendLine()
            builder.append("Lat: ")
            builder.append(trees!![position].lat)
            builder.appendLine()
            builder.append("Long: ")
            builder.append(trees!![position].long)


            holder.tvDetails.text = builder.toString()
        }

    }

    override fun getItemCount(): Int {
        if (trees == null){
            return 0
        }
        return trees!!.size
    }


    fun setOnClickListener(listener: OnItemClickListener){
        this.listener = listener
    }


}



