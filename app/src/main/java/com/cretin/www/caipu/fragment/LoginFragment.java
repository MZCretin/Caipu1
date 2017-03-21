package com.cretin.www.caipu.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.app.LocalStorageKeys;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.eventbus.NotifyLoginRegisterChange;
import com.cretin.www.caipu.eventbus.NotifyRegisterOrLoginSuccess;
import com.cretin.www.caipu.model.UserModel;
import com.cretin.www.caipu.ui.MeManager;
import com.cretin.www.caipu.utils.KV;
import com.cretin.www.caipu.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {
    public static final String TAG = "LoginFragment";
    @Bind( R.id.iv_phone )
    ImageView ivPhone;
    @Bind( R.id.et_phone )
    EditText etPhone;
    @Bind( R.id.iv_password )
    ImageView ivPassword;
    @Bind( R.id.et_password )
    EditText etPassword;
    @Bind( R.id.rl_money )
    RelativeLayout rlMoney;
    @Bind( R.id.bt_submit )
    TextView btSubmit;
    @Bind( R.id.tv_register )
    TextView tvRegister;
    @Bind( R.id.tv_forget_psw )
    TextView tvForgetPsw;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        hidTitleView();
        hidProgressView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick( {R.id.bt_submit, R.id.tv_register, R.id.tv_forget_psw} )
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.bt_submit:
                login();
                break;
            case R.id.tv_register:
                EventBus.getDefault().post(new NotifyLoginRegisterChange(NotifyLoginRegisterChange.STATE_LOGIN, null));
                break;
            case R.id.tv_forget_psw:
                //忘记密码
                Intent intent = new Intent(mActivity, MeManager.class);
                intent.putExtra(BackFragmentActivity.TAG_FRAGMENT, ResetPswFragment.TAG);
                mActivity.startActivity(intent);
                break;
        }
    }

    //登录操作
    private void login() {
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        if ( TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) ) {
            UiUtils.showToastInAnyThread("用户名或密码不能为空！");
            return;
        }
        showDialog("正在登录...");
        UserModel bu2 = new UserModel();
        bu2.setUsername(phone);
        bu2.setPassword(password);
        bu2.login(new SaveListener<UserModel>() {
                      @Override
                      public void done(UserModel bmobUser, BmobException e) {
                          stopDialog();
                          if ( e == null ) {
                              UiUtils.showToastInAnyThread("登录成功");
                              KV.put(LocalStorageKeys.USER_INFO, bmobUser);
                              EventBus.getDefault().post(new NotifyRegisterOrLoginSuccess());
                              (( BackFragmentActivity ) mActivity).removeFragment();
                          } else {
                              if ( e.getErrorCode() == 101 ) {
                                  UiUtils.showToastInAnyThread("用户名或密码错误");
                              } else
                                  UiUtils.showToastInAnyThread("网络异常");
                          }
                      }
                  }
        );
    }
}
