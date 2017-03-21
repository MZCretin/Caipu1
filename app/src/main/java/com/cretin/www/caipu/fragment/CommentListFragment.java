package com.cretin.www.caipu.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.adapter.CommonAdapter;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.model.CommentModel;
import com.cretin.www.caipu.utils.UiUtils;
import com.cretin.www.caipu.view.AutoRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentListFragment extends BaseFragment {
    public static final String TAG = "CommentListFragment";
    @Bind( R.id.listview )
    AutoRefreshListView listview;
    @Bind( R.id.swipe_refresh )
    SwipeRefreshLayout swipeRefresh;
    private ListViewAdapter adapter;
    private List<CommentModel> list;
    private int p = 0;

    public static CommentListFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id",id);
        CommentListFragment fragment = new CommentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment_list;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        setMainTitle("所有评论");

        //为下拉刷新设置变换颜色
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //设置下拉刷新事件
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 0;
                addData();
            }
        });
        //添加底部加载更多事件
        listview.setCallBack(new AutoRefreshListView.RefreshCallBack() {
            @Override
            public void onRefreshing() {
                p++;
                addData();
            }
        });
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        adapter = new ListViewAdapter(mActivity, list, R.layout.item_comment_list);
        listview.setAdapter(adapter);

        //第一次添加事件
        addData();
    }

    private void addData() {
        BmobQuery<CommentModel> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("caipuId", getArguments().getString("id"));
        //返回3条数据，如果不加上这条语句，默认返回10条数据
        query1.setLimit(10);
        query1.setSkip(p * 10);
        //按时间先后查询
        query1.order("-createdAt");
        // 希望在查询信息的同时也把发布人的信息查询出来
        query1.include("user");
        //执行查询方法
        query1.findObjects(new FindListener<CommentModel>() {
            @Override
            public void done(List<CommentModel> object, BmobException e) {
                listview.loadComplete();
                swipeRefresh.setRefreshing(false);
                if ( e == null ) {
                    if ( p == 0 ) {
                        list.clear();
                    }
                    if ( object != null && !object.isEmpty() ) {
                        list.addAll(object);
                        if ( object.size() < 10 ) {
                            listview.noMoreData();
                        }
                    } else {
                        listview.noMoreData();
                    }
                } else {
                    UiUtils.showToastInAnyThread("网络异常");
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    class ListViewAdapter extends CommonAdapter<CommentModel> {

        public ListViewAdapter(Context context, List<CommentModel> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolders holder, CommentModel item, int position) {
            String name = item.getUser().getNick();
            if ( TextUtils.isEmpty(name) )
                name = item.getUser().getUsername();
            holder.setText(R.id.tv_username,name);
            holder.setText(R.id.tv_time,item.getCreatedAt().toString());
            holder.setText(R.id.tv_content,item.getContent());
            holder.getView(R.id.divider).setVisibility(View.INVISIBLE);
        }
    }
}
