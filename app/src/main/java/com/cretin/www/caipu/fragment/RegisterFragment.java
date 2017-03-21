package com.cretin.www.caipu.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cretin.www.caipu.BaseApplication;
import com.cretin.www.caipu.R;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.eventbus.NotifyLoginRegisterChange;
import com.cretin.www.caipu.eventbus.NotifyRegisterOrLoginSuccess;
import com.cretin.www.caipu.model.UserModel;
import com.cretin.www.caipu.utils.MyAlertDialog;
import com.cretin.www.caipu.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment {
    public static final String TAG = "RegisterFragment";
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
    @Bind( R.id.iv_code )
    ImageView ivCode;
    @Bind( R.id.et_code )
    EditText etCode;
    @Bind( R.id.tv_code )
    TextView tvCode;
    @Bind( R.id.bt_submit )
    TextView btSubmit;
    @Bind( R.id.tv_protocol )
    TextView tvProtocol;
    @Bind( R.id.et_chepaihao )
    EditText etChepaihao;
    @Bind( R.id.rl_chepaihao )
    RelativeLayout rlChepaihao;
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
    protected void initData() {

    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        hidTitleView();
        hidProgressView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @OnClick( {R.id.tv_code, R.id.bt_submit, R.id.tv_protocol} )
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.tv_code:
                getCode();
                break;
            case R.id.bt_submit:
                register();
                break;
            case R.id.tv_protocol:
                MyAlertDialog myAlertDialog = new MyAlertDialog(mActivity, "提示", "哪儿有什么协议啊,哈哈哈！");
                myAlertDialog.setOnClickListener(new MyAlertDialog.OnPositiveClickListener() {
                    @Override
                    public void onPositiveClickListener(View v) {
                    }
                });
                myAlertDialog.show();
                break;
        }
    }

    //获取验证码
    private void getCode() {
        final String phone = etPhone.getText().toString().trim();
        if ( TextUtils.isEmpty(phone) ) {
            UiUtils.showToastInAnyThread("手机号不能为空!");
            return;
        }
        showDialog("发送验证码...");
        BmobQuery<UserModel> query = new BmobQuery<>();
        query.addWhereEqualTo("username", phone);
        query.findObjects(new FindListener<UserModel>() {
            @Override
            public void done(List<UserModel> object, BmobException e) {
                if ( e == null ) {
                    if ( object.size() == 0 ) {
                        //获取验证码
                        BmobSMS.requestSMSCode(phone, "Caipu", new QueryListener<Integer>() {
                            @Override
                            public void done(Integer smsId, BmobException ex) {
                                stopDialog();
                                if ( ex == null ) {
                                    //验证码发送成功
                                    UiUtils.showToastInAnyThread("验证码发送成功");
                                    handlerCode.postDelayed(runnable, 1000);
                                } else {
                                    if ( ex.getErrorCode() == 10010 ) {
                                        UiUtils.showToastInAnyThread("验证码发送太快,请稍后再试");
                                    } else
                                        UiUtils.showToastInAnyThread(ex.getMessage());
                                }
                            }
                        });
                    } else {
                        stopDialog();
                        UiUtils.showToastInAnyThread("该手机号已经被注册,请直接登录");
                    }
                } else {
                    stopDialog();
                }
            }
        });
    }

    //注册操作
    private void register() {
        final String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString();
        String code = etCode.getText().toString();
        String chepaihao = etChepaihao.getText().toString();
        if ( TextUtils.isEmpty(phone) ) {
            UiUtils.showToastInAnyThread("手机号不能为空");
            return;
        }
        if ( TextUtils.isEmpty(password) ) {
            UiUtils.showToastInAnyThread("密码不能为空");
            return;
        }
        if ( rlChepaihao.getVisibility() == View.VISIBLE )
            if ( TextUtils.isEmpty(chepaihao) ) {
                UiUtils.showToastInAnyThread("车牌号不能为空");
                return;
            }
        if ( TextUtils.isEmpty(code) ) {
            UiUtils.showToastInAnyThread("验证码不能为空");
            return;
        }
        showDialog("正在注册...");
        //用手机号进行注册
        UserModel user = new UserModel();
        user.setMobilePhoneNumber(phone);//设置手机号码（必填）
        user.setPassword(password);
        user.setAge(0);
        user.setSex(0);
        user.setPsw(password);
        //设置用户密码
        user.signOrLogin(code, new SaveListener<UserModel>() {

            @Override
            public void done(UserModel user, BmobException e) {
                stopDialog();
                if ( e == null ) {
                    UiUtils.showToastInAnyThread("注册成功");
                    handlerCode.removeCallbacks(runnable);
                    EventBus.getDefault().post(new NotifyRegisterOrLoginSuccess());
                    EventBus.getDefault().post(new NotifyLoginRegisterChange(NotifyLoginRegisterChange.STATE_REGISTERS, null));
                    clearAllData();
                } else {
                    UiUtils.showToastInAnyThread("注册失败");
                }
            }
        });
    }

    //清除文本框的数据
    private void clearAllData() {
        etCode.setText("");
        etPassword.setText("");
        etPhone.setText("");
        countDown = -2;
        countDown = 60;
        tvCode.setEnabled(true);
        tvCode.setText("获取验证码");
    }
}
