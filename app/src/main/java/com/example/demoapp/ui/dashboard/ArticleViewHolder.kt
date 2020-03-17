package com.example.demoapp.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.R
import com.example.demoapp.components.ItemClickListener
import com.example.demoapp.databinding.ItemArticleBinding
import com.example.demoapp.models.Article

/**
 * Created on 8/7/19.
 */
class ArticleViewHolder(
    val clickListener: ItemClickListener,
    private val parent: ViewGroup,
    private val binding: ItemArticleBinding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.item_article,
        parent,
        false
    )
) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = DashboardViewModel()

    fun bind(product: Article) {
        itemView.setOnClickListener {
            clickListener.onItemClicked(product)
        }
        viewModel.bind(product)
        binding.viewModel = viewModel
    }
}