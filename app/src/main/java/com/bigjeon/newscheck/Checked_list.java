package com.bigjeon.newscheck;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class Checked_list extends AppCompatActivity {

    private ArrayList<Category_Item> NewsDataList;
    private RCVadapter_News recyclerViewAdapter;
    private RecyclerView News_list;
    private String keyword;
    private TextView Keyword;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int Count_first = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checked_newslist);
        Keyword = (TextView) findViewById(R.id.Keyword);
        News_list = findViewById(R.id.Checked_listView);
        NewsDataList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.Swipe_layout);
        Intent intent = getIntent();
        keyword = intent.getExtras().getString("keyword");
        Keyword.setText(keyword);

        if (recyclerViewAdapter == null) {
            recyclerViewAdapter = new RCVadapter_News(NewsDataList, this);
            News_list.setHasFixedSize(true);
            News_list.setAdapter(recyclerViewAdapter);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String[][] Data = new String[3][20];
                    Search_News search_news = new Search_News();
                    Data = search_news.main(keyword, Integer.toString(Count_first));
                        for (int i = 0; i < 20; i++) {
                            Category_Item News = new Category_Item();
                            News.setTitle(Data[0][i]);
                            News.setURL(Data[1][i]);
                            News.setWriteDate(Data[2][i]);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerViewAdapter.addNews(News);
                                        News_list.smoothScrollToPosition(0);
                                    }
                                });
                    }
                }
            });
            thread.start();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerViewAdapter.removeItem();
                Count_first = 1;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[][] Data = new String[3][20];
                        Search_News search_news = new Search_News();
                        Data = search_news.main(keyword, Integer.toString(Count_first));
                        for (int i = 0; i < 20; i++) {
                            Category_Item News = new Category_Item();
                            News.setTitle(Data[0][i]);
                            News.setURL(Data[1][i]);
                            News.setWriteDate(Data[2][i]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerViewAdapter.addNews(News);
                                    News_list.smoothScrollToPosition(0);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }
                });
                thread.start();
            }
        });

        News_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!News_list.canScrollVertically(1)){
                    Count_first = Count_first + 20;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[][] Data = new String[3][20];
                            Search_News search_news = new Search_News();
                            Data = search_news.main(keyword, Integer.toString(Count_first));
                            for (int i = 0; i < 20; i++) {
                                Category_Item News = new Category_Item();
                                News.setTitle(Data[0][i]);
                                News.setURL(Data[1][i]);
                                News.setWriteDate(Data[2][i]);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerViewAdapter.addNews(News);
                                        News_list.smoothScrollToPosition(0);
                                    }
                                });
                            }
                        }
                    });
                    thread.start();
                }
            }
        });
    }
}