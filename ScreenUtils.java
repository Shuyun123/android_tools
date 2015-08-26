
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * @author anumbrella
 * 
 * @date 2015-8-24 上午11:07:32 获得屏幕相关辅助类
 */
public class ScreenUtils {

	private ScreenUtils() {
		/* can't be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {

		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {
		int statusHeight = -1;
		try {
			// 通过查找R文件下得dimen内部类
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			// 然后通过反射拿到dimen中的status_bar_height的值，这个值其实就是资源id，然后再通过getResource方法拿到该id对应的值
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return statusHeight;
	}

	/**
	 * 获取屏幕当前的截图,包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity) {

		// decorView是window中的最顶层view，可以从window中获取到decorView
		View view = activity.getWindow().getDecorView();
		// 开启绘图缓存
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bitmap, 0, 0, width, height);
		// 清除内存
		view.destroyDrawingCache();
		return bp;
	}

	/**
	 * 获取屏幕当前的截图,不包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity) {
		// decorView是window中的最顶层view，可以从window中获取到decorView
		View view = activity.getWindow().getDecorView();
		// 开启绘图缓存
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		Rect frame = new Rect();
		// 这里得到的是除了系统自带显示区域之外的所有区域，这里就是除了最上面的一条显示电量的状态栏之外的所有区域
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		// 这里便可以得到状态栏的高度，即最上面一条显示电量，信号等(状态栏高度)
		int statusHeihgt = frame.top;
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bitmap, 0, 0, width, height - statusHeihgt);
		return bp;
	}

}
