package com.example.bg_rem.act.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.bg_rem.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class listView extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    static final String[] LIST_MENU = {"Jacket", "Top", "Bottom", "Shoes", "Accs", "Image upload"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Intent myIntent = getIntent(); // gets the previously created intent
        String userId = myIntent.getStringExtra("firstKeyName");
        String addr = myIntent.getStringExtra("server_addr");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 코드 계속 ...

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;
                if (strText.equals("Image upload")) {
                    Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                    intent.putExtra("firstKeyName",userId); // Verify된 경우 userId 다음 액티비티로 전달하기
                    intent.putExtra("server_addr", addr);
//                    intent.putExtra("category_name", strText);
                    startActivity(intent);
                }
                else {
                    Intent intent2 = new Intent(getApplicationContext(), SubActivity2.class);
                    intent2.putExtra("secondKeyName",userId); // Verify된 경우 userId 다음 액티비티로 전달하기
                    intent2.putExtra("server_addr", addr);
                    intent2.putExtra("category_name", strText);
                    startActivity(intent2);
                }

            }
        }) ;
    }
}