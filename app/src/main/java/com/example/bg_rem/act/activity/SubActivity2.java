package com.example.bg_rem.act.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    // 여기 호스트 주소를 aws 주소로 바꿔줘야 한다.
    // http://ec2-13-209-76-218.ap-northeast-2.compute.amazonaws.com:8080
    private static final String endpoint = "https://fa93740d03cc.ngrok.io/posts/?format=json";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"WORKING 11111");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);

        Intent myIntent = getIntent(); // gets the previously created intent
        String userId = myIntent.getStringExtra("secondKeyName");
        Log.d(TAG,"RECEIVED KEY in SubActivity2 " + userId);

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

        fetchImages(userId);
    }

    private void fetchImages(String userId) {
        // 서버로부터 json fetch
        pDialog.setMessage("Getting images...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Received JSON from server" + response.toString());
                        pDialog.hide();

                        images.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();
                                // 여기에 if id가 일치하면, 코드를 넣어줄 수 있을 듯.
                                if (object.getString("title").equals(userId)) {
                                    image.setName(object.getString("title"));
                                    image.setImage(object.getString("image"));

                                    images.add(image);
                                }


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
}