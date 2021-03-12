package com.example.bg_rem.act;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface UploadAPI {  //인터페이스이기 때문에 메소드도 다 abstract
//    @Multipart
    @POST("/posts/")
    Call<String>
    uploadImage(@Body String title);
//    Call<RequestBody> uploadImage (
//            @Part("data_1") RequestBody description_1
//            @Part("data_2") RequestBody description_2,
//            @Part MultipartBody.Part photo
//    );
}
