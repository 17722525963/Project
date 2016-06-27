package com.run.running.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.run.runlibrary.utils.ToastUtil;
import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.run.running.activity.paymethod.wechat.GetFromWXActivity;
import com.run.running.activity.paymethod.wechat.SendToWXActivity;
import com.run.running.activity.paymethod.wechat.ShowFromWXActivity;
import com.run.running.constants.WechatConstants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 微信 入口 返回结果处理
 * Created by zsr on 2016/6/13.
 */
@ContentView(R.layout.activity_wechatpay)
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @ViewInject(R.id.wechat_detail)
    private TextView wechatDetail;

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    //APP_ID
    private static final String APP_ID = WechatConstants.APP_ID;

    //IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);//将应用的appId注册到微信

        api.handleIntent(getIntent(), this);//在WXEntryActivity中将接收到的intent及实现了IWXAPIEventHandler接口的对象传递给IWXAPI接口的handleIntent方法
    }

    @Event(value = {R.id.wechat_send, R.id.wechat_pay, R.id.launch_wechat, R.id.wechat_check})
    private void wechat(View view) {
        switch (view.getId()) {
            case R.id.wechat_send:
                gotoSend();//跳转到发送消息界面
                break;
            case R.id.wechat_pay:
//                startActivity(new Intent(WXEntryActivity.this, PayActivity.class));//跳转到微信支付界面
                ToastUtil.showToast(WXEntryActivity.this,"缺少条件，未完成~");
                break;
            case R.id.launch_wechat:
                //启动微信客户端
                ToastUtil.showToast(WXEntryActivity.this, "launch result = " + api.openWXApp());
                break;
            case R.id.wechat_check:
                //检查是否支持发送到朋友圈
                int wxSdkVersion = api.getWXAppSupportAPI();
                if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
                    wechatDetail.setText("wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline 支持");
                } else {
                    wechatDetail.setText("wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline 不支持");
                }
                break;
        }
    }

    private void gotoSend() {
        startActivity(new Intent(WXEntryActivity.this, SendToWXActivity.class));
    }

    /**
     * onCreate是用来创建一个Activity也就是创建一个窗体，
     * 但一个Activty处于任务栈的顶端，若再次调用startActivity去创建它，
     * 则不会再次创建。
     * 若你想利用已有的Acivity去处理别的Intent时，
     * 你就可以利用onNewIntent来处理。
     * 在onNewIntent里面就会获得新的Intent.
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleIntent(intent, this);
    }

    /**
     * 微信发送请求到第三方应用时，会回调到该方法
     *
     * @param baseReq
     */
    @Override
    public void onReq(BaseReq baseReq) {
        ToastUtil.showToast(this, "oppenId = " + baseReq.openId);
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) baseReq);
                break;
            case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
                ToastUtil.showToast(this, "Launch from wechat~");
                break;
            default:
                break;
        }
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject object = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer();//组织一个待显示的消息内容
        msg.append("description:");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("exInfo:");
        msg.append(object.extInfo);
        msg.append("\n");
        msg.append("filePath:");
        msg.append(object.filePath);

        Intent intent = new Intent(this, ShowFromWXActivity.class);
        intent.putExtra("showmsg_title", wxMsg.title);
        intent.putExtra("showmsg_message", msg.toString());
        intent.putExtra("showmsg_thumb_data", wxMsg.thumbData);
        startActivity(intent);
        finish();
    }

    private void goToGetMsg() {
        Intent intent = new Intent(this, GetFromWXActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
        finish();
    }

    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        ToastUtil.showToast(this, "openid = " + resp.openId);
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            ToastUtil.showToast(this, "code=" + ((SendAuth.Resp) resp).code);
        }

        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }
        ToastUtil.showToast(this, result);
    }
}
