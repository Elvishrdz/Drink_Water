package com.eahm.drinkwaterapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eahm.drinkwaterapp.Models.News;
import com.eahm.drinkwaterapp.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final static String TAG_NEWS_ADAPTER = "TAG_NEWS_ADAPTER";
    ArrayList<News> newsList = new ArrayList<>();
    private Context context;


    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void overrideList(ArrayList<News> value){
        if(value == null || value.size() <= 0) return;
        if(newsList.size() > 0) newsList.clear();
        newsList = value;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RelativeLayout card = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news, viewGroup, false);
        return new NewsAdapter.ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.textViewNewsTitle.setText(newsList.get(position).getTitle());
        viewHolder.textViewNewsContent.setText(newsList.get(position).getContent());

        if(newsList.get(position).getMedia() != null && newsList.get(position).getMedia().size() > 0){
            // Por ahora solo mostramos 1 foto (item 0), en el futuro mejorar esto haciendo un carrusel de items, fotos, videos, etc. foreach(getMedia())
            String imageUrl = newsList.get(position).getMedia().get(0).getCoverImageUrl();
            Log.i(TAG_NEWS_ADAPTER,"Añadiendo imagen: " + imageUrl);
            Glide.with(context)
                    .load(imageUrl)
                    .into(viewHolder.imageViewNews);
        }
        else {
            //Template
            //viewHolder.imageViewNews.setImageResource(R.drawable.news_test);
        }

        String contentUrl = newsList.get(position).getContentUrl();
        if(contentUrl.isEmpty()){
            viewHolder.buttonNewsContent.setVisibility(View.GONE);
        }
        else {
            viewHolder.buttonNewsContent.setOnClickListener(v -> {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(contentUrl)));
            });
        }

        //cuando la vista de noticias sea por items, mostraremos el boton de siguiente. ahora es un recycler view asi q no es necesaria
        viewHolder.buttonNewsNext.setVisibility(View.GONE);

        // dar formato a la fecha 03/05/2019
        viewHolder.textViewNewsDate.setText(newsList.get(position).getPublishedTimestamp());

    }

    @Override
    public int getItemCount() {
        return newsList != null ? newsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutNewsItem;
        TextView textViewNewsTitle;
        TextView textViewNewsContent;
        CardView cardViewContainer;
        ImageView imageViewNews;
        TextView textViewNewsDate;
        Button buttonNewsNext;
        Button buttonNewsContent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutNewsItem = itemView.findViewById(R.id.layoutNewsItem);
            textViewNewsTitle = itemView.findViewById(R.id.textViewNewsTitle);
            textViewNewsContent = itemView.findViewById(R.id.textViewNewsContent);
            cardViewContainer = itemView.findViewById(R.id.cardViewContainer);
            imageViewNews = itemView.findViewById(R.id.imageViewNews);
            textViewNewsDate = itemView.findViewById(R.id.textViewNewsDate);
            buttonNewsNext = itemView.findViewById(R.id.buttonNewsNext);
            buttonNewsContent = itemView.findViewById(R.id.buttonNewsContent);
        }
    }



}
