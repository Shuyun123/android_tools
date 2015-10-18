

import android.content.Context;

/**
 * Created by anumbrella on 15-10-18.
 * <p/>
 * 数据转换工具
 */
public class DataUtils {


    /**
     * dip 转 px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * px 转 dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
