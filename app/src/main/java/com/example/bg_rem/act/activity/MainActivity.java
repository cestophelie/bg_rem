package com.example.bg_rem.act.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
//import android.support.v4.content.FileProvider;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bg_rem.R;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    ImageView imgVwSelected_;
    private Button login, makeAccount;
    private EditText checkID, checkPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkID = findViewById(R.id.checkID);
        checkPW = findViewById(R.id.checkPW);

        login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인. Main Activity2 를 호출한다. (갤러리와 이미지 처리 버튼이 나오는 부분이다)
                // text view 내의 값들이 db에 있는 경우
                // Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                // startActivity(intent);
                String userId = checkID.getText().toString();
                String userPass = checkPW.getText().toString();

                checkAccount(userId, userPass);
            }
        });
        makeAccount = findViewById(R.id.btn_register);
        makeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 페이지로 넘어간다.
                Intent intent2 = new Intent(getApplicationContext(), MakeAccount.class);
                startActivity(intent2);
            }

        });
    }
    private void checkAccount(String userId, String userPass) {
        //
        RequestBody checkID = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody checkPW = RequestBody.create(MediaType.parse("text/plain"), userPass);

        // Retrofit 객체 생성
        Retrofit.Builder builder3 = new Retrofit.Builder()
                .baseUrl("https://fa93740d03cc.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit3 = builder3.build();

        MyAPI myAPI3 = retrofit3.create(MyAPI.class);

        // post 한다는 request를 보내는 부분.
        Call<ResponseBody> call = myAPI3.post_check(checkID, checkPW);
        // 만약 서버로 부터 response를 받는다면.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    Log.d(TAG,"계정 확인 완료용!!"+response.toString());
                    Toast.makeText(getApplicationContext(),"계정 확인이용!!!!!",Toast.LENGTH_SHORT).show();

//                     로그인. Main Activity2 를 호출한다. (갤러리와 이미지 처리 버튼이 나오는 부분이다)
//                     text view 내의 값들이 db에 있는 경우
                     Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                     intent.putExtra("firstKeyName",userId); // Verify된 경우 userId 다음 액티비티로 전달하기
                     startActivity(intent);
                }else {
                    Log.d(TAG,"Post Status Code ㅠㅠ : " + response.code());
                    Toast.makeText(getApplicationContext(),"계정 없어용!!!!!",Toast.LENGTH_SHORT).show();
                    Log.d(TAG,response.errorBody().toString());
                    Log.d(TAG,call.request().body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());

            }
        });
    }
}