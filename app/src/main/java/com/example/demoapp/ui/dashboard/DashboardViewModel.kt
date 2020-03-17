package com.example.demoapp.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demoapp.components.CoroutineModule
import com.example.demoapp.components.LiveDataResult
import com.example.demoapp.constants.EnvironmentConstants
import com.example.demoapp.models.Article
import com.example.demoapp.services.network.ServiceFactory
import com.example.demoapp.services.network.responses.ArticleResponse
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created on 2020-02-24.
 */
open class DashboardViewModel() : ViewModel(), CoroutineModule {

    var isLoading: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val articleList = MutableLiveData<LiveDataResult<ArticleResponse>>()

    val name = MutableLiveData<String>()
    val byLine = MutableLiveData<String>()
    val publishedDate = MutableLiveData<String>()

    init {
        isLoading.value = 8
    }

    fun getArticles() {
        launch {
            this.setLoadingVisibility(true)
            withContext(Dispatchers.IO) {
                ServiceFactory.nyService.getMostViewed(EnvironmentConstants.PATH_ALL_SECTIONS, EnvironmentConstants.PATH_PERIOD_SEVEN).subscribe(GetArticlesObserver())
            }
        }
    }

    fun setLoadingVisibility(visible: Boolean) {
        var visibility = if (visible) 0 else 8
        isLoading.postValue(visibility)
    }

    fun bind(article: Article) {
        name.value = article.title
        byLine.value = article.getByLine()
        publishedDate.value = article.published_date
    }

    /**
     * Service call Observer
     */
    inner class GetArticlesObserver : MaybeObserver<ArticleResponse> {
        override fun onSubscribe(d: Disposable) {
            this@DashboardViewModel.articleList.postValue(LiveDataResult.loading())
        }

        override fun onError(e: Throwable) {
            this@DashboardViewModel.articleList.postValue(LiveDataResult.error(e))
            this@DashboardViewModel.setLoadingVisibility(false)
        }

        override fun onSuccess(t: ArticleResponse) {
            this@DashboardViewModel.articleList.postValue(LiveDataResult.succes(t))
            this@DashboardViewModel.setLoadingVisibility(false)
        }

        override fun onComplete() {
            this@DashboardViewModel.setLoadingVisibility(false)
        }

    }
}