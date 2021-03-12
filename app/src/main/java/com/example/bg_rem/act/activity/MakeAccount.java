package com.example.bg_rem.act.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

//is it working? nope..
public class MakeAccount extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private EditText identify;
    private EditText password;
    private Button submission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_account);

        identify = findViewById(R.id.identify);
        password = findViewById(R.id.password);

        submission = findViewById(R.id.btn_register);
        submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText 내의 값들을 전송한다.
                String userId = identify.getText().toString();
                String userPass = password.getText().toString();

                registerAccount(userId, userPass);

            }
        });

    }
    private void registerAccount(String userId, String userPass) {
        RequestBody identify = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), userPass);


        // Retrofit 객체 생성
        Retrofit.Builder builder2 = new Retrofit.Builder()
                .baseUrl("https://74af883dddb7.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit2 = builder2.build();

        MyAPI myAPI2 = retrofit2.create(MyAPI.class);

        // post 한다는 request를 보내는 부분.
        Call<ResponseBody> call = myAPI2.post_accounts(identify, password);
        // 만약 서버로 부터 response를 받는다면.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"계정 완벽 등록!!");
                    Toast.makeText(getApplicationContext(),"계정 등록에 성공하였습니다!",Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG,"Post Status Code ㅠㅠ : " + response.code());
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