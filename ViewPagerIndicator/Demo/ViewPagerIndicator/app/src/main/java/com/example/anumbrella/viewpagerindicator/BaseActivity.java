package com.example.anumbrella.viewpagerindicator;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.example.anumbrella.viewpager.PagerIndicator;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragmentAdapter;

import java.util.Random;

/**
 * Created by anumbrella on 15-10-18.
 * <p/>
 * 菜单选项操作activity
 */
public class BaseActivity extends FragmentActivity {

    /**
     * 随机数生成对象
     */
    private static final Random RANDOM = new Random();

    protected TestFragmentAdapter adapter;
    protected ViewPager mPager;
    protected PagerIndicator mIndicator;

    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * 菜单选中操作
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.random:
                //生成随机数
                final int page = RANDOM.nextInt(adapter.getCount());
                mPager.setCurrentItem(page);
                return true;
            case R.id.add:
                if (adapter.getCount() < 10) {
                    adapter.setCount(adapter.getCount() + 1);
                    mIndicator.notifyDataSetChanged();
                }
                return true;
            case R.id.remove:
                if (adapter.getCount() < 10) {
                    adapter.setCount(adapter.getCount() - 1);
                    mIndicator.notifyDataSetChanged();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
