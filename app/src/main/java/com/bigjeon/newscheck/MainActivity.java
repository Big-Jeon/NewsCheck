package com.bigjeon.newscheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mbtn_write;
    private RecyclerView recyclerView, KovidView;
    private ArrayList<Category_Item> mItems, SearchData;
    private RecyclerViewAdapter mAdapter;
    private RCVadapter_News SearchAdapter;
    private DBHelper mDBHelper;
    private Button btn_list, btn_issue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int Count_first = 1;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInit();
        //광고
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                Log.d("@@@","onAdLoaded");
            }
        @Override
        public void onAdFailedToLoad(int errorCode) {
            // 광고 로드에 문제가 있을시 출력됩니다.
            Log.d("@@@", "onAdFailedToLoad " + errorCode);
        }
            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }
            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }
            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);

        KovidView = findViewById(R.id.Issue_List);
        SearchData = new ArrayList<>();

        if (SearchAdapter == null){
            SearchAdapter = new RCVadapter_News(SearchData, this);
            KovidView.setHasFixedSize(true);
            KovidView.setAdapter(SearchAdapter);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String[][] Data = new String[3][20];
                    Search_News search_news = new Search_News();
                    Data = search_news.main("[속보]", Integer.toString(Count_first));
                    for (int i = 0; i < 20; i++) {
                        Category_Item News = new Category_Item();
                        News.setTitle(Data[0][i]);
                        News.setURL(Data[1][i]);
                        News.setWriteDate(Data[2][i]);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SearchAdapter.addNews(News);
                                KovidView.smoothScrollToPosition(0);
                            }
                        });
                    }
                }
            });
            thread.start();
        }

        btn_list = findViewById(R.id.List_btn);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_list.setBackground(getDrawable(R.drawable.button_color));
                btn_issue.setBackground(getDrawable(R.drawable.transparent_color));
                recyclerView.setVisibility(View.VISIBLE);
                mbtn_write.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
            }
        });

        btn_issue = findViewById(R.id.Issue_btn);
        btn_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_issue.setBackground(getDrawable(R.drawable.button_color));
                btn_list.setBackground(getDrawable(R.drawable.transparent_color));
                recyclerView.setVisibility(View.INVISIBLE);
                mbtn_write.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SearchAdapter.removeItem();
                Count_first = 1;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[][] Data = new String[3][20];
                        Search_News search_news = new Search_News();
                        Data = search_news.main("[속보]", Integer.toString(Count_first));
                        for (int i = 0; i < 20; i++) {
                            Category_Item News = new Category_Item();
                            News.setTitle(Data[0][i]);
                            News.setURL(Data[1][i]);
                            News.setWriteDate(Data[2][i]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SearchAdapter.addNews(News);
                                    KovidView.smoothScrollToPosition(0);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }
                });
                thread.start();
            }
        });

        KovidView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!KovidView.canScrollVertically(1)){
                    Count_first = Count_first + 20;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[][] Data = new String[3][20];
                            Search_News search_news = new Search_News();
                            Data = search_news.main("[속보]", Integer.toString(Count_first));
                            for (int i = 0; i < 20; i++) {
                                Category_Item News = new Category_Item();
                                News.setTitle(Data[0][i]);
                                News.setURL(Data[1][i]);
                                News.setWriteDate(Data[2][i]);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SearchAdapter.addNews(News);
                                        KovidView.smoothScrollToPosition(0);
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



    private void setInit() {

        mDBHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.Category_List);
        mbtn_write = findViewById(R.id.Add_Category_btn);
        mItems = new ArrayList<>();

        loadRecentDB();

        mbtn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_add_category);
                EditText et_title = dialog.findViewById(R.id.category);
                Button btn_ok = dialog.findViewById(R.id.add_fin_btn);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_title.getText().toString().getBytes().length > 0){
                            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                            mDBHelper.InsertCategory(et_title.getText().toString(), currentTime);

                            Category_Item item = new Category_Item();
                            item.setTitle(et_title.getText().toString());
                            item.setWriteDate(currentTime);

                            mAdapter.addcategory(item);
                            recyclerView.smoothScrollToPosition(0);
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "추가할 키워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void loadRecentDB() {
        mItems = mDBHelper.getCategory();
        if (mAdapter == null){
            mAdapter = new RecyclerViewAdapter(mItems, this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mAdapter);
        }
    }
}

