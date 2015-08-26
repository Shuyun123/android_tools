
import android.content.Context;
import android.widget.Toast;

/**
 * @author anumbrella
 * 
 * @date 2015-8-26 上午10:05:00
 * 
 *       Toast统一管理类
 */
public class T {

	/**
	 * 是否显示Toast的消息
	 */
	public static boolean isShow = true;

	private T() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message   charsequence类型
	 *            
	 */
	public static void showShort(Context context, CharSequence message) {
		if (isShow) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message    int类型
	 *           
	 */
	public static void showShort(Context context, int message) {
		if (isShow) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message   charsequence类型
	 *            
	 */
	public static void showLong(Context context, CharSequence message) {
		if (isShow) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message  int类型
	 *            
	 */
	public static void showLong(Context context, int message) {
		if (isShow) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 自定义显示时间Toast
	 * 
	 * @param context
	 * @param message   charsequence类型
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (isShow) {
			Toast.makeText(context, message, duration).show();
		}
	}
	
	/**
	 * 自定义显示时间Toast
	 * 
	 * @param context
	 * @param message   int类型
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		if (isShow) {
			Toast.makeText(context, message, duration).show();
		}
	}


}
