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
import com.cretin.www.caipu.model.LikeModel;
import com.cretin.www.caipu.model.ResponseModel;
import com.cretin.www.caipu.ui.MeManager;
import com.cretin.www.caipu.utils.UiUtils;
import com.cretin.www.caipu.view.AutoRefreshListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyLoveFragment extends BaseFragment {
    public static final String TAG = "MyLoveFragment";
    @Bind( R.id.listview )
    AutoRefreshListView listview;
    @Bind( R.id.swipe_refresh )
    SwipeRefreshLayout swipeRefresh;
    private int p = 0;
    private ListViewAdapter adapter;
    private List<LikeModel> list;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_love;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        setMainTitle("我的收藏");
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        adapter = new ListViewAdapter(mActivity, list, R.layout.item_listview);
        listview.setAdapter(adapter);

        showDialog("请稍后...");
        addData();

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
                handlerData(position);
            }
        });
    }

    private void handlerData(int position) {
        LikeModel likeModel = list.get(position);
        ResponseModel.ResultBean.DataBean responseModel = new ResponseModel.ResultBean.DataBean();
        responseModel.setId(likeModel.getId());
        responseModel.setCaipu_id(likeModel.getCaipu_id());
        responseModel.setTitle(likeModel.getTitle());
        responseModel.setTags(likeModel.getTags());
        responseModel.setImtro(likeModel.getImtro());
        responseModel.setIngredients(likeModel.getIngredients());
        responseModel.setBurden(likeModel.getBurden());
        responseModel.setAlbums(likeModel.getAlbums());
        responseModel.setPassed(likeModel.getPassed());
        responseModel.setUser_upload(likeModel.getUser_upload());
        List<ResponseModel.ResultBean.DataBean.StepsBean> list = new ArrayList<>();
        for ( LikeModel.StepsBean bean :
                likeModel.getSteps() ) {
            list.add(new
                    ResponseModel.ResultBean.DataBean.StepsBean(bean.getStep(), bean.getImg()));
        }
        responseModel.setSteps(list);

        Intent intent = new Intent(mActivity, MeManager.class);
        intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, CaipuDetailsFragment.TAG);
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", responseModel);
        intent.putExtra(BaseFragmentActivity.ARGS, bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        p = 0;
        addData();
    }

    private void addData() {

        BmobQuery<LikeModel> query = new BmobQuery<>();
        //userId
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(10);
        query.setSkip(10 * p); // 忽略前10条数据（即第一页数据结果）
        //执行查询方法
        query.findObjects(new FindListener<LikeModel>() {
            @Override
            public void done(List<LikeModel> object, BmobException e) {
                stopDialog();
                listview.loadComplete();
                swipeRefresh.setRefreshing(false);
                if ( e == null ) {
                    if ( p == 0 )
                        list.clear();
                    if ( object != null && !object.isEmpty() ) {
                        list.addAll(object);
                        if ( object.size() < 10 ) {
                            listview.noMoreData();
                        }
                    } else {
                        listview.noMoreData();
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    UiUtils.showToastInAnyThread("网络异常");
                }
            }
        });
    }

    class ListViewAdapter extends CommonAdapter<LikeModel> {

        public ListViewAdapter(Context context, List<LikeModel> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolders holder, LikeModel item, int position) {
            holder.setText(R.id.tv_name, item.getTitle());
            holder.setText(R.id.tv_cailiao, item.getIngredients());
            holder.setText(R.id.tv_des, "       " + item.getImtro());
            Picasso.with(mActivity)
                    .load(item.getAlbums())
                    .into(( ImageView ) holder.getView(R.id.iv_pic));
        }
    }
}
