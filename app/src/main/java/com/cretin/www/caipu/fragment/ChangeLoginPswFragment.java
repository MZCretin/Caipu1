package com.cretin.www.caipu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.app.LocalStorageKeys;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.eventbus.NotifyRegisterOrLoginSuccess;
import com.cretin.www.caipu.utils.KV;
import com.cretin.www.caipu.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeLoginPswFragment extends BaseFragment {
    public static final String TAG = "ChangeLoginPswFragment";
    @Bind( R.id.et_original )
    EditText etOriginal;
    @Bind( R.id.et_new_psw )
    EditText etNewPsw;
    @Bind( R.id.et_new_confirm )
    EditText etNewConfirm;
    @Bind( R.id.tv_submit )
    TextView tvSubmit;
    @Bind( R.id.ll_first )
    LinearLayout llFirst;
    private int type = 0;
    private String telephone;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_change_login_psw;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        setMainTitle("修改登录密码");
    }

    @Override
    protected void initData() {

    }

    @OnClick( R.id.tv_submit )
    public void onClick() {
        submit();
    }

    private void submit() {
        String originStr = etOriginal.getText().toString();
        String newPsw = etNewPsw.getText().toString();
        String confirmPsw = etNewConfirm.getText().toString();

        if ( TextUtils.isEmpty(originStr) ||
                TextUtils.isEmpty(newPsw) ||
                TextUtils.isEmpty(confirmPsw) ) {
            UiUtils.showToastInAnyThread("请完整填写信息！");
            return;
        }
        if ( newPsw.length() < 6 ) {
            UiUtils.showToastInAnyThread("密码长度不能少于6位！");
            return;
        }

        if ( !newPsw.equals(confirmPsw) ) {
            UiUtils.showToastInAnyThread("两次密码输入不一致！");
            return;
        }

        BmobUser.updateCurrentUserPassword(originStr, newPsw, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if ( e == null ) {
                    UiUtils.showToastInAnyThread("密码修改成功，可以用新密码进行登录啦");
                    ((BackFragmentActivity )mActivity).closeAllFragment();
                    KV.put(LocalStorageKeys.USER_INFO,null);
                    EventBus.getDefault().post(new NotifyRegisterOrLoginSuccess());
                } else {
                    UiUtils.showToastInAnyThread("密码修改失败");
                }
            }

        });
    }
}
