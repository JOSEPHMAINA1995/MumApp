package com.moringaschool.mumapp.network;

import com.moringaschool.mumapp.models.ArticleResponse;
import com.moringaschool.mumapp.models.SearchArticleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface mumApi {
    @GET("get-allArticles")
    Call<List<ArticleResponse>> getArticle(
    );
    @GET("search-articles")
    Call<List<SearchArticleResponse>> searchArticle(
            @Query("query") String query
    );


}
