package com.run.running.frag;

import android.content.Intent;
import android.view.View;

import com.run.running.R;
import com.run.running.activity.chart.HolleChartsActivity;
import com.run.running.activity.chart.MvpAndroidChartsActivity;
import com.run.running.app.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * 图表开源库 练习
 * Created by zsr on 2016/6/8.
 */
@ContentView(R.layout.frag_chart)
public class ChartFragment extends BaseFragment {

    @Event(value = {R.id.chart_hellocharts, R.id.chart_mvpandroidchart})
    private void chart(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.chart_hellocharts:
                intent.setClass(getActivity(), HolleChartsActivity.class);
                break;
            case R.id.chart_mvpandroidchart:
                intent.setClass(getActivity(), MvpAndroidChartsActivity.class);
                break;
        }
        startActivity(intent);
    }

}
