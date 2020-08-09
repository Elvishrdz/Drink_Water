package com.eahm.drinkwaterapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eahm.drinkwaterapp.Adapters.NewsAdapter;
import com.eahm.drinkwaterapp.Models.News;
import com.eahm.drinkwaterapp.R;
import com.eahm.drinkwaterapp.ThisApplication;

import java.util.ArrayList;
import java.util.Collections;

public class NewsFragment extends Fragment {

    private static final String TAG_NEWS = "TAG_NEWS";
    ArrayList<News> newsList = new ArrayList<>();

    TextView textViewNews;
    RecyclerView recyclerViewNews;
    Button buttonNewsContinue;

    RecyclerView.LayoutManager mLayourManager;
    NewsAdapter newsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_news, container, false);
        recyclerViewNews = view.findViewById(R.id.recyclerViewNews);
        buttonNewsContinue = view.findViewById(R.id.buttonNewsContinue);

        newsList = ((ThisApplication) getActivity().getApplication()).getNewsList();

        Log.i(TAG_NEWS, "Mostrar en recycler: " + newsList.size() + " elementos");
        recyclerViewNews.setHasFixedSize(true);
        mLayourManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) mLayourManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewNews.setLayoutManager(mLayourManager);
        newsAdapter = new NewsAdapter(getContext());
        recyclerViewNews.setAdapter(newsAdapter);

        Collections.reverse(newsList);
        newsAdapter.overrideList(newsList);
        newsAdapter.notifyDataSetChanged();

        buttonNewsContinue.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
