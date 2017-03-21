package com.cretin.www.caipu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cretin.www.caipu.R;
import com.cretin.www.caipu.app.LocalStorageKeys;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.model.UserModel;
import com.cretin.www.caipu.utils.KV;
import com.cretin.www.caipu.utils.UiUtils;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends BaseFragment {
    public static final String TAG = "UserInfoFragment";
    @Bind( R.id.tv_username )
    TextView tvUsername;
    @Bind( R.id.tv_nickname )
    EditText tvNickname;
    @Bind( R.id.et_phone )
    EditText etPhone;
    @Bind( R.id.radio1 )
    RadioButton radio1;
    @Bind( R.id.radio2 )
    RadioButton radio2;
    @Bind( R.id.et_qq )
    EditText etQq;
    @Bind( R.id.et_des )
    EditText etDes;
    @Bind( R.id.bt_submit )
    TextView btSubmit;
    private UserModel userModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        setMainTitle("个人信息");
    }

    @Override
    protected void initData() {
        userModel = KV.get(LocalStorageKeys.USER_INFO);
        tvUsername.setText(userModel.getUsername());
        String nick = userModel.getNick();
        if ( !TextUtils.isEmpty(nick) ) {
            tvNickname.setText(nick);
        }
        etPhone.setText(userModel.getAge() + "");
        if ( userModel.getSex() == 0 ) {
            radio2.setChecked(true);
        } else {
            radio1.setChecked(true);
        }
        if ( userModel.getQq() != null )
            etQq.setText(userModel.getQq());
        if ( userModel.getMails() != null )
            etDes.setText(userModel.getMails());
    }

    @OnClick( R.id.bt_submit )
    public void onClick() {
        showDialog("正在修改...");
        userModel.setNick(tvNickname.getText().toString());
        userModel.setAge(Integer.parseInt(etPhone.getText().toString()));
        userModel.setSex(radio1.isChecked() ? 1 : 0);
        userModel.setQq(etQq.getText().toString());
        userModel.setMails(etDes.getText().toString());
        userModel.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                stopDialog();
                if ( e == null ) {
                    KV.put(LocalStorageKeys.USER_INFO, userModel);
                    UiUtils.showToastInAnyThread("修改成功");
                    (( BackFragmentActivity ) mActivity).removeFragment();
                }
            }
        });
    }
}
