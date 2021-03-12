package com.example.bg_rem.act.activity;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
// here attached
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import okhttp3.MultipartBody;

// API를 정의하는 instance 만드는 과정
public interface MyAPI{
    // Retrofit Interface 구성하기
    // Call 을 통해서 웹서버에 요청을 보낼 수 있다.
    // 1. Http요청을 어노테이션으로 명시
    // 2. URL Parameter와 Query Parameter 사용이 가능하다.
    // ex) @GET("/group/{id}/users") Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort)
    //3. 객체는 Body 로 json형태로 전달한다.
    //url 끝에 / 를 빼먹으면 error 가 발생할 수 있으니 유의바란다.

    // meaning we need to annotate every parameter with @Part annotaion

    @Multipart
    @POST("/posts/")
    Call<ResponseBody> post_posts(
            @Part("title") RequestBody param,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("/accounts/")
    Call<ResponseBody> post_accounts(
            @Part("identify") RequestBody param1,
            @Part("password") RequestBody param2
    );

    @Multipart
    @POST("/check/")
    Call<ResponseBody> post_check(
            @Part("checkID") RequestBody param1,
            @Part("checkPW") RequestBody param2
    );

    @PATCH("/posts/{pk}/")
    Call<PostItem> patch_posts(@Path("pk") int pk, @Body PostItem post);

    @DELETE("/posts/{pk}/")
    Call<PostItem> delete_posts(@Path("pk") int pk);

    @GET("/posts/")
    Call<List<PostItem>> get_posts();

    @GET("/posts/{pk}/")
    Call<PostItem> get_post_pk(@Path("pk") int pk);
}