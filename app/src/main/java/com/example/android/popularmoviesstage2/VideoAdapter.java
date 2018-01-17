package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Suraz Verma on 1/14/2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private final ArrayList<VideoData> trailerData;
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private final Context context;

    public VideoAdapter(Context context, ArrayList<VideoData> trailerData)
    {
        this.context = context;
        this.trailerData = trailerData;
    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.trailer_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {

       String titleName = trailerData.get(position).getVideoName();
        holder.trailerTitle.setText(titleName);
        final int currentPosition = position;
        holder.trailerTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              String videoKey = trailerData.get(currentPosition).getVideoKey();
                String finalURL = YOUTUBE_URL +videoKey;
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalURL));
                context.startActivity(youtubeIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView trailerTitle ;
        public ViewHolder(View view){
            super(view);

                trailerTitle = view.findViewById(R.id.video_title_text);

        }
    }

}
