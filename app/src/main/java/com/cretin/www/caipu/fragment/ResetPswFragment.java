package com.cretin.www.caipu.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cretin.www.caipu.BaseApplication;
import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.utils.UiUtils;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPswFragment extends BaseFragment {
    public static final String TAG = "ResetPswFragment";
    @Bind( R.id.et_phone )
    EditText etPhone;
    @Bind( R.id.et_new_psw )
    EditText etNewPsw;
    @Bind( R.id.et_new_confirm )
    EditText etNewConfirm;
    @Bind( R.id.et_code )
    EditText etCode;
    @Bind( R.id.tv_code )
    TextView tvCode;
    @Bind( R.id.tv_submit )
    TextView tvSubmit;
    private Handler handlerCode = BaseApplication.getHandler();
    private int countDown = 60;// 倒计时秒数
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if ( countDown != 1 ) {
                countDown--;
                tvCode.setText("还剩" + countDown + "S");
                tvCode.setEnabled(false);
                handlerCode.postDelayed(this, 1000);
            } else if ( countDown == 1 ) {
                countDown = 60;
                tvCode.setEnabled(true);
                tvCode.setText("重新发送");
            } else if ( countDown <= 0 ) {
                countDown = 60;
                tvCode.setEnabled(true);
                tvCode.setText("获取验证码");
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reset_psw;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        hidProgressView();
        setMainTitle("重置登录密码");
    }

    @Override
    protected void initData() {

    }

    @OnClick( {R.id.tv_code, R.id.tv_submit} )
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.tv_code:
                getCode();
                break;
            case R.id.tv_submit:
                resetPsw();
                break;
        }
    }

    //重置密码
    private void resetPsw() {
        String phone = etPhone.getText().toString().trim();
        String newPsw = etNewPsw.getText().toString();
        String newPswConfirm = etNewConfirm.getText().toString();
        String code = etCode.getText().toString();
        if ( TextUtils.isEmpty(phone) || phone.length() != 11 ) {
            UiUtils.showToastInAnyThread("请输入11位手机号");
            return;
        }
        if ( TextUtils.isEmpty(newPsw) ) {
            UiUtils.showToastInAnyThread("请输入密码");
            return;
        }
        if ( !newPsw.equals(newPswConfirm) ) {
            UiUtils.showToastInAnyThread("两次密码输入不一致");
            return;
        }
        if ( TextUtils.isEmpty(code) ) {
            UiUtils.showToastInAnyThread("请输入验证码");
            return;
        }
        showDialog("重置中...");
        //校验成功 开始重置密码
        BmobUser.resetPasswordBySMSCode(code, newPsw, new UpdateListener() {
            @Override
            public void done(BmobException ex) {
                stopDialog();
                if ( ex == null ) {
                    UiUtils.showToastInAnyThread("密码重置成功");
                    handlerCode.removeCallbacks(runnable);
                    (( BackFragmentActivity ) mActivity).removeFragment();
                } else {
                    UiUtils.showToastInAnyThread("密码重置失败");
                }
            }
        });
    }

    //获取重置密码的验证码
    private void getCode() {
        String phone = etPhone.getText().toString().trim();
        if ( TextUtils.isEmpty(phone) || phone.length() != 11 ) {
            UiUtils.showToastInAnyThread("请输入11位手机号");
            return;
        }
        showDialog("发送验证码...");
        BmobSMS.requestSMSCode(phone, "TakeATaxi", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                stopDialog();
                if ( ex == null ) {//验证码发送成功
                    UiUtils.showToastInAnyThread("验证码发送成功！");
                    handlerCode.postDelayed(runnable, 1000);
                } else {
                    UiUtils.showToastInAnyThread(ex.getMessage());
                }
            }
        });
    }
}
