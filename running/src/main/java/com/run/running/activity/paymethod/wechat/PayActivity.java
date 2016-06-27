package com.run.running.activity.paymethod.wechat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.run.runlibrary.utils.ToastUtil;
import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.run.running.constants.WechatConstants;
import com.run.running.utils.http_xutil.MyCallBack;
import com.run.running.utils.http_xutil.XUtil;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 微信支付界面
 * Created by zsr on 2016/6/13.
 */
@ContentView(R.layout.activity_pay_wechat)
public class PayActivity extends BaseActivity {

    private IWXAPI api;

    @ViewInject(R.id.pay_detail)
    private TextView detail;


    //////////////////////////////////////////////////////////////////////////////////
    //微信公众平台id
    private String app_wx_appid = WechatConstants.APP_ID;

    //微信开放平台和商户约定的密钥
    private String app_wx_secret_key = "db426a9829e4b49a0dcac7b4162da6b6";

    //微信公众平台商户模块和商户约定的密钥
    private String app_wx_parent_key = "8934e7d15453e97507ef794cf7b0519d";

    //微信公众平台商户模块和商户约定的支付密钥
    private String app_wx_pay_key = "L8LrMqqeGRxST5reouB0K66CaYAWpqhAVsq7ggKkxHCOastWksvuX1uvmvQclxaHoYd3ElNBrNO2DHnnzgfVG9Qs473M3DTOZug5er46FhuGofumV8H2FVR9qkjSlC5K";

    //商家向财付通申请的商家id */
    private String app_tx_parent_key = "1900000109";

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");//APP_ID为测试APP_ID

    }

    @Event(value = {R.id.wechat_pay_start, R.id.wechat_pay_check, R.id.wechat_pay_open})
    private void pay(View view) {
        switch (view.getId()) {
            case R.id.wechat_pay_start:
                startPay();//服务端签名微信支付
                break;
            case R.id.wechat_pay_check:
                //检查版本是否支持支付
                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
                break;
            case R.id.wechat_pay_open:
                openPay();//使用weixinDemo中的debug_keystore 调起微信支付
                break;
        }
    }

    private void openPay() {
//        1 获取accessToken，accessToken的值
//         解析服务器响应
//        2 根据第一步的accesstoken值将组装的商品参数post给微信服务器
//        3 在WXEntryActivity处理返回结果
    }


    private void startPay() {
        String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
        ToastUtil.showToast(PayActivity.this, "订单获取中···");
        XUtil.get(url, null, new MyCallBack<Object>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                super.onError(ex, isOnCallback);
//                detail.setText("服务器请求错误~" + ex.getMessage());
//            }

            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                try {
                    String content = new String(result.toString());
                    JSONObject json = new JSONObject(content);
                    if (null != json && !json.has("retcode")) {
                        PayReq req = new PayReq();
                        req.appId = json.getString("appid");//APPID 微信开放平台审核通过的应用APPID
                        req.partnerId = json.getString("partnerid");//商户号 微信支付分配的商户号
                        req.prepayId = json.getString("prepayid");//预支付交易会话ID 微信返回的支付交易会话ID
                        req.nonceStr = json.getString("noncestr");//随机字符串 随机字符串，不长于32位。
                        req.timeStamp = json.getString("timestamp");//时间戳
                        req.packageValue = json.getString("package");//扩展字段
                        req.sign = json.getString("sign");//签名
                        req.extData = "app data";//随意
                        ToastUtil.showToast(PayActivity.this, "正常调起支付");
                        // 在支付前，需要先将应用注册到微信，

                        System.out.println("=================" + json.getString("prepayid"));
                        System.out.println("=================" + json.getString("noncestr"));
                        System.out.println("=================" + json.getString("timestamp"));
                        System.out.println("=================" + json.getString("appid"));
                        System.out.println("=================" + json.getString("package"));

                        api.sendReq(req);
                    } else {
                        detail.setText("返回错误" + json.getString("retmsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
//
//
//{"appid":"wxb4ba3c02aa476ea1"
//        ,"partnerid":"1305176001"
//        ,"package":"Sign=WXPay"
//        ,"noncestr":"d035aebb2be22ed3ccd84601ee5d4629"
//        ,"timestamp":1465884830
//        ,"prepayid":"wx20160614141350146d59e4630619371459"
//        ,"sign":"D1F913274599BE2168DE9A32C5C1808D"}
