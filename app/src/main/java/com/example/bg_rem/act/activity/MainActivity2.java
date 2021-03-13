package com.example.bg_rem.act.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.bg_rem.R;

import android.util.Log;
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

        //
        Intent myIntent = getIntent(); // gets the previously created intent
        String userId = myIntent.getStringExtra("firstKeyName");
        Log.d(TAG,"RECEIVED KEY in MainActivity2 " + userId);

        btnImageSend_ = findViewById(R.id.btnImageSend);
        btnImageSend_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                intent.putExtra("firstKeyName", userId); // Verify된 경우 userId 다음 액티비티로 전달하기
                startActivity(intent);

            }
        });
        btnImageSelection_ = findViewById(R.id.btnImageSelection);
        btnImageSelection_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), SubActivity2.class);
                intent2.putExtra("secondKeyName", userId);
                startActivity(intent2);
            }

        });
    }

}