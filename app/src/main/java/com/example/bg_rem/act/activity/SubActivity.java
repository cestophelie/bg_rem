package com.example.bg_rem.act.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bg_rem.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    ImageView imgVwSelected_;
    private Button ImageSend_, ImageSelection_;
    File tempSelectFile;
    Uri finalUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //
        Intent myIntent = getIntent(); // gets the previously created intent
        String userId = myIntent.getStringExtra("firstKeyName");
        Log.d(TAG,"RECEIVED KEY in SubActivity " + userId);

        ImageSend_ = findViewById(R.id.ImageSend);
        ImageSend_.setEnabled(false);
        ImageSend_.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //여기서 upload image 메소드 호출
                uploadImage();
            }
        });
        //
        ImageSelection_ = findViewById(R.id.ImageSelection);
        ImageSelection_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Intent 를 통해 이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Log.d(TAG,"여기가 먼저? : ");
                startActivityForResult(intent, 1); // activity 값 받는 부분
            }
        });
        imgVwSelected_ = findViewById(R.id.imgVwSelected);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // 이 부분 추가함.

        Log.d(TAG,"HERE : " + requestCode);

        if (requestCode != 1 || resultCode != RESULT_OK) {
            return;
        }
        Uri dataUri = data.getData();
        finalUri = dataUri;
        Log.d(TAG,"URI!! --------- : " + dataUri.toString());
        imgVwSelected_.setImageURI(dataUri); // 밑에 inputstream 파라미터도 바꿈

        try {
            //imageview 에 이미지 출력
            Log.d(TAG,"WORKING!! : ");
            Log.d(TAG,"QUIO!! : " + dataUri.toString());
            InputStream in = getContentResolver().openInputStream(dataUri); // data.getData()
            Bitmap image = BitmapFactory.decodeStream(in);

            imgVwSelected_.setImageBitmap(image); // 이 부분이 실제 이미지 출력되는 코드.
            Log.d(TAG,"WORKING!! : ");

            in.close();
            tempSelectFile = new File(getFilesDir(), "hihi.jpeg");
            OutputStream out = new FileOutputStream(tempSelectFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        ImageSend_.setEnabled(true);  // 이 부분이 이렇게 바뀌었을 때만 데이터 전송이 이루어 진다.
        imgVwSelected_.setImageURI(data.getData());
    }

    private void uploadImage() {
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), "sewoni");

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), tempSelectFile);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("image", tempSelectFile.getName(), requestBody);

        Log.d(TAG,"FILE PATH" + tempSelectFile.toString());
        Log.d(TAG,"FILE NAME" + tempSelectFile.getName());

        // Retrofit 객체를 생성하고 이 객체를 이용해서, API service 를 create 해준다.
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://d016c362b8af.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        MyAPI myAPI = retrofit.create(MyAPI.class);

        // post 한다는 request를 보내는 부분.
        Call<ResponseBody> call = myAPI.post_posts(title, parts);
        // 만약 서버로 부터 response를 받는다면.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"등록 완료");
                    Toast.makeText(getApplicationContext(),"이미지 전송에 성공하였습니다!",Toast.LENGTH_SHORT).show();
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
    }
}