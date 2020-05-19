package com.ashehata.exooo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    private ArrayList<VideoModel> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set RV
        mRecyclerView = findViewById(R.id.rv_videos);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllVideos();
    }

    private void getAllVideos() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", 0);
            parameters.put("token","cdVkuR1ZTiC2n0ci1O9FeO:APA91bG3MG3BitPeo5CpgJ-bCWtOL6mGGs6V5U9dqwIm65jYwl14TQWc6hp3iKdmbo95sEx98iIN96bm3_L7YrOAjXg1J7Egl6m-5nKLOemraPsbYx4GAiFQ5iXWM35vw3mcBGoh8bgC");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(this, Variables.showAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
            }
        });
    }

    public void Parse_data(String responce){

        data_list = new ArrayList<>();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject itemdata = msgArray.optJSONObject(i);
                    VideoModel item=new VideoModel();
                    item.fb_id=itemdata.optString("fb_id");

                    JSONObject user_info=itemdata.optJSONObject("user_info");

                    item.first_name=user_info.optString("first_name",getResources().getString(R.string.app_name));
                    item.last_name=user_info.optString("last_name","User");
                    item.profile_pic=user_info.optString("profile_pic","null");

                    JSONObject sound_data=itemdata.optJSONObject("sound");
                    item.sound_id=sound_data.optString("id");
                    item.sound_name=sound_data.optString("sound_name");
                    item.sound_pic=sound_data.optString("thum");

                    JSONObject count=itemdata.optJSONObject("count");
                    item.like_count=count.optString("like_count");
                    item.video_comment_count=count.optString("video_comment_count");


                    item.video_id=itemdata.optString("id");
                    item.liked=itemdata.optString("liked");
                    item.video_url=Variables.base_url+itemdata.optString("video");
                    item.video_description=itemdata.optString("description");
                    item.thum=Variables.base_url+itemdata.optString("thum");
                    item.created_date=itemdata.optString("created");

                    data_list.add(item);
                }

                setAdapter();

            }else {
                Toast.makeText(this, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setAdapter() {
        Toast.makeText(this, data_list.size()+ "", Toast.LENGTH_SHORT).show();
        VideoAdapter videoAdapter = new VideoAdapter(getApplication(), data_list);
        mRecyclerView.setAdapter(videoAdapter);

    }


}
