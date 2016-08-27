package com.simplemented.okdelay;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("users/{user}/repos")
    Call<List<Repository>> listRepositories(@Path("user") String user, @Query("sort") String sort);
}
