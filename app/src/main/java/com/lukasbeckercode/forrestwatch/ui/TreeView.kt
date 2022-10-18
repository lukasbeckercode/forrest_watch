package com.lukasbeckercode.forrestwatch.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.database.CloudFireStore
import com.lukasbeckercode.forrestwatch.models.MarkedTree

class TreeView : AppCompatActivity() {
    private val adapter = RecyclerAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_view)
        CloudFireStore().getMarkedTrees(this)

        adapter.setOnClickListener(object: OnItemClickListener{
            override fun setOnClickListener(pos: Int,treeView: TreeView) {
                Toast.makeText(treeView,"IMPLEMENT ME!!!", Toast.LENGTH_SHORT).show()
            }

            override fun setOnLongClickListener(pos: Int,treeView: TreeView) {
                AlertDialog.Builder(treeView)
                    .setTitle(treeView.getString(R.string.tree_view_delete_title))
                    .setMessage(treeView.getString(R.string.tree_view_delete_content))
                    .setCancelable(false)
                    .setPositiveButton(treeView.getString(R.string.tree_view_delete_confirm)) { _, _ ->
                        CloudFireStore().deleteTree(adapter.trees!![pos].id!!,treeView)
                    }
                    .show()
            }

        })


    }

    fun success(results:List<MarkedTree>){
        adapter.trees = results

        val layoutManager = LinearLayoutManager(this)
        val recyclerView:RecyclerView = findViewById(R.id.tree_view_recycle_view)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}