package com.run.running.activity.paymethod.wechat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.run.running.constants.WechatConstants;
import com.run.running.utils.WechatUtil;
import com.run.running.view.MMAlert;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXFileObject;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 微信发送消息界面
 * Created by zsr on 2016/6/13.
 */
@ContentView(R.layout.activity_sendtowx)
public class SendToWXActivity extends BaseActivity {

    @ViewInject(R.id.wechat_ifsendtofh)
    private CheckBox checkBox;

    @ViewInject(R.id.wechat_openid)
    private EditText openid;

    @ViewInject(R.id.wechat_scope)
    private EditText scopeEt;

    private IWXAPI api;

    private static final int MMAlertSelect1 = 0;
    private static final int MMAlertSelect2 = 1;
    private static final int MMAlertSelect3 = 2;

    private static final int THUMB_SIZE = 150;

    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, WechatConstants.APP_ID);

        init();
    }

    private void init() {
        checkBox.setChecked(false);
    }

    @Event(value = {R.id.wechat_send_text, R.id.wechat_send_img, R.id.wechat_send_music, R.id.wechat_send_video, R.id.wechat_send_webpage, R.id.wechat_get_token, R.id.wechat_unregister, R.id.wechat_fav_img, R.id.wechat_fav_file})
    private void send(View view) {
        switch (view.getId()) {
            case R.id.wechat_send_text:
                text();//发送text
                break;
            case R.id.wechat_send_img:
                img();//发送img
                break;
            case R.id.wechat_send_music:
                music();//发送Music
                break;
            case R.id.wechat_send_video:
                video();//发送Video
                break;
            case R.id.wechat_send_webpage:
                webpage();//发送网页
                break;
            case R.id.wechat_get_token:
                getToken(); //get Token
                break;
            case R.id.wechat_unregister:
                api.unregisterApp();
                break;
            case R.id.wechat_fav_img:
                favImg();//收藏图片
                break;
            case R.id.wechat_fav_file:
                favFile();//收藏文件
                break;
        }
    }


    private void favFile() {
        MMAlert.showAlert(SendToWXActivity.this, "收藏文件", SendToWXActivity.this.getResources().getStringArray(R.array.fav_file_item), null, new MMAlert.OnAlertSelectId() {
            @Override
            public void onClick(int whichButton) {
                switch (whichButton) {
                    case MMAlertSelect1: {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);
                        byte[] dataBuf = null;
                        try {
                            final ByteArrayOutputStream os = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.JPEG, 85, os);
                            dataBuf = os.toByteArray();
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        WXFileObject fileObject = new WXFileObject(dataBuf);

                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = fileObject;

                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                        bmp.recycle();
                        msg.thumbData = WechatUtil.bmpToByteArray(thumbBmp, true);//缩略图

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("file");
                        req.message = msg;
                        req.scene = SendMessageToWX.Req.WXSceneFavorite;
                        req.openId = getOpenId();
                        api.sendReq(req);
                        finish();
                        break;
                    }
                    case MMAlertSelect2: {
                        String path = SDCARD_ROOT + "/test.jpg";
                        File file = new File(path);
                        if (!file.exists()) {
                            String tip = SendToWXActivity.this.getString(R.string.send_img_file_not_exist);
                            Toast.makeText(SendToWXActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
                            break;
                        }

                        WXFileObject fileObject = new WXFileObject();
                        fileObject.setFilePath(path);

                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = fileObject;

                        Bitmap bmp = BitmapFactory.decodeFile(path);
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                        bmp.recycle();
                        msg.thumbData = WechatUtil.bmpToByteArray(thumbBmp, true);

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("file");
                        req.message = msg;
                        req.scene = SendMessageToWX.Req.WXSceneFavorite;
                        req.openId = getOpenId();
                        api.sendReq(req);

                        finish();
                        break;
                    }
                }
            }
        });
    }

    private void favImg() {
        MMAlert.showAlert(SendToWXActivity.this, "收藏图片", SendToWXActivity.this.getResources().getStringArray(R.array.fav_img_item), null, new MMAlert.OnAlertSelectId() {
            @Override
            public void onClick(int whichButton) {
                switch (whichButton) {
                    case MMAlertSelect1: {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);
                        WXImageObject object = new WXImageObject(bmp);

                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = object;

                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                        bmp.recycle();
                        msg.thumbData = WechatUtil.bmpToByteArray(thumbBmp, true);//设置缩略图

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("img");
                        req.message = msg;
                        req.scene = SendMessageToWX.Req.WXSceneFavorite;
                        req.openId = getOpenId();
                        api.sendReq(req);

                        finish();

                        break;
                    }
                    case MMAlertSelect2: {
                        String path = SDCARD_ROOT + "/test.png";
                        File file = new File(path);
                        if (!file.exists()) {
                            String tip = SendToWXActivity.this.getString(R.string.send_img_file_not_exist);
                            Toast.makeText(SendToWXActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
                            break;
                        }

                        WXImageObject imgObj = new WXImageObject();
                        imgObj.setImagePath(path);

                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = imgObj;

                        Bitmap bmp = BitmapFactory.decodeFile(path);
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                        bmp.recycle();
                        msg.thumbData = WechatUtil.bmpToByteArray(thumbBmp, true);

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("img");
                        req.message = msg;
                        req.scene = SendMessageToWX.Req.WXSceneFavorite;
                        req.openId = getOpenId();
                        api.sendReq(req);

                        finish();

                        break;
                    }
                }
            }
        });
    }


    private void webpage() {
        MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_webpage),
                SendToWXActivity.this.getResources().getStringArray(R.array.send_webpage_item),
                null, new MMAlert.OnAlertSelectId() {

                    @Override
                    public void onClick(int whichButton) {
                        switch (whichButton) {
                            case MMAlertSelect1:
                                WXWebpageObject webpage = new WXWebpageObject();
                                webpage.webpageUrl = "http://www.baidu.com";
                                WXMediaMessage msg = new WXMediaMessage(webpage);
                                msg.title = "WebPage Title";
                                msg.description = "WebPage Description";
                                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                                msg.thumbData = WechatUtil.bmpToByteArray(thumb, true);

                                SendMessageToWX.Req req = new SendMessageToWX.Req();
                                req.transaction = buildTransaction("webpage");
                                req.message = msg;
                                req.scene = checkBox.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                                req.openId = getOpenId();
                                api.sendReq(req);

                                finish();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    private void video() {
        MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_video),
                SendToWXActivity.this.getResources().getStringArray(R.array.send_video_item),
                null, new MMAlert.OnAlertSelectId() {

                    @Override
                    public void onClick(int whichButton) {
                        switch (whichButton) {
                            case MMAlertSelect1: {
                                WXVideoObject video = new WXVideoObject();
                                video.videoUrl = "http://www.baidu.com";

                                WXMediaMessage msg = new WXMediaMessage(video);
                                msg.title = "Video Title";
                                msg.description = "Video Description";
                                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                                msg.thumbData = WechatUtil.bmpToByteArray(thumb, true);

                                SendMessageToWX.Req req = new SendMessageToWX.Req();
                                req.transaction = buildTransaction("video");
                                req.message = msg;
                                req.scene = checkBox.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                                req.openId = getOpenId();
                                api.sendReq(req);

                                finish();
                                break;
                            }
                            case MMAlertSelect2: {
                                WXVideoObject video = new WXVideoObject();
                                video.videoLowBandUrl = "http://www.qq.com";

                                WXMediaMessage msg = new WXMediaMessage(video);
                                msg.title = "Video Title";
                                msg.description = "Video Description";

                                SendMessageToWX.Req req = new SendMessageToWX.Req();
                                req.transaction = buildTransaction("video");
                                req.message = msg;
                                req.scene = checkBox.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                                req.openId = getOpenId();
                                api.sendReq(req);

                                finish();
                                break;
                            }
                            default:
                                break;
                        }
                    }
                });
    }

    private void music() {
        MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_music),
                SendToWXActivity.this.getResources().getStringArray(R.array.send_music_item),
                null, new MMAlert.OnAlertSelectId() {

                    @Override
                    public void onClick(int whichButton) {
                        switch (whichButton) {
                            case MMAlertSelect1: {
                                WXMusicObject music = new WXMusicObject();
                                //music.musicUrl = "http://www.baidu.com";
                                music.musicUrl = "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
                                //music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";

                                WXMediaMessage msg = new WXMediaMessage();
                                msg.mediaObject = music;
                                msg.title = "Music Title";
                                msg.description = "Music Album";

                                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                                msg.thumbData = WechatUtil.bmpToByteArray(thumb, true);

                                SendMessageToWX.Req req = new SendMessageToWX.Req();
                                req.transaction = buildTransaction("music");
                                req.message = msg;
                                req.scene = checkBox.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                                req.openId = getOpenId();
                                api.sendReq(req);

                                finish();
                                break;
                            }
                            case MMAlertSelect2: {
                                WXMusicObject music = new WXMusicObject();
                                music.musicLowBandUrl = "http://www.qq.com";

                                WXMediaMessage msg = new WXMediaMessage();
                                msg.mediaObject = music;
                                msg.title = "Music Title";
                                msg.description = "Music Album";

                                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                                msg.thumbData = WechatUtil.bmpToByteArray(thumb, true);

                                SendMessageToWX.Req req = new SendMessageToWX.Req();
                                req.transaction = buildTransaction("music");
                                req.message = msg;
                                req.scene = checkBox.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                                req.openId = getOpenId();
                                api.sendReq(req);

                                finish();
                                break;
                            }
                            default:
                                break;
                        }
                    }
                });
    }

    private void img() {
        MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_img), SendToWXActivity.this.getResources().getStringArray(R.array.send_img_item), null, new MMAlert.OnAlertSelectId() {
            @Override
            public void onClick(int whichButton) {
                switch (whichButton) {
                    case MMAlertSelect1:
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);
                        //初始化Image对象
                        WXImageObject imageObject = new WXImageObject(bmp);

                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = imageObject;

                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                        bmp.recycle();
                        msg.thumbData = WechatUtil.bmpToByteArray(thumbBmp, true);//设置缩略图

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("img");
                        req.message = msg;
                        req.scene = checkBox.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        req.openId = getOpenId();
                        api.sendReq(req);
                        finish();
                        break;
                    case MMAlertSelect2:
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "test.png";
                        File file = new File(path);
                        if (!file.exists()) {
                            String tip = SendToWXActivity.this.getString(R.string.send_img_file_not_exist);
                            Toast.makeText(SendToWXActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
                            break;
                        }

                        WXImageObject imageObject2 = new WXImageObject();
                        imageObject2.setImagePath(path);

                        WXMediaMessage msg2 = new WXMediaMessage();
                        msg2.mediaObject = imageObject2;

                        Bitmap bmp2 = BitmapFactory.decodeFile(path);
                        Bitmap thumbBmp2 = Bitmap.createScaledBitmap(bmp2, THUMB_SIZE, THUMB_SIZE, true);
                        bmp2.recycle();
                        msg2.thumbData = WechatUtil.bmpToByteArray(thumbBmp2, true);

                        SendMessageToWX.Req req2 = new SendMessageToWX.Req();
                        req2.transaction = buildTransaction("img");
                        req2.message = msg2;
                        req2.scene = checkBox.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        req2.openId = getOpenId();
                        api.sendReq(req2);

                        finish();
                        break;
                    case MMAlertSelect3:
                        String url = "http://b.hiphotos.baidu.com/image/pic/item/6a63f6246b600c33fe5527171e4c510fd8f9a1c5.jpg";
                        try {
                            WXImageObject imgObj = new WXImageObject();
                            imgObj.imagePath = url;

                            WXMediaMessage msg3 = new WXMediaMessage();
                            msg3.mediaObject = imgObj;

                            Bitmap bmp3 = BitmapFactory.decodeStream(new URL(url).openStream());
                            Bitmap thumbBmp3 = Bitmap.createScaledBitmap(bmp3, THUMB_SIZE, THUMB_SIZE, true);
                            bmp3.recycle();
                            msg3.thumbData = WechatUtil.bmpToByteArray(thumbBmp3, true);

                            SendMessageToWX.Req req3 = new SendMessageToWX.Req();
                            req3.transaction = buildTransaction("img");
                            req3.message = msg3;
                            req3.scene = checkBox.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                            req3.openId = getOpenId();
                            api.sendReq(req3);

                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void text() {
        final EditText editText = new EditText(SendToWXActivity.this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setText("这段文字发送自微信SDK发送文本Text测试");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发送文本");
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = editText.getText().toString();
                if (text == null || text.length() == 0) {
                    return;
                }

                //初始化一个WXTextObject对象
                WXTextObject textObject = new WXTextObject();
                textObject.text = text;

                //用WXTextObject对象初始化一个WXMediaMessage对象
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObject;
                //发送文本类型的消息时，title字段不起作用·
//                msg.title = "Will be ignored";
                msg.description = text;

                //构造一个Req
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("text");//transaction字段用于唯一标识一个请求
                req.message = msg;
                req.scene = checkBox.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;//此处判断是否分享到微信朋友圈
                req.openId = getOpenId();
                //调用api接口发送数据到微信
                api.sendReq(req);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public String getOpenId() {
        return openid.getText().toString();
    }

    public void getToken() {
        String scope = scopeEt.getText().toString();
        if (scope == null || scope.length() == 0) {
            scope = "snsapi_userinfo";
        }

        //send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = scope;
        req.state = "none";
        req.openId = getOpenId();
        api.sendReq(req);
        finish();

    }
}
