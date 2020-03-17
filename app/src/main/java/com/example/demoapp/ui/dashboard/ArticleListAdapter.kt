package com.example.demoapp.ui.dashboard

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.components.ItemClickListener
import com.example.demoapp.models.Article

/**
 * Created on 8/5/19.
 */

class ArticleListAdapter(val onItemClickeListener: ItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var articleList: List<Article>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = ArticleViewHolder(onItemClickeListener, parent)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return if (::articleList.isInitialized) articleList.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArticleViewHolder).bind(articleList.get(position))
    }

    fun updateList(articles: List<Article>) {
        articleList = articles
        notifyDataSetChanged()
    }
}