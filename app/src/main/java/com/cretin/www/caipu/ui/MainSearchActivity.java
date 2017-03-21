package com.cretin.www.caipu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.adapter.CommonAdapter;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseActivity;
import com.cretin.www.caipu.base.BaseFragmentActivity;
import com.cretin.www.caipu.fragment.CaipuDetailsFragment;
import com.cretin.www.caipu.model.ResponseModel;
import com.cretin.www.caipu.net.HttpCallbackModelListener;
import com.cretin.www.caipu.net.HttpUtil;
import com.cretin.www.caipu.utils.UiUtils;
import com.cretin.www.caipu.utils.ViewUtils;
import com.cretin.www.caipu.view.AutoRefreshListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class MainSearchActivity extends BaseActivity {
    @Bind( R.id.ed_search )
    EditText edSearch;
    @Bind( R.id.tv_cancel )
    TextView tvCancel;
    @Bind( R.id.ll_head )
    LinearLayout llHead;
    @Bind( R.id.listview )
    AutoRefreshListView listview;
    @Bind( R.id.swipe_refresh )
    SwipeRefreshLayout swipeRefresh;
    private List<ResponseModel.ResultBean.DataBean> list;
    private ListViewAdapter adapter;
    private int p = 0;

    @Override
    protected void initData() {
        list = new ArrayList<>();
        adapter = new ListViewAdapter(this, list, R.layout.item_listview);
        listview.setAdapter(adapter);
        //为下拉刷新设置变换颜色
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //设置下拉刷新事件
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 0;
                addData(edSearch.getText().toString());
            }
        });
        //添加底部加载更多事件
        listview.setCallBack(new AutoRefreshListView.RefreshCallBack() {
            @Override
            public void onRefreshing() {
                p++;
                addData(edSearch.getText().toString());
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, MeManager.class);
                intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, CaipuDetailsFragment.TAG);
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", list.get(position));
                intent.putExtra(BaseFragmentActivity.ARGS, bundle);
                startActivity(intent);
            }
        });

        //为edittet添加监听事件
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( TextUtils.isEmpty(s.toString()) ) {
                    tvCancel.setText("取消");
                    swipeRefresh.setVisibility(View.GONE);
                } else {
                    tvCancel.setText("搜索");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //请求数据
    private void addData(String title) {
        HttpUtil.sendGetModelRequest(mActivity, "http://caipu.yjghost.com/index.php/query/read?menu=" + title + "&rn=15&start=" + (p * 15), new HttpCallbackModelListener<ResponseModel>() {
            @Override
            public void onFinish(ResponseModel response) {
                listview.loadComplete();
                swipeRefresh.setRefreshing(false);
                stopDialog();
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
                        if ( !list.isEmpty() ) {
                            swipeRefresh.setVisibility(View.VISIBLE);
                        } else {
                            UiUtils.showToastInAnyThread("没有搜索到数据");
                        }
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }, ResponseModel.class);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_search;
    }


    @Override
    protected void initView(View view) {
        if ( view != null ) {
            hidTitleView();
            if ( isKitkat ) {
                llHead.setPadding(0, ViewUtils.getStatusBarHeights() + 20, 0, 0);
            }
            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if ( TextUtils.isEmpty(s.toString()) ) {
                        tvCancel.setText("取消");
                        listview.setVisibility(View.GONE);
                    } else {
                        tvCancel.setText("搜索");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_bottom_out);
    }


    @OnClick( R.id.tv_cancel )
    public void onClick() {
        String tag = tvCancel.getText().toString();
        if ( tag.equals("取消") ) {
            finish();
        } else if ( tag.equals("搜索") ) {
            tvCancel.setText("取消");
            showDialog("请稍等...");
            String searchContent = edSearch.getText().toString();
            if ( !TextUtils.isEmpty(searchContent) ) {
                addData(searchContent);
            }
        }
    }
}
