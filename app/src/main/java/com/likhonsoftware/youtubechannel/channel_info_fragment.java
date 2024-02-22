package com.likhonsoftware.youtubechannel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class channel_info_fragment extends Fragment {
    ImageView channelprofimg;
    TextView channelname, channeldescription;
    Button subscribebtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container!=null){
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.channel_info_fragment, container, false);
        channelprofimg = myview.findViewById(R.id.channelprofimg);
        channelname = myview.findViewById(R.id.channelname);
        channeldescription = myview.findViewById(R.id.channeldescription);
        subscribebtn = myview.findViewById(R.id.subscribebtn);
        String apiKey = getResources().getString(R.string.youtubeapikey);
        String channelId = getResources().getString(R.string.channel_id);
        String subscriptionLink = "https://www.youtube.com/channel/"+channelId+"?sub_confirmation=1";
        String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet&id="+channelId+"&key="+apiKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray items = jsonResponse.getJSONArray("items");
                    JSONObject channel = items.getJSONObject(0);
                    JSONObject snippet = channel.getJSONObject("snippet");

                    // Extracting data
                    String channelName = snippet.getString("title");
                    String channelId = channel.getString("id");
                    String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                    String description = snippet.getString("description");

                    channelname.setText(channelName);
                    channeldescription.setText(description);
                    Picasso.get()
                            .load(""+thumbnailUrl)
                            .into(channelprofimg);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue que = Volley.newRequestQueue(getActivity());
        que.add(stringRequest);
        subscribebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the subscription link
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(subscriptionLink));

                // Display a toast message indicating the action
                Toast.makeText(requireContext(), "Opening YouTube app... subscribe us", Toast.LENGTH_SHORT).show();

                // Open the subscription link
                startActivity(intent);
            }
        });
        return myview ;
    }
}