package com.example.bg_rem.act.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.bg_rem.R;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    ImageView imgVwSelected_;
    private Button btnImageSend_, btnImageSelection_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnImageSend_ = findViewById(R.id.btnImageSend);
        btnImageSend_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent);

            }
        });
        btnImageSelection_ = findViewById(R.id.btnImageSelection);
        btnImageSelection_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), SubActivity2.class);
                startActivity(intent2);
            }

        });
    }
    /*private final String TAG = getClass().getSimpleName();;
    ImageView imgVwSelected_;
    Button btnImageSend_, btnImageSelection_;
    File tempSelectFile;
    Uri finalUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnImageSend_ = findViewById(R.id.btnImageSend);
        btnImageSend_.setEnabled(false);
        btnImageSend_.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //여기서 upload image 메소드 호출
                uploadImage();
            }
        });
        //
        btnImageSelection_ = findViewById(R.id.btnImageSelection);
        btnImageSelection_.setOnClickListener(new View.OnClickListener() {
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
        btnImageSend_.setEnabled(true);  // 이 부분이 이렇게 바뀌었을 때만 데이터 전송이 이루어 진다.
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
                .baseUrl("https://b0ae21c3bfdc.ngrok.io")
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
    }*/
}