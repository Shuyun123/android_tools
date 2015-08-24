
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Environment;
import android.util.Log;

/**
 * @author anumbrella
 * 
 * @date 2015-8-24 下午2:58:27
 * 
 *       Logcat统一管理类
 */
public class L {

	/**
	 * 是否开启日志,可在onCreate()方法初始化
	 */
	public static boolean isDubug = true;

	/**
	 * 是否开启日志写入文件,可在onCreate()方法初始化
	 */
	public static boolean isRecord = false;

	/**
	 * TAG的默认值
	 */
	private static String TAG = "anumbrella";

	/**
	 * 当前应用程序的名称(使用时更改为自己的应用程序名)
	 */
	private static String APPName = "anumbrella";

	/**
	 * 日志记录的文件夹
	 */
	private static String LOG_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ APPName
			+ File.separator
			+ "log" + File.separator;

	/**
	 * 各个日志类型存储地址目录
	 * 
	 */
	private static final String PATH_LOG_INFO = LOG_PATH + "info"
			+ File.separator;
	private static final String PATH_LOG_DEBUG = LOG_PATH + "debug"
			+ File.separator;
	private static final String PATH_LOG_VERBOSE = LOG_PATH + "verbose"
			+ File.separator;
	private static final String PATH_LOG_ERROR = LOG_PATH + "error"
			+ File.separator;

	public L() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	// 默认的四个日志记录方法
	public static void d(String message) {
		if (isDubug) {
			Log.d(TAG, message);
		}
		if (isRecord) {
			logRecord(PATH_LOG_DEBUG, TAG, message);
		}
	}

	public static void i(String message) {
		if (isDubug) {
			Log.i(TAG, message);
		}
		if (isRecord) {
			logRecord(PATH_LOG_INFO, TAG, message);
		}
	}

	public static void e(String message) {
		if (isDubug) {
			Log.e(TAG, message);
		}
		if (isRecord) {
			logRecord(PATH_LOG_ERROR, TAG, message);
		}
	}

	public static void v(String message) {
		if (isDubug) {
			Log.v(TAG, message);
		}
		if (isRecord) {
			logRecord(PATH_LOG_VERBOSE, TAG, message);
		}
	}

	// 以下为自定义的日志记录方法
	public static void d(String tag, String message) {
		if (isDubug) {
			Log.d(tag, message);
		}
		if (isRecord) {
			logRecord(PATH_LOG_DEBUG, tag, message);
		}
	}

	public static void i(String tag, String message) {
		if (isDubug) {
			Log.i(tag, message);
		}
		if (isRecord) {
			logRecord(PATH_LOG_INFO, tag, message);
		}
	}

	public static void e(String tag, String message) {
		if (isDubug) {
			Log.e(tag, message);
		}
		if (isRecord) {
			logRecord(PATH_LOG_ERROR, tag, message);
		}
	}

	public static void v(String tag, String message) {
		if (isDubug) {
			Log.v(tag, message);
		}
		if (isRecord) {
			logRecord(PATH_LOG_VERBOSE, tag, message);
		}
	}

	/**
	 * 日志记录文件方法(该方法需要使用SDCardUtils.class类工具的方法)
	 */
	private static void logRecord(String logPath, String tag, String message) {
		if (SDCardUtils.isSDCardEnable()) {
			Date date = new Date();
			SimpleDateFormat dataFormat = new SimpleDateFormat("",
					Locale.SIMPLIFIED_CHINESE);
			// 设置显示的时间格式
			dataFormat.applyPattern("yyyy");
			logPath += dataFormat.format(date) + File.separator;
			dataFormat.applyPattern("MM");
			logPath += dataFormat.format(date) + File.separator;
			dataFormat.applyPattern("dd");
			logPath += dataFormat.format(date) + ".log";
			dataFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
			String time = dataFormat.format(date);
			File file = new File(logPath);
			if (!file.exists()) {
				// 创建文件
				createFileByPath(logPath);
			}
			BufferedWriter out = null;
			try {
				// 参数true表示文件存在，不会删除，在原文件上追加内容
				out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file, true)));
				out.write(time + " " + tag + " " + message + "\r\n");
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据文件路径创建新文件
	 * 
	 * @param filePath
	 */
	private static void createFileByPath(String filePath) {
		String parentPath = filePath.substring(0,
				filePath.lastIndexOf(File.separator));
		File file = new File(filePath);
		File parentFile = new File(parentPath);
		if (!file.exists()) {
			parentFile.mkdirs();
		}
		try {
			// 生成新文件
			file.createNewFile();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
