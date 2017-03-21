package com.cretin.www.caipu.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cretin.www.caipu.R;


public class MyAlertDialog extends Dialog implements
        View.OnClickListener {
    private View mView;
    private TextView mTitle;
    public TextView mContent;
    public TextView mBtnCancel;
    private TextView mBtnConfirm;
    private String title;
    private String message;
    private int layoutId;
    private View contentView;
    private Context mContext;
    private String okMessage;
    private String cancelMsg;
    private int textSize = -1;
    private boolean showEWM;
    private int gravity = Gravity.CENTER;
    private int padding = 0;
    private int padLR;
    private int padTB;

    public MyAlertDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public MyAlertDialog(Context context, int layoutId) {
        super(context);
        this.layoutId = layoutId;
        this.mContext = context;
    }


    public MyAlertDialog(Context context, View contentView) {
        super(context);
        this.contentView = contentView;
        this.mContext = context;
    }

    public MyAlertDialog(Context context, String title, String message) {
        super(context);
        this.title = title;
        this.message = message;
        this.mContext = context;
    }

    public MyAlertDialog(Context context, String title, String message, int textSize) {
        super(context);
        this.title = title;
        this.message = message;
        this.mContext = context;
        this.textSize = textSize;
    }

    public MyAlertDialog(Context context, String title, String message, int textSize, int gravity) {
        super(context);
        this.title = title;
        this.message = message;
        this.mContext = context;
        this.textSize = textSize;
        this.gravity = gravity;
    }

    public MyAlertDialog(Context context, String title, String message, int textSize, int gravity, int padding) {
        super(context);
        this.title = title;
        this.message = message;
        this.mContext = context;
        this.textSize = textSize;
        this.gravity = gravity;
        this.padding = padding;
    }

    public MyAlertDialog(Context context, String title, String message, int textSize, int gravity, int padLR, int padTB) {
        super(context);
        this.title = title;
        this.message = message;
        this.mContext = context;
        this.textSize = textSize;
        this.gravity = gravity;
        this.padLR = padLR;
        this.padTB = padTB;
    }

    public MyAlertDialog(Context context, String title, String message, String okMessage, String cancelMsg) {
        super(context);
        this.title = title;
        this.message = message;
        this.mContext = context;
        this.okMessage = okMessage;
        this.cancelMsg = cancelMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if ( layoutId != 0 ) {
            setContentView(layoutId);
        } else if ( contentView != null ) {
            setContentView(contentView);
        } else {
            setContentView(R.layout.common_dialog);
            mTitle = ( TextView ) findViewById(R.id.dialog_title);
            mContent = ( TextView ) findViewById(R.id.dialog_content);
            mBtnCancel = ( TextView ) findViewById(R.id.button_cancel);
            mBtnConfirm = ( TextView ) findViewById(R.id.button_confirm);
            mTitle.setText(title);
            mContent.setText(message);
            if ( !TextUtils.isEmpty(okMessage) )
                mBtnConfirm.setText(okMessage);
            if ( !TextUtils.isEmpty(cancelMsg) )
                mBtnCancel.setText(cancelMsg);
            if ( textSize != -1 ) {
                mContent.setTextSize(textSize);
            }
            if ( padding != 0 )
                mContent.setPadding(padding, padding, padding, padding);
            mContent.setPadding(padLR, padTB, padLR, padTB);
            mContent.setGravity(gravity);
            mBtnCancel.setOnClickListener(this);
            mBtnConfirm.setOnClickListener(this);
        }
    }

    public void setTitle(String title) {
        if ( mTitle != null ) {
            mTitle.setText(title);
        }
    }

    public void hideCancel() {
        if ( mBtnCancel != null )
            mBtnCancel.setVisibility(View.GONE);
    }

    public void setRightButton(String button) {
        if ( mBtnConfirm != null ) {
            mBtnConfirm.setText(button);
        }
    }

    public void setMessage(String message) {
        if ( mContent != null ) {
            mContent.setText(message);
        }
    }

    OnPositiveClickListener mClickListener;

    public interface OnPositiveClickListener {
        void onPositiveClickListener(View v);

    }

    public interface OnNegativeClickListener {
        void onNegativeClickListener(View v);
    }

    private OnNegativeClickListener mNegativeListener;

    public MyAlertDialog setOnNegativeListener(
            OnNegativeClickListener mNegativeListener) {
        this.mNegativeListener = mNegativeListener;
        return this;
    }

    public MyAlertDialog setOnClickListener(OnPositiveClickListener mClickListener) {
        this.mClickListener = mClickListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.button_cancel:
                if ( mNegativeListener != null ) {
                    mNegativeListener.onNegativeClickListener(v);
                }
                this.dismiss();
                break;
            case R.id.button_confirm:
                if ( mClickListener != null ) {
                    this.dismiss();
                    mClickListener.onPositiveClickListener(v);
                }
                break;
            default:
                break;
        }
    }
}
