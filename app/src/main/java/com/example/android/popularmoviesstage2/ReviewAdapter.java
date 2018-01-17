package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Suraz Verma on 1/16/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final ArrayList<ReviewData> reviews;
    private final Context context;
    public ReviewAdapter(Context context, ArrayList<ReviewData> reviewData){
        this.context = context;
        this.reviews = reviewData;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_unit_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String currentAuthorName = reviews.get(position).getAuthor();
        holder.reviewAuthorName.setText(currentAuthorName);
        String currentReviewContent = reviews.get(position).getReviewContent();
        holder.reviewContentBody.setText(currentReviewContent);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView reviewAuthorName ;
        TextView reviewContentBody;
        public ViewHolder(View view){
            super(view);

            reviewAuthorName = view.findViewById(R.id.review_author_name);
            reviewContentBody = view.findViewById(R.id.review_body_text);

        }
    }
}
