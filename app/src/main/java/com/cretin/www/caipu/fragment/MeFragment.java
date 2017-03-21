package com.cretin.www.caipu.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.app.LocalStorageKeys;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.base.BaseFragmentActivity;
import com.cretin.www.caipu.eventbus.NotifyRegisterOrLoginSuccess;
import com.cretin.www.caipu.model.UserModel;
import com.cretin.www.caipu.ui.MeManager;
import com.cretin.www.caipu.utils.DataCleanManager;
import com.cretin.www.caipu.utils.KV;
import com.cretin.www.caipu.utils.MyAlertDialog;
import com.cretin.www.caipu.utils.UiUtils;
import com.cretin.www.caipu.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {
    public static final String TAG = "MeFragment";
    @Bind( R.id.iv_portrait )
    CircleImageView ivPortrait;
    @Bind( R.id.tv_name )
    TextView tvName;
    @Bind( R.id.tv_username )
    TextView tvUsername;
    @Bind( R.id.iv_arrow )
    ImageView ivArrow;
    @Bind( R.id.rl_me )
    RelativeLayout rlMe;
    @Bind( R.id.textView )
    TextView textView;
    @Bind( R.id.ll_cainixihuan )
    LinearLayout llCainixihuan;
    @Bind( R.id.textView3 )
    TextView textView3;
    @Bind( R.id.tv_invite )
    TextView tvInvite;
    @Bind( R.id.ll_guanyuwomen )
    LinearLayout llGuanyuwomen;
    @Bind( R.id.textView2 )
    TextView textView2;
    @Bind( R.id.tv_zizhi )
    TextView tvZizhi;
    @Bind( R.id.ll_wodeshoucang )
    LinearLayout llWodeshoucang;
    @Bind( R.id.tv_cache )
    TextView tvCache;
    @Bind( R.id.ll_clearcache )
    LinearLayout llClearcache;
    @Bind( R.id.tv_yonghuxieyi )
    TextView tvYonghuxieyi;
    @Bind( R.id.tv_forget_psw )
    TextView tvForgetPsw;
    @Bind( R.id.tv_exit )
    TextView tvExit;
    private UserModel mUserModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        hidBackBtn();
        setMainTitle("更多");
    }

    @Override
    protected void initData() {
        mUserModel = KV.get(LocalStorageKeys.USER_INFO);
        if ( mUserModel == null ) {
            //未登录
            tvName.setText("登录/注册");
            tvUsername.setVisibility(View.GONE);
            tvForgetPsw.setVisibility(View.GONE);
            tvExit.setVisibility(View.GONE);
        } else {
            tvUsername.setText("账号：" + mUserModel.getUsername());
            String nick = mUserModel.getNick();
            if ( TextUtils.isEmpty(nick) )
                nick = "没有昵称的用户";
            tvName.setText(nick);
            tvForgetPsw.setVisibility(View.VISIBLE);
            tvExit.setVisibility(View.VISIBLE);
            tvUsername.setVisibility(View.VISIBLE);
        }

        try {
            //获取缓存大小
            String ss = DataCleanManager.getTotalCacheSize(getActivity());
            tvCache.setText(ss);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @OnClick( {R.id.rl_me, R.id.ll_cainixihuan, R.id.ll_guanyuwomen, R.id.ll_wodeshoucang,
            R.id.ll_clearcache, R.id.tv_yonghuxieyi, R.id.tv_forget_psw, R.id.tv_exit} )
    public void onClick(View view) {
        Intent intent = new Intent(mActivity, MeManager.class);
        switch ( view.getId() ) {
            case R.id.rl_me:
                if ( mUserModel == null ) {
                    intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, LoginRegisterFragment.TAG);
                } else {
                    intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, UserInfoFragment.TAG);
                }
                break;
            case R.id.ll_cainixihuan:
                intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, ClassifyDetailsFragment.TAG);
                Bundle bundle = new Bundle();
                bundle.putString("title", "猜你喜欢");
                intent.putExtra(BaseFragmentActivity.ARGS, bundle);
                break;
            case R.id.ll_guanyuwomen:
                intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, AboutFragment.TAG);
                break;
            case R.id.ll_wodeshoucang:
                intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, MyLoveFragment.TAG);
                break;
            case R.id.ll_clearcache:
                //清除应用缓存
                MyAlertDialog alertDialog = new MyAlertDialog(getActivity(), "温馨提示", "是否清除应用缓存？");
                alertDialog.setOnClickListener(new MyAlertDialog.OnPositiveClickListener() {
                    @Override
                    public void onPositiveClickListener(View v) {
                        DataCleanManager.clearAllCache(mActivity);
                        UiUtils.showToastInAnyThread("缓存已清除");
                        try {
                            //获取缓存大小
                            String ss = DataCleanManager.getTotalCacheSize(getActivity());
                            tvCache.setText(ss);
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }
                    }
                });
                alertDialog.show();
                return;
            case R.id.tv_yonghuxieyi:
                MyAlertDialog myAlertDialog = new MyAlertDialog(mActivity, "提示", "哪儿有什么协议啊,哈哈哈！");
                myAlertDialog.setOnClickListener(new MyAlertDialog.OnPositiveClickListener() {
                    @Override
                    public void onPositiveClickListener(View v) {
                    }
                });
                myAlertDialog.show();
                return;
            case R.id.tv_forget_psw:
                intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, ChangeLoginPswFragment.TAG);
                break;
            case R.id.tv_exit:
                intent = null;
                exitLogin();
                return;
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    //退出登录
    private void exitLogin() {
        final MyAlertDialog myAlertDialog = new MyAlertDialog(mActivity, "温馨提示", "确定退出登录吗?");
        myAlertDialog.setOnClickListener(new MyAlertDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClickListener(View v) {
                KV.put(LocalStorageKeys.USER_INFO, null);
                EventBus.getDefault().post(new NotifyRegisterOrLoginSuccess());
                UserModel.logOut();   //清除缓存用户对象
            }
        });
        myAlertDialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void notifyRegisterOrLoginSuccess(NotifyRegisterOrLoginSuccess
                                                     notifyRegisterOrLoginSuccess) {
        initData();
    }
}
