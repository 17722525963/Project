package com.run.running.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.run.running.R;
import com.run.running.app.BaseFragment;
import com.run.running.wxapi.WXEntryActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * Created by zsr on 2016/5/18.
 */
@ContentView(R.layout.frag_paymethod)
public class PayMethodFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCallback.onPayMethodFragmentRefresh
// ();
    }

    @Event(value = {R.id.paymethod_wechat})
    private void paymethod(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.paymethod_wechat:
                intent.setClass(getContext(), WXEntryActivity.class);
                break;
        }
        startActivity(intent);
    }

}
