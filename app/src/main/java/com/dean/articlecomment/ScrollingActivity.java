package com.dean.articlecomment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dean.articlecomment.xrecycleview.ProgressStyle;
import com.dean.articlecomment.xrecycleview.XRecyclerView;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {
    Context context;
    Animation mHideAnimation,mShowAnimation;
    View bottomContent;
    private XRecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ArrayList<String> listData;
    private int refreshTime = 0;
    private int times = 0;
    public ProgressWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_scrolling);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        bottomContent = findViewById(R.id.bottom_content);
        mHideAnimation =  AnimationUtils.loadAnimation(context, R.anim.hide_to_bottom);
        mShowAnimation =  AnimationUtils.loadAnimation(context, R.anim.show_from_bottom);

        mWebView = (ProgressWebView) this.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new ArticleWebViewClient());
        mWebView.loadUrl("http://7xs3hm.com1.z0.glb.clouddn.com/0425%E7%8E%A9%E8%BD%AC%E6%88%B7%E5%A4%96%E6%8E%A2%E5%AD%90%E7%9A%84%E6%AD%A3%E7%A1%AE%E5%A7%BF%E5%8A%BF.html");

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    if (isHidden()) {
                        playShowAnimation();
                        bottomContent.setVisibility(View.VISIBLE);
                    }

                } else {
                    if (!isHidden()) {
                        playHideAnimation();
                        bottomContent.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
//
//        NestedScrollView textView = (NestedScrollView) findViewById(R.id.scrollView);
//        textView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
////                toolbar.setVisibility(View.VISIBLE);
////                bottomContent.setVisibility(View.VISIBLE);
//                Log.e("onc", "");
//                return false;
//            }
//        });

        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);

//        WebView webView = new WebView(this);
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//        });
//        webView.loadUrl("http://www.baidu.com");
//        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mRecyclerView.addHeaderView(webView);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                refreshTime ++;
//                times = 0;
//                new Handler().postDelayed(new Runnable(){
//                    public void run() {
//
//                        listData.clear();
//                        for(int i = 0; i < 15 ;i++){
//                            listData.add("item" + i + "after " + refreshTime + " times of refresh");
//                        }
//                        mAdapter.notifyDataSetChanged();
//                        mRecyclerView.refreshComplete();
//                    }
//
//                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                if(times < 2){
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            for(int i = 0; i < 15 ;i++){
                                listData.add("item" + (1 + listData.size() ) );
                            }
                            mRecyclerView.loadMoreComplete();
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for(int i = 0; i < 9 ;i++){
                                listData.add("item" + (1 + listData.size() ) );
                            }
                            mRecyclerView.setNoMore(true);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
                times ++;
            }
        });

        listData = new ArrayList<String>();
        for(int i = 0; i < 15 ;i++){
            listData.add("item" + i);
        }
        mAdapter = new MyAdapter(listData);

        mRecyclerView.setAdapter(mAdapter);
    }

    public class ArticleWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            webView.loadUrl(s);
            return true;
        }
    }

    public boolean isHidden() {
        return bottomContent.getVisibility() == View.INVISIBLE;
    }

    void playShowAnimation() {
        if (bottomContent.isShown()) {

        }
        mHideAnimation.cancel();
        bottomContent.startAnimation(mShowAnimation);
    }

    void playHideAnimation() {
        mShowAnimation.cancel();
        bottomContent.startAnimation(mHideAnimation);
    }
}
