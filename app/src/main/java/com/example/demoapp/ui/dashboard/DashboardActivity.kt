package com.example.demoapp.ui.dashboard

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoapp.R
import com.example.demoapp.components.Initializer
import com.example.demoapp.components.ItemClickListener
import com.example.demoapp.databinding.ActivityDashboardBinding
import com.example.demoapp.extensions.toast
import com.example.demoapp.models.Article
import com.example.demoapp.ui.webview.WebViewActivity

/**
 * Created on 2020-02-24.
 */
class DashboardActivity : AppCompatActivity(), Initializer, ItemClickListener {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var binding: ActivityDashboardBinding
    private var articleListAdapter: ArticleListAdapter = ArticleListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView<ActivityDashboardBinding>(
                this,
                R.layout.activity_dashboard
            );

        initializeUI()
        initializeViewModel()
        subscribeAttributes()
        viewModel.getArticles()
    }

    override fun initializeUI() {
        binding.rvProducts.adapter = articleListAdapter
        binding.rvProducts.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    override fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        binding.viewModel = viewModel
    }

    override fun subscribeAttributes() {
        viewModel.articleList.observe(this, Observer { items ->
            items?.let { items.data?.let { it1 -> populateList(it1.results) } }
        })
        viewModel.errorMessage.observe(this, Observer { error ->
            error?.let { toast(this, error) }
        })
    }

    private fun populateList(items: ArrayList<Article>) {
        articleListAdapter.updateList(items)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }

    override fun onItemClicked(item: Any?) {
        var article = item as Article
        if (article.url.isNullOrEmpty()) {
            toast(this, "No Details are available")
            return
        }

        var intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", article.url)
        intent.putExtra("title", article.title)
        startActivity(intent)
    }
}
