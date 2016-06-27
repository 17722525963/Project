package com.run.running.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.run.runlibrary.BaseV4Fragment;
import com.run.running.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zsr on 2016/5/11.
 */
@ContentView(R.layout.fragment_content)
public class ContentFragment extends BaseV4Fragment {

    @ViewInject(R.id.tv)
    private TextView view;

    private static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static ContentFragment newInstance(int page) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view.setText("Fragment" + mPage);
    }
}
