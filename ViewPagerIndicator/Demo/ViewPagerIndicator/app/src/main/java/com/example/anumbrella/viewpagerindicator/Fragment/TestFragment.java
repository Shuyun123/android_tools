package com.example.anumbrella.viewpagerindicator.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by anumbrella on 15-10-5.
 */
public class TestFragment extends Fragment {

    /**
     * 内容存放key
     */
    private final String KEY_CONTENT = "TestFragment:Content";


    /**
     * 页面存放内容
     */
    private String mContent = null;


    public static TestFragment newInstance(String content) {
        TestFragment fragment = new TestFragment();
        fragment.mContent = content;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        if ((savedIntanceState != null) && savedIntanceState.containsKey(KEY_CONTENT)) {
            mContent = savedIntanceState.getString(KEY_CONTENT);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView text = new TextView(getActivity());
        text.setGravity(Gravity.CENTER);
        text.setText(mContent);
        text.setTextSize(20 * getResources().getDisplayMetrics().density);

        text.setPadding(20, 20, 20, 20);

        LinearLayout layout = new LinearLayout(getActivity());

        layout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT)
        );
        layout.setGravity(Gravity.CENTER);
        layout.addView(text);
        return layout;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(KEY_CONTENT, mContent);
    }


}
