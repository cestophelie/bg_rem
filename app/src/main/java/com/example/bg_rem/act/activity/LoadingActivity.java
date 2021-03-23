package com.example.bg_rem.act.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bg_rem.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadingActivity extends AppCompatActivity implements Serializable {
//    public static Activity activity = null;
    File tempSelectFile;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent newIntent = getIntent();
        String title = newIntent.getStringExtra("title");
//        String file_path = newIntent.getExtra("file_path");
        File file_path = (File)newIntent.getExtras().get("file_path");
        String file_name = newIntent.getStringExtra("file_name");

        RequestBody title_ = RequestBody.create(MediaType.parse("text/plain"), "new");

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file_path);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("image", file_name, requestBody);
        Log.d(TAG,"FILE PATH" + file_path.toString());
        Log.d(TAG,"FILE NAME" + file_name);

        // timeout setting 해주기
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Retrofit 객체를 생성하고 이 객체를 이용해서, API service 를 create 해준다.
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://fa93740d03cc.ngrok.io")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        MyAPI myAPI = retrofit.create(MyAPI.class);

        // post 한다는 request를 보내는 부분.
        Call<ResponseBody> call = myAPI.post_posts(title_, parts);
        // 만약 서버로 부터 response를 받는다면.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"등록 완료");
                    Toast.makeText(getApplicationContext(),"이미지 전송에 성공하였습니다!",Toast.LENGTH_SHORT).show();
                    finish();
//                    onBackPressed();
                }else {
                    Log.d(TAG,"Post Status Code : " + response.code());
                    Log.d(TAG,response.errorBody().toString());
                    Log.d(TAG,call.request().body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());

            }
        });

//        Intent myIntent = getIntent(); // gets the previously created intent
//        Bundle bundle_ = getIntent().getExtras();
//        Call<ResponseBody> call = bundle_.getParcelable("parse");
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(response.isSuccessful()){
//                    Log.d(TAG,"등록 완료");
//                    Toast.makeText(getApplicationContext(),"이미지 전송에 성공하였습니다!",Toast.LENGTH_SHORT).show();
////                    finish(intent);
////                    onBackPressed();
//                }else {
//                    Log.d(TAG,"Post Status Code : " + response.code());
//                    Log.d(TAG,response.errorBody().toString());
//                    Log.d(TAG,call.request().body().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG,"Fail msg : " + t.getMessage());
//
//            }
//        });
//        Call<ResponseBody> call2 = (Call<ResponseBody>) myIntent.getExtras().getSerializable("parse");
//        String userId = myIntent.getStringExtra("new");
//        tempSelectFile = myIntent.getOutputMediaFile("thing");

//        activity = this;
    }
}