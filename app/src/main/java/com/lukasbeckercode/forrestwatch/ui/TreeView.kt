package com.lukasbeckercode.forrestwatch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lukasbeckercode.forrestwatch.R

class TreeView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_view)
        val layoutManager = LinearLayoutManager(this)
        val recyclerView:RecyclerView = findViewById(R.id.tree_view_recycle_view)
        val adapter = RecyclerAdapter()

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}