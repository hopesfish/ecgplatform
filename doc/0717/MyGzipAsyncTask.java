package com.mine.utils.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.aragoncg.apps.zmkm.activitys.BaseActivity;
import com.mine.requests.AbstractRequest;
import com.mine.utils.StringUtils;
import com.mine.utils.exception.ErrorMsg;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class MyGzipAsyncTask extends
		AsyncTask<AbstractRequest, Object, AbstractRequest> {
	private static String tag = "MyAsyncTask";
	private BaseActivity.OnUICallback callback;
	private Activity activity;

	public MyGzipAsyncTask(Activity activity, BaseActivity.OnUICallback callback) {
		this.callback = callback;
		this.activity = activity;
	}

	@Override
	protected AbstractRequest doInBackground(AbstractRequest... params) {
		AbstractRequest request = (AbstractRequest) params[0];
		if (request == null)
			return null;
		String url = request.getUrl();
		if (url == null)
			return null;
		Map<String, String> requestValues = request.getRequestValues();
		if (requestValues != null) {
			String result;
			
			result = getRequest(url, requestValues, activity);
			try {
				request.read(result);
			} catch (ErrorMsg e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			String jsonContent = request.getJsonContent();
			boolean isPost = true;
			if (jsonContent == null || jsonContent.trim().length() == 0) {
				isPost = false;
			}
			String result;
			try {
				result = getRequest(url, jsonContent, activity, isPost);
				request.read(result);
			} catch (ErrorMsg e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return request;
	}

	@Override
	protected void onPostExecute(AbstractRequest result) {
		callback.onGetResult(result);
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		int progress = (Integer) values[0];
		String desc = (String) values[1];
		callback.updateProgress(progress, desc);
	}

	public String getRequest(String uriAPI, String post, Context ctx,
			boolean isPost) throws Exception {
		String strResult;
		DefaultHttpClient httpclient = null;
		StringEntity entity;
		try {
			httpclient = buildClient(ctx);
			URL url = new URL(uriAPI);
			int port = url.getPort();
			if (port == -1)
				port = 80;
			URI uri = new URI(url.getProtocol(), url.getHost() + ":" + port,
					url.getPath(), url.getQuery(), null);
			HttpResponse rsp = null;
			if (isPost) {
				HttpPost req = null;
				req = new HttpPost(uri);
				req.addHeader("Accept-Encoding", "gzip");
				entity = new StringEntity(post, "utf-8");
				req.setEntity(entity);
				rsp = httpclient.execute(req);
			} else {
				HttpGet req = null;
				req = new HttpGet(uri);
				rsp = httpclient.execute(req);
			}

			if (rsp.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(rsp.getEntity());
				return strResult.trim();
			} else {
				return "Error Response: " + rsp.getStatusLine().toString();
			}
		} finally {
			if (httpclient != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
	}

	public  byte[] compress(String str)  {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = null;
		GZIPOutputStream gzip = null;
		byte[] byteArray = null;
		try {
			out = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());
			gzip.flush();
			gzip.finish();
			byteArray = out.toByteArray();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null != gzip) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return byteArray;
	}

	public String getRequest(String uriAPI, Map<String, String> requestValues,
			Context ctx)  {
		if (uriAPI == null || "".equals(uriAPI)) {
			return "";
		}
		String strResult;
		DefaultHttpClient httpclient = null;
		try {
			httpclient = buildClient(ctx);
			HttpParams params = new BasicHttpParams();
			// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
			
			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
			HttpConnectionParams.setSoTimeout(params, 20 * 1000);
			httpclient.setParams(params);
			
			URL url = new URL(uriAPI);
			int port = url.getPort();
			if (port == -1)
				port = 80;
			URI uri = new URI(url.getProtocol(), url.getHost() + ":" + port,
					url.getPath(), url.getQuery(), null);
			HttpResponse rsp = null;

			HttpPost req = null;
			JSONObject jsonObject = new JSONObject(); 
			for (Map.Entry<String, String> entry : requestValues.entrySet()) {
				jsonObject.put(entry.getKey(),entry.getValue());
			}
			req = new HttpPost(uri);
			
			req.addHeader("Accept-Encoding", "gzip");
			byte[] byteArray = compress(jsonObject.toString());
			if(null != byteArray) {
				req.setEntity(new ByteArrayEntity(byteArray));
				rsp = httpclient.execute(req);
				
				if (rsp.getStatusLine().getStatusCode() == 200) {
					strResult = EntityUtils.toString(rsp.getEntity());
					return strResult.trim();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (httpclient != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
		return null;
	}

	private DefaultHttpClient buildClient(Context ctx) {

		HttpHost proxy = null;
		APNType apnType = getCurrentUsedAPNType(ctx);
		if (APNType.CTWAP.equals(apnType)) {
			proxy = new HttpHost(CTPROXY, Integer.parseInt(PROT));
		} else if (APNType.CMWAP.equals(apnType)) {
			proxy = new HttpHost(CMPROXY, Integer.parseInt(PROT));
		}
		// else if (APNType._3GWAP.equals(apnType)){//��ͨ
		// if(paramProxy == null){
		// paramProxy = CMPROXY;
		// }
		// proxy = new HttpHost(CMPROXY, Integer.parseInt(PROT));
		// proxy = new HttpHost(CTPROXY.replace("http://", ""), 80);
		// }
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 1000 * 10;

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 1000 * 10;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		if (!testWifi(ctx)) {
			if (proxy != null) {
				httpclient.getParams().setParameter(
						ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
		}

		return httpclient;
	}

	public static boolean testWifi(Context ctx) {
		ConnectivityManager mConnectivity = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			Log.d(tag, "!mConnectivity.getBackgroundDataSetting()"
					+ mConnectivity.getBackgroundDataSetting());
			return false;
		}
		int netType = info.getType();
		int netSubType = info.getSubtype();
		Log.d(tag, "netType:" + netType + " netSubType:" + netSubType);
		if (ConnectivityManager.TYPE_WIFI == netType) {
			Log.d(tag, "TYPE_WIFI");
			return info.isConnectedOrConnecting();
		}
		return false;
	}

	public enum APNType {
		CMWAP, CMNET, Unknow, CTWAP, CTNET, _3GNET, _3GWAP;
	}

	public static final String CMPROXY = "10.0.0.172";
	public static final String CTPROXY = "10.0.0.200";
	public static final String PROT = "80";
	public static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	// ��ȡ���������
	public APNType getCurrentUsedAPNType(Context ctx) {
		Log.d(tag, "getCurrentUsedAPNType");
		try {
			ContentResolver cr = ctx.getContentResolver();
			Cursor cursor = cr.query(PREFERRED_APN_URI, new String[] { "_id",
					"name", "apn", "proxy", "port" }, null, null, null);

			cursor.moveToFirst();
			if (cursor.isAfterLast()) {
				return APNType.Unknow;
			}
			String id = cursor.getString(0);
			String name = cursor.getString(1);
			String apn = cursor.getString(2);
			String proxy = cursor.getString(3);
			String _prot = cursor.getString(4);
			if (!StringUtils.isEmpty(proxy)) {
				String proxyAddress = proxy;
			}
			if (!StringUtils.isEmpty(_prot)) {
				String proxyProt = _prot;
			}
			Log.d(tag, id + " proxy:" + proxy + " prot:" + _prot + " apn:"
					+ apn);
			cursor.close();
			if (// "1".equals(id) || //���ǵ���1Ϊctwap,2Ϊctnet
			(("CTWAP".equals(apn.toUpperCase()) || "CTWAP".equals(name
					.toUpperCase())) && !StringUtils.isEmpty(proxy) && !StringUtils
						.isEmpty(_prot)))
				return APNType.CTWAP;
			else if (// "2".equals(id) ||
			(("CTNET".equals(apn.toUpperCase()) || "CTNET".equals(name
					.toUpperCase())) && StringUtils.isEmpty(proxy) && StringUtils
						.isEmpty(_prot)))
				return APNType.CTNET;
			else if (("CMWAP".equals(apn.toUpperCase()) || "CMWAP".equals(name
					.toUpperCase()))
					&& !StringUtils.isEmpty(proxy)
					&& !StringUtils.isEmpty(_prot))
				return APNType.CMWAP;
			else if (("CMNET".equals(apn.toUpperCase()) || "CMNET".equals(name
					.toUpperCase()))
					&& StringUtils.isEmpty(proxy)
					&& StringUtils.isEmpty(_prot))
				return APNType.CMNET;
			else if (("3GWAP".equals(apn.toUpperCase()) || "3GWAP".equals(name
					.toUpperCase()))
					&& !StringUtils.isEmpty(proxy)
					&& !StringUtils.isEmpty(_prot))
				return APNType._3GWAP;
			else if (("3GNET".equals(apn.toUpperCase()) || "3GNET".equals(name
					.toUpperCase()))
					&& StringUtils.isEmpty(proxy)
					&& StringUtils.isEmpty(_prot))
				return APNType._3GNET;
			else
				return APNType.Unknow;
		} catch (Exception ep) {
			Log.e(tag, "getCurrentUsedAPNTypeException");
			return APNType.Unknow;
		}
	}

}
