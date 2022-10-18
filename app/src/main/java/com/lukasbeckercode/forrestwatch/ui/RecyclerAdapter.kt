package com.lukasbeckercode.forrestwatch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.models.MarkedTree

/**
 * Interface to handle the different possible OnClick events for each element of the RecycleView
 * @author lukas becker
 */
interface OnItemClickListener{
    /**
     * Card is tapped shortly
     * @param pos position of the current Card
     * @param treeView originally calling activity (needed for contexts)
     */
    fun setOnClickListener(pos:Int,treeView: TreeView)

    /**
     * user presses and holds card
     * @param pos position of the current Card
     * @param treeView originally calling activity (needed for contexts)
     */
    fun setOnLongClickListener(pos:Int,treeView: TreeView)
}

/**
 * Class used to put elements of card_view_tree.xml into the recycle view
 * dynamically generates cards based on the data found in FireStore
 * @param treeView orginally calling activity: needed for context
 * @author lukas becker
 */
class RecyclerAdapter(var treeView: TreeView):RecyclerView.Adapter<RecyclerAdapter.ViewHandler>() {
    var trees:List<MarkedTree>? = null
    lateinit var listener: OnItemClickListener

    /**
     * Inner class that handles updating of TextViews and OnClickListeners
     * @author lukas becker
     */
    inner class ViewHandler(itemView:View, var listener: OnItemClickListener,):RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
        val tvTitle:TextView = itemView.findViewById(R.id.card_view_tree_cl_title)
        val tvDetails:TextView = itemView.findViewById(R.id.card_view_tree_cl_details)

        /**
         * @see View.OnClickListener
         */
        override fun onClick(p0: View?) {
            listener.setOnClickListener(adapterPosition,treeView)
        }

        /**
         * @see View.setOnLongClickListener
         */
        override fun onLongClick(p0: View?): Boolean {
            listener.setOnLongClickListener(adapterPosition, treeView)
            return true
        }

    }

    /**
     * creates recycleView initially
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHandler {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_tree,parent,false)
        return ViewHandler(view,listener,)
    }

    /**
     * binds data to the Recycle View elements (= Cards)
     */
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

    /**
     * Counts how many items are in the recycle view
     */
    override fun getItemCount(): Int {
        if (trees == null){
            return 0
        }
        return trees!!.size
    }

    /**
     * sets OnClickListeners
     * @see OnItemClickListener
     */
    fun setOnClickListener(listener: OnItemClickListener){
        this.listener = listener
    }


}



