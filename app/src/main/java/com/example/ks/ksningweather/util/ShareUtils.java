package com.example.ks.ksningweather.util;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareUtils {
    //QQ好友分享
    public static void shareQQ(String title, String shareUrl, String text, String imageUrl,
                               PlatformActionListener listener) {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);//设置分享平台
        QQ.ShareParams sp = new QQ.ShareParams();//分享参数
        sp.setTitle(title);
        sp.setTitleUrl(shareUrl);
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        qq.setPlatformActionListener(listener);
        qq.share(sp);

    }

    //QQ空间
    public static void shareQZone(String title, String shareUrl, String text, String imageUrl,
                                  PlatformActionListener listener) {
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);//设置分享平台
        QZone.ShareParams sp = new QZone.ShareParams();//分享参数
        sp.setTitle(title);
        sp.setTitleUrl(shareUrl);
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setText(text);
        sp.setSite("qq空间");
        sp.setSiteUrl(shareUrl);
        sp.setImageUrl(imageUrl);
        qzone.setPlatformActionListener(listener);
        qzone.share(sp);

    }

    //新浪微博
    public static void shareSinaWeibo(String title, String shareUrl, String text, String imageUrl,
                                      PlatformActionListener listener) {
        Platform sinWeiBo = ShareSDK.getPlatform(SinaWeibo.NAME);
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(shareUrl);
        sp.setSiteUrl(shareUrl);
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        sinWeiBo.setPlatformActionListener(listener);
        sinWeiBo.share(sp);
    }

    //微信
    public static void shareWechat(String title, String shareUrl, String text, String imageUrl,
                                   PlatformActionListener listener) {
        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setImageUrl(imageUrl);
        sp.setUrl(shareUrl);
        sp.setText(text);
        sp.setSite("QQ空间标题");
        weixin.setPlatformActionListener(listener);
        weixin.share(sp);
    }

    //朋友圈
    public static void shareWechatMoments(String title, String shareUrl, String text, String imageUrl,
                                          PlatformActionListener listener) {
        Platform friend = ShareSDK.getPlatform(WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setImageUrl(imageUrl);
        sp.setUrl(shareUrl);
        sp.setText(text);
        sp.setSite("qq空间");
        friend.setPlatformActionListener(listener);
        friend.share(sp);
    }

    public static void shareTwitter(String title, String shareUrl, String text, String imageUrl,
                                    PlatformActionListener listener) {
        Platform twiter = ShareSDK.getPlatform(Twitter.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setImageUrl(imageUrl);
        sp.setUrl(shareUrl);
        sp.setText(text);
        sp.setSite("qq空间");
        twiter.setPlatformActionListener(listener);
        twiter.share(sp);

    }
    public static void shareFaceBook(String title, String shareUrl, String text, String imageUrl,
                                    PlatformActionListener listener) {
        Platform facebook = ShareSDK.getPlatform(Facebook.NAME);
        Facebook.ShareParams sp = new Facebook.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setImageUrl(imageUrl);
        sp.setUrl(shareUrl);
        sp.setText(text);
        sp.setSite("qq空间");
        facebook.setPlatformActionListener(listener);
        facebook.share(sp);

    }
    public static void shareTencentWeibo(String title, String shareUrl, String text, String imageUrl,
                                    PlatformActionListener listener) {
        Platform tecent = ShareSDK.getPlatform(TencentWeibo.NAME);
        TencentWeibo.ShareParams sp = new TencentWeibo.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setImageUrl(imageUrl);
        sp.setUrl(shareUrl);
        sp.setText(text);
        sp.setSite("qq空间");
        tecent.setPlatformActionListener(listener);
        tecent.share(sp);

    }



}