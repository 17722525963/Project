package com.run.running.frag;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.run.runlibrary.utils.ToastUtil;
import com.run.running.R;
import com.run.running.activity.html5.Html5Activity;
import com.run.running.app.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/7/11.
 */
@ContentView(R.layout.frag_html5)
public class Html5Fragment extends BaseFragment {

    @Event(R.id.html5_startaty)
    private void start(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.html5_startaty:
                intent.setClass(getContext(), Html5Activity.class);
                break;
            default:
                ToastUtil.showToast(getContext(),"暂未完成~");
                break;
        }
        startActivity(intent);
    }

}
