package com.example.bg_rem.act.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bg_rem.R;

import android.app.ProgressDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import .R;
import com.example.bg_rem.act.adapter.GalleryAdapter;
import com.example.bg_rem.act.app.AppController;
import com.example.bg_rem.act.model.Image;


public class SubActivity2 extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "https://d016c362b8af.ngrok.io/posts/?format=json";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"WORKING 11111");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG,"WORKING 11111");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchImages();
    }

    private void fetchImages() {

        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "sewoni" + response.toString());
                        pDialog.hide();

                        images.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();
                                // 여기에 if id가 일치하면, 코드를 넣어줄 수 있을 듯.
                                image.setName(object.getString("title"));
                                image.setImage(object.getString("image"));

                                images.add(image);
                                // 여기까지

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
    /*private final String TAG = getClass().getSimpleName();
    private Button button_;
    private ImageView imageView_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);

        button_ = findViewById(R.id.button1);
        button_.setOnClickListener(new View.OnClickListener() {
        // 버튼 클릭 시, Glide 라이브러리를 사용해 ImageView 에 띄우기
            @Override
            public void onClick(View view) {
                Log.d(TAG,"WORKING 11111");
                imageView_ = findViewById(R.id.imageView);
                Glide.with(view).load("https://08788d393d62.ngrok.io/media/fwqyk.png").into(imageView_);
                Log.d(TAG,"WORKING 22222");
            }
        });
    }*/
}