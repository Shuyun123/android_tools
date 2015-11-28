

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anumbrella on 15-10-18.
 * <p/>
 * 时间格式转换类
 */
public class DateUtils {

    /**
     * 将时间戳转换为时间字符串   格式: yyyy-MM-dd HH:mm:ss
     *
     * @param time 秒
     * @return
     */
    public static String getStrTime_ymd_hms(String time) {

        String re_timeStr = "";
        if (TextUtils.isEmpty(time) || "null".equals(time)) {
            return re_timeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lTime = Long.valueOf(time);
        re_timeStr = sdf.format(new Date(lTime * 1000L));
        return re_timeStr;
    }


    /**
     * 将时间戳转换为时间字符串   格式: yyyy-MM-DD HH:mm
     *
     * @param time 秒
     * @return
     */
    public static String getStrTime_ymd_hm(String time) {

        String re_timeStr = "";
        if (TextUtils.isEmpty(time) || "null".equals(time)) {
            return re_timeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lTime = Long.valueOf(time);
        re_timeStr = sdf.format(new Date(lTime * 1000L));
        return re_timeStr;
    }


    /**
     * 将时间戳转换为时间字符串   格式: yyyy.MM.dd
     *
     * @param time 秒
     * @return
     */
    public static String getStrTime_ymd(String time) {

        String re_timeStr = "";
        if (TextUtils.isEmpty(time) || "null".equals(time)) {
            return re_timeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        long lTime = Long.valueOf(time);
        re_timeStr = sdf.format(new Date(lTime * 1000L));
        return re_timeStr;
    }


    /**
     * 将时间戳转换为时间字符串   格式: yyyy
     *
     * @param time 秒
     * @return
     */
    public static String getStrTime_y(String time) {

        String re_timeStr = "";
        if (TextUtils.isEmpty(time) || "null".equals(time)) {
            return re_timeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        long lTime = Long.valueOf(time);
        re_timeStr = sdf.format(new Date(lTime * 1000L));
        return re_timeStr;
    }


    /**
     * 将时间戳转换为时间字符串   格式:MM-dd
     *
     * @param time 秒
     * @return
     */
    public static String getStrTime_md(String time) {

        String re_timeStr = "";
        if (TextUtils.isEmpty(time) || "null".equals(time)) {
            return re_timeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-DD");
        long lTime = Long.valueOf(time);
        re_timeStr = sdf.format(new Date(lTime * 1000L));
        return re_timeStr;
    }

    /**
     * 将时间戳转换为时间字符串   格式:HH:mm
     *
     * @param time 秒
     * @return
     */
    public static String getStrTime_hm(String time) {

        String re_timeStr = "";
        if (TextUtils.isEmpty(time) || "null".equals(time)) {
            return re_timeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long lTime = Long.valueOf(time);
        re_timeStr = sdf.format(new Date(lTime * 1000L));
        return re_timeStr;
    }


    /**
     * 将时间戳转换为时间字符串   格式: yyyy-MM-DD HH:mm:ss
     *
     * @param time 秒
     * @return
     */
    public static String getStrTime_hms(String time) {

        String re_timeStr = "";
        if (TextUtils.isEmpty(time) || "null".equals(time)) {
            return re_timeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long lTime = Long.valueOf(time);
        re_timeStr = sdf.format(new Date(lTime * 1000L));
        return re_timeStr;
    }

    /**
     * 将时间戳转换为时间字符串   格式: MM-dd HH:mm:ss
     *
     * @param time 秒
     * @return
     */
    public static String getStrTime_md_hms(String time) {

        String re_timeStr = "";
        if (TextUtils.isEmpty(time) || "null".equals(time)) {
            return re_timeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        long lTime = Long.valueOf(time);
        re_timeStr = sdf.format(new Date(lTime * 1000L));
        return re_timeStr;
    }


    /**
     * 将当前时间转换为时间戳字符串 （10位数）
     *
     * @return 时间戳(秒)
     */
    public static String getTime_second() {

        String re_time = "";
        //获取当前时间的毫秒
        long currentTime = System.currentTimeMillis();
        String longTime = String.valueOf(currentTime);
        //获取前10位,转换为秒(后3位(000)舍弃)
        re_time = longTime.substring(0, 10);
        return re_time;
    }


    /**
     * 将当前时间转换为时间戳字符串
     *
     * @return 时间戳(毫秒)
     */
    public static String getTime_timeStamp() {

        String re_time = "";
        //获取当前时间的毫秒
        long currentTime = System.currentTimeMillis();
        String longTime = String.valueOf(currentTime);
        re_time = longTime;
        return re_time;
    }

    /**
     * 将时间戳转换为字符串时间 格式:yyyy.MM.dd 星期几
     *
     * @param time 秒
     * @return
     */
    public static String getStrTimeSection(String time) {
        String re_time = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd EEEE");
        //对于创建SimpleDateFormat传入的参数：EEEE代表星期，如“星期四”；
        // MMMM代表中文月份，如“十一月”；MM代表月份，如“11”；
        //yyyy代表年份，如“2010”；dd代表天，如“25”
        long lTime = Long.valueOf(time);
        re_time = sdf.format(new Date(lTime * 1000L));
        return re_time;
    }
}
