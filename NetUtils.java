
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author anumbrella
 * 
 * @date 2015-8-26 上午8:54:08
 * 
 *       网络相关辅助类
 */
public class NetUtils {

	private NetUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断手机网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CAPTIONING_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 判断wifi是否是可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CAPTIONING_SERVICE);
		if (connectivityManager == null) {
			return false;
		}

		return (connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
	}

	/**
	 * 打开手机设置页面
	 * 
	 * @param activity
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName componentName = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(componentName);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}
	
	
	/**
	 * 获取当前网络连接的类型信息
	 * 
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			// 如果当前的网络可以使用，返回当前网络的类型
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}

		return -1;
	}

}
