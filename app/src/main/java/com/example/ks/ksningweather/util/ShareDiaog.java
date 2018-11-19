package com.example.ks.ksningweather.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ks.ksningweather.MyActivity;
import com.example.ks.ksningweather.R;

public class ShareDiaog implements View.OnClickListener {
    private Context context;
    //目录提醒
    private AlertDialog alertDialog;
    private LinearLayout ll_share_wechat;
    private LinearLayout ll_share_friend;
    private LinearLayout ll_share_qq;
    private LinearLayout ll_share_sina;
    private RelativeLayout rl_menu_cancle;
    private LinearLayout ll_share_twitter;
    private LinearLayout ll_share_tecent;
    private LinearLayout ll_share_facebook;
    private LinearLayout ll_share_qzone;

    public ShareClickListener shareClickListener;

    public ShareDiaog(Context context){
        this.context=context;
    }

    public ShareDiaog builder(){
        alertDialog=new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.AppTheme)).create();
        alertDialog.show();
        Window win=alertDialog.getWindow();
        win.setWindowAnimations(R.style.AppTheme);
        win.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams lp=win.getAttributes();
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        win.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        win.setAttributes(lp);
        win.setContentView(R.layout.share_dialog);

        //在窗口win中找到各个板块
        rl_menu_cancle=win.findViewById(R.id.rl_menu_cancle);
        ll_share_wechat=win.findViewById(R.id.ll_share_wechat);
        ll_share_friend = win.findViewById(R.id.ll_share_pyq);
        ll_share_qq = win.findViewById(R.id.ll_share_qq);
        ll_share_sina=win.findViewById(R.id.ll_share_sina);
        ll_share_facebook=win.findViewById(R.id.ll_share_facebook);
        ll_share_qzone=win.findViewById(R.id.ll_share_qzone);
        ll_share_tecent=win.findViewById(R.id.ll_share_tecentWeiBo);
        ll_share_twitter=win.findViewById(R.id.ll_share_twitter);

        //为他们设置监听事件
        rl_menu_cancle.setOnClickListener(this);
        ll_share_wechat.setOnClickListener(this);
        ll_share_friend.setOnClickListener(this);
        ll_share_qq.setOnClickListener(this);
        ll_share_sina.setOnClickListener(this);
        ll_share_twitter.setOnClickListener(this);
        ll_share_tecent.setOnClickListener(this);
        ll_share_qzone.setOnClickListener(this);
        ll_share_facebook.setOnClickListener(this);


        return this;

    }
    public void show(){
        alertDialog.show();
    }
    public void cancle(){
        alertDialog.cancel();
    }
    //定义分享接口


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.rl_menu_cancle:
                cancle();
                break;
            case R.id.ll_share_wechat:
                cancle();
                if(shareClickListener!=null){
                    shareClickListener.shareWechat();
                }
                break;
            case R.id.ll_share_qq:
                cancle();
                if(shareClickListener!=null){
                    shareClickListener.shareQQ();
                }
                break;
            case R.id.ll_share_sina:
                cancle();
                if(shareClickListener!=null){
                    shareClickListener.shareSinaWeibo();
                }
                break;
            case R.id.ll_share_pyq:
                cancle();
                if(shareClickListener!=null){
                    shareClickListener.shareFriend();
                }
                break;
            case R.id.ll_share_facebook:
                cancle();
                if(shareClickListener!=null){
                    shareClickListener.shareFaceBook();
                }
                break;
            case R.id.ll_share_tecentWeiBo:
                cancle();
                if(shareClickListener!=null){
                    shareClickListener.shareTecent();
                }
                break;
            case R.id.ll_share_twitter:
                cancle();
                if(shareClickListener!=null){
                    shareClickListener.shareTwitter();
                }
                break;
            case R.id.ll_share_qzone:
                cancle();
                if(shareClickListener!=null){
                    shareClickListener.shareQzone();
                }
                break;

        }

    }
    public interface ShareClickListener{
        void shareWechat();
        void shareFriend();
        void shareQQ();
        void shareQzone();
        void shareSinaWeibo();
        void shareFaceBook();
        void shareTecent();
        void shareTwitter();

    }
    public void setShareClickListener(ShareClickListener shareClickListener){
        this.shareClickListener=shareClickListener;

    }

}
