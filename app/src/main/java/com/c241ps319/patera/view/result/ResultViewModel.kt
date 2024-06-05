package com.c241ps319.patera.view.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c241ps319.patera.data.remote.response.ArticlesItem
import com.c241ps319.patera.data.remote.retrofit.ApiConfig
import com.c241ps319.patera.data.state.ResultState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ResultViewModel: ViewModel(){
    private val _news = MutableLiveData<ResultState<List<ArticlesItem>>>()
    val news = _news

    init {
        getNews()
    }

    private fun getNews(){
        viewModelScope.launch {
            try {
                _news.value = ResultState.Loading
                val response = ApiConfig.getApiService().getNews()
                if(response.status == "ok") {
                    _news.value = ResultState.Success(response.articles)
                }
            }catch (e: HttpException){
                _news.value = ResultState.Error(e.message())
            }
        }
    }

}