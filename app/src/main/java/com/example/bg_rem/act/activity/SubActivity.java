package com.example.bg_rem.act.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bg_rem.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class SubActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    ImageView imgVwSelected_;
    private Button ImageSend_, ImageSelection_;
    File tempSelectFile;
    private String addr;
//    Uri finalUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //
        Intent myIntent = getIntent(); // gets the previously created intent
        String userId = myIntent.getStringExtra("firstKeyName");
        addr = myIntent.getStringExtra("server_addr");
        Log.d(TAG,"RECEIVED KEY in SubActivity " + userId);
        Log.d(TAG,"SERVER ADDRESS IN SUB1 " + addr);

        ImageSend_ = findViewById(R.id.ImageSend);
        ImageSend_.setEnabled(false);
        ImageSend_.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //여기서 upload image 메소드 호출
                uploadImage(userId);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // 이 부분 추가함.
        Log.d(TAG,"HERE : " + requestCode);
        int imgWidth;
        int imgHeight;
        int value;

        if (requestCode != 1 || resultCode != RESULT_OK) {
            return;
        }
        Uri dataUri = data.getData();
//        finalUri = dataUri;
        Log.d(TAG,"URI!! --------- : " + dataUri.toString());
        imgVwSelected_.setImageURI(dataUri); // 밑에 inputstream 파라미터도 바꿈

        try {
            //imageview 에 이미지 출력
            Log.d(TAG,"WORKING!! : ");
            Log.d(TAG,"QUIO!! : " + dataUri.toString());
//            InputStream in = getContentResolver().openInputStream(dataUri); // data.getData()
//            Bitmap image = BitmapFactory.decodeStream(in);
//            Bitmap newImage = resizeBitmap(image, 300);
//            Bitmap newImage = resize(getApplicationContext(), dataUri, 200);  // 이미지 resize 용도로는 이걸 사용했었음.
            Bitmap finalImg = getBitmapFromGalleryUri(getApplicationContext(), dataUri, 300.);
// -----------------
            imgVwSelected_.setImageBitmap(finalImg); // 이 부분이 실제 이미지 출력되는 코드.
            Log.d(TAG,"WORKING!! : ");

//            in.close();

            tempSelectFile = new File(getFilesDir(), "hihi.jpeg");
            OutputStream out = new FileOutputStream(tempSelectFile);
            finalImg.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        ImageSend_.setEnabled(true);  // 이 부분이 이렇게 바뀌었을 때만 데이터 전송이 이루어 진다.
        imgVwSelected_.setImageURI(data.getData());
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public Bitmap getBitmapFromGalleryUri(Context mContext, Uri uri, Double maxSize)throws IOException {
        int orientation = 0;

        InputStream input = mContext.getContentResolver().openInputStream(uri);
        if (input != null){
            ExifInterface exif = new ExifInterface(input);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            input.close();
        }


        input = mContext.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        try {
            input.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = Math.max(onlyBoundsOptions.outHeight, onlyBoundsOptions.outWidth);

        double ratio = (originalSize > maxSize) ? (originalSize / maxSize) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = mContext.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        try {
            input.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        int rotationInDegrees = exifToDegrees(orientation);

        if (orientation != 0) {
            matrix.preRotate(rotationInDegrees);
        }

        int bmpWidth = 0;
        try {
            bmpWidth = bitmap.getWidth();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Bitmap adjustedBitmap = bitmap;
        if (bmpWidth > 0) {
            adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return adjustedBitmap;

    }
    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    public static int exifToDegrees(int exifOrientation) {
        // 이미지 orientation (회전 여부와 정도 리턴)
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }


    private Bitmap resize(Context context, Uri uri, int resize){
        Bitmap resizeBitmap=null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options); // 1번

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while (true) {//2번
                if (width / 2 < resize || height / 2 < resize)
                    break;
                width /= 2;
                height /= 2;
                samplesize *= 2;

            }

            options.inSampleSize = samplesize;
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options); //3번
            resizeBitmap=bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 이미지 크기 측정 코드 추가
        int width = resizeBitmap.getWidth();
        int height = resizeBitmap.getHeight();
        Log.d(TAG,"New image width" + width);
        Log.d(TAG,"New image height" + height);
        return resizeBitmap;
    }

    private Bitmap resizeBitmap(Bitmap getBitmap, int maxSize) {
        int width = getBitmap.getWidth();
        int height = getBitmap.getHeight();
        double x;

        if (width >= height && width > maxSize) {
            x = width / height;
            width = maxSize;
            height = (int) (maxSize / x);
        } else if (height >= width && height > maxSize) {
            x = height / width;
            height = maxSize;
            width = (int) (maxSize / x);
        }
        Log.d(TAG,"New image width" + width);
        Log.d(TAG,"New image height" + height);
        return Bitmap.createScaledBitmap(getBitmap, width, height, false);
    }

    private void uploadImage(String userId) {
        // 여기서 해당 userId를 같이 전송한다.
        ProgressDialog dialog = new ProgressDialog(SubActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Image processing...");

        dialog.show();

        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), userId);
//
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), tempSelectFile);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("image", tempSelectFile.getName(), requestBody);

        Log.d(TAG,"FILE PATH" + tempSelectFile);
        Log.d(TAG,"FILE NAME" + tempSelectFile.getName());

        // 별도의 Loading activity 를 호출할 시.
//        Intent intent= new Intent(getApplicationContext(), LoadingActivity.class);
//        intent.putExtra("title", userId);
//        intent.putExtra("file_path", tempSelectFile);
//        intent.putExtra("file_name", tempSelectFile.getName());
//        startActivity(intent);

        // timeout setting 해주기
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Retrofit 객체를 생성하고 이 객체를 이용해서, API service 를 create 해준다.
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(addr)
                .client(okHttpClient)
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
                    dialog.dismiss();

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