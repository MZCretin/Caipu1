package com.cretin.www.caipu.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.adapter.CommonAdapter;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.base.BaseFragmentActivity;
import com.cretin.www.caipu.model.ResponseModel;
import com.cretin.www.caipu.net.HttpCallbackModelListener;
import com.cretin.www.caipu.net.HttpUtil;
import com.cretin.www.caipu.ui.MeManager;
import com.cretin.www.caipu.view.AutoRefreshListView;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyDetailsFragment extends BaseFragment {
    public static final String TAG = "ClassifyDetailsFragment";
    @Bind( R.id.listview )
    AutoRefreshListView listview;
    @Bind( R.id.swipe_refresh )
    SwipeRefreshLayout swipeRefresh;
    private ListViewAdapter adapter;
    private List<ResponseModel.ResultBean.DataBean> list;
    private int p = 0;

    public static ClassifyDetailsFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        ClassifyDetailsFragment fragment = new ClassifyDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classify_details;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        setMainTitle(getArguments().getString("title"));
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
        //添加stView的点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, MeManager.class);
                intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, CaipuDetailsFragment.TAG);
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", list.get(position));
                intent.putExtra(BaseFragmentActivity.ARGS, bundle);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        adapter = new ListViewAdapter(mActivity, list, R.layout.item_listview);
        listview.setAdapter(adapter);

        //第一次添加事件
        addData();
    }

    private void addData() {
        String title = getArguments().getString("title");
        if ( title.equals("猜你喜欢") ) {
            title = "甜品";
        }
        try {
            HttpUtil.sendGetModelRequest(mActivity, "http://caipu.yjghost.com/index.php/query/read?menu=" + URLEncoder.encode(title, "UTF-8") + "&rn=15&start=" + (p * 15), new HttpCallbackModelListener<ResponseModel>() {
                @Override
                public void onFinish(ResponseModel response) {
                    listview.loadComplete();
                    swipeRefresh.setRefreshing(false);
                    if ( response != null ) {
                        ResponseModel.ResultBean result = response.getResult();
                        if ( result != null ) {
                            List<ResponseModel.ResultBean.DataBean> data = result.getData();
                            if ( p == 0 ) {
                                list.clear();
                            }
                            if ( data != null ) {
                                list.addAll(data);
                                adapter.notifyDataSetChanged();
                            } else if ( data.isEmpty() ) {
                                listview.noMoreData();
                            }
                            if ( result.getTotalNum() < 15 ) {
                                listview.noMoreData();
                            }
                        }
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            }, ResponseModel.class);
        } catch ( UnsupportedEncodingException e ) {
            e.printStackTrace();
        }
    }

    class ListViewAdapter extends CommonAdapter<ResponseModel.ResultBean.DataBean> {

        public ListViewAdapter(Context context, List<ResponseModel.ResultBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolders holder, ResponseModel.ResultBean.DataBean item, int position) {
            holder.setText(R.id.tv_name, item.getTitle());
            holder.setText(R.id.tv_cailiao, item.getIngredients());
            holder.setText(R.id.tv_des, "       " + item.getImtro());
            Picasso.with(mActivity)
                    .load(item.getAlbums())
                    .into(( ImageView ) holder.getView(R.id.iv_pic));
        }
    }
}
