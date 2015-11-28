

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StatFs;

/**
 * @author anumbrella
 * @date 2015-8-24 下午1:50:45
 * <p/>
 * SDCard相关辅助类
 */
public class SDCardUtils {

    private SDCardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获得SDCard的路径
     *
     * @return
     */
    public static String getSDCardPath() {
        if(isSDCardEnable()){
            return Environment.getExternalStorageDirectory()
                    + File.separator;
        }
        return null;
    }

    /**
     * 获取SDCard剩余容量大小(单位:byte)
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static long getAvaliableSDCardSize() {
        if (isSDCardEnable()) {
            StatFs SDCardInfo = new StatFs(getSDCardPath());
            // 获取空闲的数据块
            long availableBlocks = (long) SDCardInfo.getAvailableBlocksLong();
            // 获取每块数据块的大小
            long blockSize = SDCardInfo.getBlockSizeLong();
            return blockSize * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取SDCard总的容量大小(单位:byte)
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static long getAllSDCardSize() {
        if (isSDCardEnable()) {
            StatFs SDCardInfo = new StatFs(getSDCardPath());
            // 获取所有的数据块
            long allBlocks = (long) SDCardInfo.getBlockCountLong();
            // 获取每块数据块的大小
            long blockSize = SDCardInfo.getBlockSizeLong();
            return blockSize * allBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量(单位:byte)
     *
     * @param filePath 文件路径
     * @return 容量字节, 可用空间的大小
     */
    @SuppressLint("NewApi")
    public static long getFreeBytes(String filePath) {

        // 如果是SDCard下的路径,则获取SDCard的可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {
            // 如果是内部存储路径,则获取内部存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs status = new StatFs(filePath);
        long availableBlocks = status.getAvailableBlocksLong();
        return availableBlocks * status.getBlockSizeLong();
    }

    /**
     * 获取手机内存的总容量
     *
     * @return
     */

    @SuppressLint("NewApi")
    public static long getRomTotalSize() {

        String filePath = Environment.getDataDirectory().getAbsolutePath();
        StatFs status = new StatFs(filePath);
        long allBlocks = status.getBlockCountLong();
        return allBlocks * status.getBlockSizeLong();
    }


    /**
     * 获取系统存储路径(根路径(/system))
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

}
