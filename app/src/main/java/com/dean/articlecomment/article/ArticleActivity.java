package com.dean.articlecomment.article;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dean.articlecomment.R;
import com.dean.articlecomment.ui.XAppBarLayout;
import com.dean.articlecomment.ui.XNestedScrollView;
import com.dean.articlecomment.base.BaseActivity;
import com.dean.articlecomment.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by DeanGuo on 9/1/16.
 */
public class ArticleActivity extends BaseActivity<ArticlePresenter> implements ArticleContract.View, XAppBarLayout.XAppBarListener, XNestedScrollView.XNestedScrollViewListener {
    Animation mHideAnimation, mShowAnimation;

    @BindView(R.id.bottom_content)
    View bottomContent;

    @BindView(R.id.scrollView)
    XNestedScrollView nestedScrollView;

    @BindView(R.id.app_bar)
    XAppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.comment_btn)
    View commentBtn;

    @BindView(R.id.go_comment_btn)
    View goCommentBtn;

    @BindView(R.id.comment_content_view)
    View commentView;

    @BindView(R.id.article_content_view)
    View articleView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scrolling;
    }

    @Override
    protected void initEventAndData() {
        // webView fragment
        ArticleDetailFragment mArticleFragment = ArticleDetailFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mArticleFragment, R.id.article_content_view);

        // comment fragment
        ArticleCommentFragment mCommentFragment = ArticleCommentFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mCommentFragment, R.id.comment_content_view);

        appBarLayout.setXAppBarListener(this);

        nestedScrollView.setXNestedScrollViewListener(this);

        initBottomContent();
        DaggerArticleComponent.builder().articlePresenterModule(new ArticlePresenterModule(mArticleFragment, mCommentFragment, this)).build().inject(this);
    }

    private void initBottomContent() {
        mHideAnimation = AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom);
        mShowAnimation = AnimationUtils.loadAnimation(this, R.anim.show_from_bottom);

        toolbar.setOnClickListener(view -> goToArticle());

        commentBtn.setOnClickListener(view -> mPresenter.addComment());

        goCommentBtn.setOnClickListener(view -> goToComment());
    }

    @Override
    public void onFingerUp() {
        mPresenter.showBottomView();
    }

    @Override
    public void onFingerDown() {
        mPresenter.hideBottomView();
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

    @Override
    public void onScrollToPageEnd() {
        mPresenter.commentView.onScrollToPageEnd();
    }

    @Override
    public void showBottomView() {
        if (!isHidden()) {
            playHideAnimation();
            bottomContent.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void hideBottomView() {
        if (isHidden()) {
            playShowAnimation();
            bottomContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void goToComment() {
        nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, commentView.getTop()));
    }

    @Override
    public void goToArticle() {
        nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, articleView.getTop()));
    }

    @Override
    public void setPresenter(ArticleContract.Presenter presenter) {
        mPresenter = (ArticlePresenter) presenter;
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
