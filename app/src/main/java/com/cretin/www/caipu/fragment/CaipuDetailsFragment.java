package com.cretin.www.caipu.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.cretin.www.caipu.R;
import com.cretin.www.caipu.adapter.CommonAdapter;
import com.cretin.www.caipu.app.LocalStorageKeys;
import com.cretin.www.caipu.base.BackFragmentActivity;
import com.cretin.www.caipu.base.BaseFragment;
import com.cretin.www.caipu.model.CommentModel;
import com.cretin.www.caipu.model.LikeModel;
import com.cretin.www.caipu.model.ResponseModel;
import com.cretin.www.caipu.model.UserModel;
import com.cretin.www.caipu.utils.KV;
import com.cretin.www.caipu.utils.UiUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.cretin.www.caipu.R.id.ll_container;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaipuDetailsFragment extends BaseFragment implements SpeechSynthesizerListener {
    public static final String TAG = "CaipuDetailsFragment";
    @Bind( R.id.listview )
    ListView listview;
    @Bind( R.id.iv_like )
    ImageView ivLike;
    @Bind( R.id.iv_pl )
    ImageView ivPl;
    @Bind( R.id.tv_plnum )
    TextView tvPlnum;
    @Bind( R.id.iv_replay )
    ImageView ivReplay;
    @Bind( R.id.iv_last )
    ImageView ivLast;
    @Bind( R.id.iv_next )
    ImageView ivNext;
    private ResponseModel.ResultBean.DataBean mData;
    private ListViewAdapter adapter;
    private List<ResponseModel.ResultBean.DataBean.StepsBean> list;
    private boolean hasShoucahng;
    //保存收藏后的id
    private String objectId;

    public static CaipuDetailsFragment newInstance(ResponseModel.ResultBean.DataBean data) {
        Bundle args = new Bundle();
        args.putSerializable("model", data);
        CaipuDetailsFragment fragment = new CaipuDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_caipu_details;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        showProgressView();
        mData = ( ResponseModel.ResultBean.DataBean ) getArguments().getSerializable("model");
        if ( mData != null )
            setMainTitle(mData.getTitle());
        if ( mData != null ) {
            UserModel userModel = KV.get(LocalStorageKeys.USER_INFO);
            if ( userModel == null ) {
                UiUtils.showToastInAnyThread("您未登录，无法获取收藏状态");
                return;
            }
            BmobQuery<LikeModel> query = new BmobQuery<>();
            query.addWhereEqualTo("id", mData.getId());
            query.addWhereEqualTo("userId", userModel.getObjectId());
            //执行查询方法
            query.findObjects(new FindListener<LikeModel>() {
                @Override
                public void done(List<LikeModel> object, BmobException e) {
                    hidProgressView();
                    if ( e == null ) {
                        if ( object != null && !object.isEmpty() ) {
                            objectId = object.get(0).getObjectId();
                            ivLike.setImageResource(R.mipmap.sc_red);
                            hasShoucahng = true;
                        } else {
                            ivLike.setImageResource(R.mipmap.sc_grey);
                            hasShoucahng = false;
                        }
                    } else {
                        UiUtils.showToastInAnyThread("网络异常");
                    }
                }
            });
        }
        ivLast.setEnabled(false);
    }

    private void like() {
        showDialog("正在收藏...");
        LikeModel likeModel = new LikeModel();
        likeModel.setId(mData.getId());
        likeModel.setCaipu_id(mData.getCaipu_id());
        likeModel.setTitle(mData.getTitle());
        likeModel.setAlbums(mData.getAlbums());
        likeModel.setTags(mData.getTags());
        likeModel.setImtro(mData.getImtro());
        likeModel.setIngredients(mData.getIngredients());
        likeModel.setBurden(mData.getBurden());
        likeModel.setPassed(mData.getPassed());
        likeModel.setUser_upload(mData.getUser_upload());
        likeModel.setUserId(BmobUser.getCurrentUser().getObjectId());
        List<LikeModel.StepsBean> list = new ArrayList<>();
        for ( ResponseModel.ResultBean.DataBean.StepsBean bean : mData.getSteps()
                ) {
            list.add(new LikeModel.StepsBean(bean.getImg(), bean.getStep()));
        }
        likeModel.setSteps(list);
        likeModel.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                stopDialog();
                if ( e == null ) {
                    UiUtils.showToastInAnyThread("收藏成功");
                    objectId = s;
                    hasShoucahng = true;
                    ivLike.setImageResource(R.mipmap.sc_red);
                } else {
                    UiUtils.showToastInAnyThread("网络异常");
                }
            }
        });
    }

    public void cancleLike() {
        showDialog("取消收藏...");
        LikeModel gameScore = new LikeModel();
        gameScore.setObjectId(objectId);
        gameScore.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                stopDialog();
                if ( e == null ) {
                    UiUtils.showToastInAnyThread("取消收藏成功");
                    hasShoucahng = false;
                    ivLike.setImageResource(R.mipmap.sc_grey);
                } else {
                    UiUtils.showToastInAnyThread("网络异常");
                }
            }
        });
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        View head = View.inflate(mActivity, R.layout.head_caipu_details, null);
        View footer = View.inflate(mActivity, R.layout.footer_listview_caipu_details, null);
        initHeadView(head);
        initFooterView(footer);
        listview.addHeaderView(head);
        listview.addFooterView(footer);
        adapter = new ListViewAdapter(mActivity, list, R.layout.item_listview_step);
        listview.setAdapter(adapter);

        if ( mData != null ) {
            if ( mData.getSteps() != null ) {
                list.addAll(mData.getSteps());
                adapter.notifyDataSetChanged();
            }
        }

        initTTS();
    }

    private int currCount = 0;
    private TextView tvPLNums;
    private TextView tvSeeAll;
    private LinearLayout llContainer;

    private void initFooterView(View footer) {
        tvPLNums = ( TextView ) footer.findViewById(R.id.tv_pl_num);
        tvSeeAll = ( TextView ) footer.findViewById(R.id.tv_seeall);
        llContainer = ( LinearLayout ) footer.findViewById(ll_container);
        BmobQuery<CommentModel> query = new BmobQuery<>();
        query.addWhereEqualTo("caipuId", mData.getCaipu_id());
        query.count(CommentModel.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if ( e == null ) {
                    tvPLNums.setText(count + "条评论");
                    tvPlnum.setText("评论 " + count);
                    currCount = count;
                    if ( count > 3 ) {
                        tvSeeAll.setVisibility(View.VISIBLE);
                    }
                } else {
                    UiUtils.showToastInAnyThread("网络异常");
                }
            }
        });

        BmobQuery<CommentModel> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("caipuId", mData.getCaipu_id());
        //返回3条数据，如果不加上这条语句，默认返回10条数据
        query1.setLimit(3);
        //按时间先后查询
        query1.order("-createdAt");
        // 希望在查询信息的同时也把发布人的信息查询出来
        query1.include("user");
        //执行查询方法
        query1.findObjects(new FindListener<CommentModel>() {
            @Override
            public void done(List<CommentModel> object, BmobException e) {
                if ( e == null ) {
                    for ( CommentModel gameScore : object ) {
                        View view = View.inflate(mActivity, R.layout.item_comment_list, null);
                        TextView tvName1 = ( TextView ) view.findViewById(R.id.tv_username);
                        TextView tvContent1 = ( TextView ) view.findViewById(R.id.tv_content);
                        TextView tvTime1 = ( TextView ) view.findViewById(R.id.tv_time);
                        String name = gameScore.getUser().getNick();
                        if ( TextUtils.isEmpty(name) )
                            name = gameScore.getUser().getUsername();
                        tvName1.setText(name);
                        tvContent1.setText(gameScore.getContent());
                        tvTime1.setText(gameScore.getCreatedAt().toString());
                        llContainer.addView(view);
                    }
                } else {
                    UiUtils.showToastInAnyThread("网络异常");
                }
            }
        });

        tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到详情页
                (( BackFragmentActivity ) mActivity).addFragment(
                        CommentListFragment.newInstance(mData.getCaipu_id()), true, true);
            }
        });
    }

    //填充头部数据
    private void initHeadView(View head) {
        TextView tvName = ( TextView ) head.findViewById(R.id.tv_name);
        TextView tvTips = ( TextView ) head.findViewById(R.id.tv_tips);
        TextView tvDaodu = ( TextView ) head.findViewById(R.id.tv_daodu);
        TextView tvYongliao = ( TextView ) head.findViewById(R.id.tv_yongliao);
        ImageView ivPic = ( ImageView ) head.findViewById(R.id.iv_pic);
        Picasso.with(mActivity).load(mData.getAlbums()).into(ivPic);
        tvName.setText(mData.getTitle());
        tvTips.setText(mData.getIngredients());
        tvDaodu.setText("       " + mData.getImtro());
        tvYongliao.setText(mData.getBurden().replaceAll(";", "\n").replaceAll(",", "    :    "));
    }

    //记录当前步骤
    private int stepIndex = -1;

    @OnClick( {R.id.iv_like, R.id.iv_pl, R.id.iv_replay, R.id.iv_last, R.id.iv_next} )
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.iv_like:
                UserModel userModel = KV.get(LocalStorageKeys.USER_INFO);
                if ( userModel == null ) {
                    UiUtils.showToastInAnyThread("您未登录，请先登录再收藏");
                    return;
                }
                if ( hasShoucahng ) {
                    //取消收藏
                    cancleLike();
                } else {
                    //收藏
                    like();
                }
                break;
            case R.id.iv_pl:
                UserModel userModel1 = KV.get(LocalStorageKeys.USER_INFO);
                if ( userModel1 == null ) {
                    UiUtils.showToastInAnyThread("您未登录，请先登录再评论");
                    return;
                }
                comment();
                break;
            case R.id.iv_replay:
                if ( stepIndex >= 0 && stepIndex < list.size() ) {
                    ResponseModel.ResultBean.DataBean.StepsBean stepsBean1 = list.get(stepIndex);
                    mSpeechSynthesizer.stop();
                    mSpeechSynthesizer.speak(stepsBean1.getStep());
                }
                break;
            case R.id.iv_last:
                stepIndex--;
                if ( stepIndex >= 0 ) {
                    scroll(stepIndex + 1);
                    ResponseModel.ResultBean.DataBean.StepsBean stepsBean = list.get(stepIndex);
                    mSpeechSynthesizer.stop();
                    mSpeechSynthesizer.speak(stepsBean.getStep());
                    if ( stepIndex > 0 ) {
                        if ( stepIndex < list.size() - 1 ) {
                            ivNext.setImageResource(R.mipmap.next);
                            ivNext.setEnabled(true);
                        }
                    } else {
                        ivLast.setImageResource(R.mipmap.last_grey);
                        ivLast.setEnabled(false);
                        if ( stepIndex < list.size() - 1 ) {
                            ivNext.setImageResource(R.mipmap.next);
                            ivNext.setEnabled(true);
                        }
                    }
                }
                break;
            case R.id.iv_next:
                stepIndex++;
                if ( stepIndex < list.size() ) {
                    scroll(stepIndex + 1);
                    ResponseModel.ResultBean.DataBean.StepsBean stepsBean = list.get(stepIndex);
                    mSpeechSynthesizer.stop();
                    mSpeechSynthesizer.speak(stepsBean.getStep());
                    if ( stepIndex < list.size() - 1 ) {
                        if ( stepIndex > 0 ) {
                            ivLast.setImageResource(R.mipmap.last);
                            ivLast.setEnabled(true);
                        }
                    } else {
                        ivNext.setImageResource(R.mipmap.next_gray);
                        ivNext.setEnabled(false);
                        if ( stepIndex > 0 ) {
                            ivLast.setImageResource(R.mipmap.last);
                            ivLast.setEnabled(true);
                        }
                    }
                }
                break;
        }
    }

    private void scroll(final int position) {
        listview.post(new Runnable() {
            @Override
            public void run() {
                listview.setSelectionFromTop(position, 0);
            }
        });
    }

    private void comment() {
        if ( mData != null ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_comment, null);
            builder.setView(view);
            final AlertDialog alertDialog = builder.create();
            final EditText editText = ( EditText ) view.findViewById(R.id.dialog_content);
            final TextView confirm = ( TextView ) view.findViewById(R.id.button_confirm);
            final TextView cancel = ( TextView ) view.findViewById(R.id.button_cancel);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String content = editText.getText().toString();
                    if ( TextUtils.isEmpty(content) ) {
                        UiUtils.showToastInAnyThread("请输入评论内容");
                        return;
                    }
                    CommentModel commentModel = new CommentModel();
                    commentModel.setCaipuId(mData.getCaipu_id());
                    commentModel.setContent(content);
                    commentModel.setUser(BmobUser.getCurrentUser(UserModel.class));
                    commentModel.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if ( e == null ) {
                                UiUtils.showToastInAnyThread("评论成功");
                                currCount++;
                                if ( currCount - 1 == 3 ) {
                                    //代表之前只是没有显示查看更多
                                    tvSeeAll.setVisibility(View.VISIBLE);
                                } else if ( currCount - 1 < 3 ) {
                                    //再增加一条数据
                                    View view = View.inflate(mActivity, R.layout.item_comment_list, null);
                                    TextView tvName1 = ( TextView ) view.findViewById(R.id.tv_username);
                                    TextView tvContent1 = ( TextView ) view.findViewById(R.id.tv_content);
                                    TextView tvTime1 = ( TextView ) view.findViewById(R.id.tv_time);
                                    UserModel currentUser = BmobUser.getCurrentUser(UserModel.class);
                                    String name = currentUser.getNick();
                                    if ( TextUtils.isEmpty(name) )
                                        name = currentUser.getUsername();
                                    tvName1.setText(name);
                                    tvContent1.setText(content);
                                    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    tvTime1.setText(simple.format(new Date()));
                                    llContainer.addView(view);
                                }
                                tvPlnum.setText("评论 " + currCount);
                                tvPLNums.setText(currCount + "条评论");
                            }
                        }
                    });
                    alertDialog.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            UiUtils.showToastInAnyThread("服务器异常");
        }
    }


    // 语音合成客户端
    private SpeechSynthesizer mSpeechSynthesizer;
    // 语音合成授权信息
    private AuthInfo authInfo;

    // 初始化语音合成客户端并启动
    private void initTTS() {
        // 获取语音合成对象实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(mActivity);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
        mSpeechSynthesizer.setApiKey("c15Sejb4iSAePI9NeDwbUkVN", "563132025fb7d854fa0019d821afce0e");
        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
        mSpeechSynthesizer.setAppId("9422339");
        // 设置语音合成文本模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, "your_txt_file_path");
        // 设置语音合成声音模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, "your_speech_file_path");
        // 设置语音合成声音授权文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, "your_licence_path");
        // 获取语音合成授权信息
        authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        // 判断授权信息是否正确，如果正确则初始化语音合成器并开始语音合成，如果失败则做错误处理
        if ( authInfo.isSuccess() ) {
            mSpeechSynthesizer.initTts(TtsMode.MIX);
        } else {
            // 授权失败
            UiUtils.showToastInAnyThread("语音授权失败");
        }
    }

    public void onError(String arg0, SpeechError arg1) {
        // 监听到出错，在此添加相关操作
    }

    public void onSpeechFinish(String arg0) {
        // 监听到播放结束，在此添加相关操作
    }

    public void onSpeechProgressChanged(String arg0, int arg1) {
        // 监听到播放进度有变化，在此添加相关操作
    }

    public void onSpeechStart(String arg0) {
        // 监听到合成并播放开始，在此添加相关操作
    }

    public void onSynthesizeDataArrived(String arg0, byte[] arg1, int arg2) {
        // 监听到有合成数据到达，在此添加相关操作
    }

    public void onSynthesizeFinish(String arg0) {
        // 监听到合成结束，在此添加相关操作
    }

    public void onSynthesizeStart(String arg0) {
        // 监听到合成开始，在此添加相关操作
    }

    class ListViewAdapter extends CommonAdapter<ResponseModel.ResultBean.DataBean.StepsBean> {

        public ListViewAdapter(Context context, List<ResponseModel.ResultBean.DataBean.StepsBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolders holder, ResponseModel.ResultBean.DataBean.StepsBean item, int position) {
            holder.setText(R.id.tv_nums, (position + 1) + ".");
            holder.setText(R.id.tv_des, item.getStep().substring(item.getStep().indexOf(".") + 1));
            Picasso.with(mActivity)
                    .load(item.getImg())
                    .into(( ImageView ) holder.getView(R.id.iv_pic));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSpeechSynthesizer.release();
    }
}
