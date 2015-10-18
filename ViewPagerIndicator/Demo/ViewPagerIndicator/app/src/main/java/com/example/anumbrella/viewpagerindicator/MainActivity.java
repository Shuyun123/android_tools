package com.example.anumbrella.viewpagerindicator;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //获取从某个activity传过来的intent
        Intent intent = getIntent();

        //获取其中的数据
        String path = intent.getStringExtra("net.anumbrella.viewpagerindicator.example.PATH");

        if (path == null) {
            path = "";
        }

        //给ListActivity中的listView添加适配数据
        setListAdapter(new SimpleAdapter(this, getData(path), android.R.layout.simple_list_item_1,
                new String[]{"title"}, new int[]{android.R.id.text1}));

        //启动文本过滤
        getListView().setTextFilterEnabled(true);


    }


    /**
     * 匹配数据
     *
     * @param prefix
     * @return
     */
    private List<Map<String, Object>> getData(String prefix) {

        List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();


        //开始匹配AndroidManifest.xml中的activity数据
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);

        mainIntent.addCategory("net.anumbrella.android.viewpagerindicator.sample.SAMPLE");

        //获取包管理器,以便获取app的相关信息
        PackageManager pm = getPackageManager();

        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (list == null) {
            return myData;
        }

        String[] prefixPath;
        String prefixWithSlash = prefix;


        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            //如:prefix = Circles,则prefixPath = Circles,prefixPath.length = 1;
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }

        //这个长度是固定不变的
        int len = list.size();

        //判断是否添加到myData的标识
        Map<String, Boolean> entries = new HashMap<String, Boolean>();

        //开始遍历数据
        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);

            //获取AndroidManifest.xml中activity定义的label标签的值
            CharSequence labelSeq = info.loadLabel(pm);

            String label = (labelSeq != null ? labelSeq.toString() : info.activityInfo.name);

            if (prefixWithSlash.length() == 0
                    || label.startsWith(prefixWithSlash)) {
                //将label的值分成数组,如:label = Circles/Default，
                String[] labelPath = label.split("/");

                /**
                 * 如果:label = Circles/Default，prefixPath == null，
                 * 则nextLabel = Circles，否则 nextLabel = Default
                 */
                String nextLabel = (prefixPath == null ? labelPath[0] : labelPath[prefixPath.length]);

                //(true or false ? 1:0== 1)
                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {

                    addItem(myData, nextLabel,
                            activityIntent(info.activityInfo.packageName, info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        addItem(myData, nextLabel,
                                browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }

                }
            }

        }


        // 按照字母的大小写顺序进行排序
        Collections.sort(myData, NAME_COMPARATOR);
        return myData;
    }


    /**
     * 对字母大小(abcd)进行排序
     */
    private final static Comparator<Map<String, Object>> NAME_COMPARATOR = new Comparator<Map<String, Object>>() {

        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(Map<String, Object> map1, Map<String, Object> map2) {
            return collator.compare(map1.get("title"), map2.get("title"));
        }
    };


    /**
     * 添加点击跳转的intent
     *
     * @param name
     * @return
     */
    private Intent browseIntent(String name) {

        Intent result = new Intent();
        result.setClass(this, MainActivity.class);
        //给当前页面传递的数据
        result.putExtra("net.anumbrella.viewpagerindicator.example.PATH", name);
        return result;
    }

    /**
     * 添加点击跳转的intent
     *
     * @param packageName
     * @param componentName
     */
    private Intent activityIntent(String packageName, String componentName) {
        Intent result = new Intent();
        result.setClassName(packageName, componentName);
        return result;
    }

    /**
     * 添加数据到myData当中
     *
     * @param myData
     * @param nextLabel
     * @param intent
     */
    private void addItem(List<Map<String, Object>> myData, String nextLabel, Intent intent) {

        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("title", nextLabel);
        tmp.put("intent", intent);
        myData.add(tmp);
    }

    /**
     * 给listView的每个item添加点击跳转事件
     *
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }
}
