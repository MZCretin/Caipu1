package com.cretin.www.caipu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cretin.www.caipu.R;

/**
 * Created by cretin on 16/8/16.
 */
public class AutoRefreshListView extends ListView implements AbsListView.OnScrollListener {
    public static final String NO_MORE = "没有更多数据";
    public static final String LOADING = "正在加载...";
    public static final String LOADING_FAIL = "加载失败";
    public static final String LOADING_MORE = "点击加载更多";
    public static final int NO_MORE_STATE = 0;
    public static final int LOADING_STATE = 1;
    public static final int LOADING_FAIL_STATE = 2;
    private static final int LOADING_COMPLETE_STATE = 3;
    private View footerView;
    private boolean isFooter;
    private ProgressBar footerProgress;
    private TextView tips;
    private int currState = 0;
    private RefreshCallBack callBack;

    public AutoRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public AutoRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi( Build.VERSION_CODES.LOLLIPOP )
    public AutoRefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        handelFooter(context);
        setOnScrollListener(this);
    }

    //处理footerView
    private void handelFooter(Context context) {
        footerView = LayoutInflater.from(context).inflate(R.layout.auto_listview_footer, null);
        addFooterView(footerView);
        footerProgress = ( ProgressBar ) footerView.findViewById(R.id.auto_listview_footer_progress);
        tips = ( TextView ) footerView.findViewById(R.id.auto_listview_footer_tips);
        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( currState != LOADING_STATE && currState != NO_MORE_STATE ) {
                    if ( callBack != null ) {
                        loading();
                        callBack.onRefreshing();
                    }
                }
            }
        });
    }

    //隐藏加载更多
    public void hideAddMoreView() {
        if ( footerView != null )
            removeFooterView(footerView);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if ( lastVisibleItem == totalItemCount && scrollState == SCROLL_STATE_IDLE ) {
            if ( currState != LOADING_STATE && currState != NO_MORE_STATE ) {
                if ( callBack != null ) {
                    loading();
                    callBack.onRefreshing();
                }
            }
        }
        if ( firstVisibleItem == 0 ) {
            View firstVisibleItemView = view.getChildAt(0);
            if ( firstVisibleItemView != null && firstVisibleItemView.getTop() == 0 ) {
                Log.d("ListView", "##### 滚动到顶部 ######");
                if ( scrollToHead != null )
                    scrollToHead.scrolledToHead(true);
            } else {
                if ( scrollToHead != null )
                    scrollToHead.scrolledToHead(false);
            }
        } else {
            if ( scrollToHead != null )
                scrollToHead.scrolledToHead(false);
        }
    }

    private int totalItemCount, lastVisibleItem, firstVisibleItem;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        this.totalItemCount = totalItemCount;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.firstVisibleItem = firstVisibleItem;
    }

    //没有更多数据
    public void noMoreData() {
        if ( footerView != null )
            footerProgress.setVisibility(GONE);
        if ( tips != null )
            tips.setText(NO_MORE);
        currState = NO_MORE_STATE;
    }

    //正在加载
    public void loading() {
        if ( footerProgress != null )
            footerProgress.setVisibility(VISIBLE);
        if ( tips != null )
            tips.setText(LOADING);
        currState = LOADING_STATE;
    }

    //加载完成
    public void loadComplete() {
        currState = LOADING_COMPLETE_STATE;
        if ( footerProgress != null )
            footerProgress.setVisibility(GONE);
        if ( tips != null )
            tips.setText(LOADING_MORE);
    }

    //加载失败
    public void loadingFail() {
        if ( footerView != null )
            footerProgress.setVisibility(GONE);
        if ( tips != null )
            tips.setText(LOADING_FAIL);
        currState = LOADING_FAIL_STATE;
    }

    public interface RefreshCallBack {
        void onRefreshing();
    }

    public RefreshCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(RefreshCallBack callBack) {
        this.callBack = callBack;
    }

    private ScrollToHead scrollToHead;

    public ScrollToHead getScrollToHead() {
        return this.scrollToHead;
    }

    public void setScrollToHead(ScrollToHead scrollToHead) {
        this.scrollToHead = scrollToHead;
    }

    public interface ScrollToHead {
        void scrolledToHead(boolean done);
    }
}
