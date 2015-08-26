
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author anumbrella
 * 
 * @date 2015-8-26 上午10:25:36
 * 
 *       Http请求工具类
 */
public class HttpUtils {

	/**
	 * 设置请求超时的时间
	 */
	private static final int TIME_OUT = 5000;

	/**
	 * 
	 * 异步Get请求
	 * 
	 * @param url
	 *            url地址
	 * @param callBack
	 *            回调函数
	 */
	public static void doGetAsyn(final String url, final CallBack callBack) {

		new Thread() {
			@Override
			public void run() {
				try {
					String result = doGet(url);
					if (callBack != null) {
						callBack.onRequestComplete(result);
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * 异步Post请求
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 * @throws Exception
	 */
	public static void doPostAsyn(final String url, final String params,
			final CallBack callBack) throws Exception {
		new Thread() {
			@Override
			public void run() {

				try {
					String result = doPost(url, params);
					if (callBack != null) {
						callBack.onRequestComplete(result);
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}.start();

	}

	/**
	 * Get请求,获得返回数据
	 * 
	 * @param urlStr
	 * @return
	 */
	public static String doGet(String urlStr) {
		URL url = null;
		HttpURLConnection connection = null;
		InputStream inStream = null;
		ByteArrayOutputStream baos = null;

		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			// 设置超时
			connection.setReadTimeout(TIME_OUT);

			// http头部属性设置
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");

			// 正确响应
			if (connection.getResponseCode() == 200) {
				inStream = connection.getInputStream();
				baos = new ByteArrayOutputStream();

				int length = -1;
				// 每次缓存数组大小，读取大小
				byte[] b = new byte[128];

				while ((length = inStream.read(b)) != -1) {
					baos.write(b, 0, length);
				}
				baos.flush();
				return baos.toString();
			} else {
				throw new RuntimeException("responseCode is not 200 ...");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (inStream != null) {
					inStream.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 销毁连接
			connection.disconnect();
		}

		return null;
	}

	/**
	 * 向指定url发送POST方法的请求
	 * 
	 * @param url
	 * @param param
	 * @return
	 */
	public static String doPost(String url, String params) {

		// 定义文件的输出流
		PrintWriter out = null;
		BufferedReader in = null;
		String result = " ";
		try {
			URL resultUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) resultUrl
					.openConnection();
			// 设置HTTP的通用的属性设置
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			// 关闭缓存
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// 设置超时
			connection.setReadTimeout(TIME_OUT);
			connection.setConnectTimeout(TIME_OUT);
			if (params != null && !params.trim().equals("")) {
				// 获得http的网络输出流
				out = new PrintWriter(connection.getOutputStream());
				// 发送HTTP的post请求
				out.print(params);
				out.flush();
			}

			// 获取HTTP的输入流
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				result += line;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		return result;
	}

	/**
	 * 回调函数
	 */
	public interface CallBack {

		public void onRequestComplete(String result);

	}

}
