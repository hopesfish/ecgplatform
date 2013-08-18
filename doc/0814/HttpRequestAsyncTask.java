package com.lcworld.healthbutler.framework.network;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.lcworld.healthbutler.application.SoftApplication;
import com.lcworld.healthbutler.constant.Constants;
import com.lcworld.healthbutler.framework.network.ServerInterfaceDefinition.RequestMethod;
import com.lcworld.healthbutler.util.LogUtil;
import com.lcworld.healthbutler.util.StringUtil;

public class HttpRequestAsyncTask extends AsyncTask<Request, Void, Object> {
	private static final int RESPONSE_TIME_OUT = 60000;
	private static final int REQUEST_TIME_OUT = 60000;

	private String resultString;
	private OnCompleteListener onCompleteListener;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	private HttpClient buildHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		// 请求超时
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIME_OUT);
		// 读取超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				RESPONSE_TIME_OUT);

		return httpClient;
	}

	@Override
	protected Object doInBackground(Request... params) {

		Object object = null;
		try {
			HttpClient httpClient = buildHttpClient();
			Request request = params[0];

			/**
			 * 获取BaseUrl
			 */
			String urlString = SoftApplication.softApplication.getAppInfo().serverAddress;
			String opt = request.getServerInterfaceDefinition().getOpt();
			urlString += opt;
			HttpResponse httpResponse;
			HttpGet httpGet = null;
			HttpPost httpPost = null;
			HttpPut httpPut = null;
			HttpDelete httpDelete = null;
			

			/**
			 * GET请求方式
			 */
			if (RequestMethod.GET.equals(request.getServerInterfaceDefinition()
					.getRequestMethod())) {
				StringBuffer stringBuffer = new StringBuffer(urlString + "?");
				for (Map.Entry<String, String> entry : request.getParamsMap()
						.entrySet()) {
					if("opt".equals(entry.getKey()))
						continue;
					stringBuffer.append(entry.getKey());
					stringBuffer.append('=');
					stringBuffer.append(entry.getValue());
					stringBuffer.append('&');
					LogUtil.log("参数：" + entry.getKey() + "值："
							+ entry.getValue());
				}
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
				LogUtil.log("GET:" + stringBuffer.toString());
				httpGet = new HttpGet(stringBuffer.toString());
				httpGet.setHeader("Authorization", Constants.token);
				httpResponse = httpClient.execute(httpGet);
			}
			/**
			 * PUT请求
			 */
			else if(RequestMethod.PUT.equals(request.getServerInterfaceDefinition()
					.getRequestMethod())){
				httpPut = new HttpPut(urlString);
				httpPut.setHeader("Authorization", Constants.token);
				LogUtil.log(urlString);

				ArrayList<BasicNameValuePair> localArrayList = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> entry : request.getParamsMap()
						.entrySet()) {
					localArrayList.add(new BasicNameValuePair(entry.getKey(),
							entry.getValue()));

					LogUtil.log("参数：" + entry.getKey() + "值："
							+ entry.getValue());

				}
				httpPut.setEntity(new UrlEncodedFormEntity(localArrayList,
						"UTF-8"));
				httpResponse = httpClient.execute(httpPut);
			} 
			/**
			 * DELETE请求
			 */
			else if(RequestMethod.DELETE.equals(request.getServerInterfaceDefinition()
					.getRequestMethod())){
				httpDelete = new HttpDelete(urlString);
				httpDelete.setHeader("Authorization", Constants.token);
				LogUtil.log(urlString);
				httpResponse = httpClient.execute(httpDelete);
			} 
			
			
			
			else {
				/**
				 * POST请求方式
				 */

				httpPost = new HttpPost(urlString);
				if(StringUtil.isNotNull(Constants.token))
					httpPost.setHeader("Authorization", Constants.token);
				LogUtil.log(urlString);

				ArrayList<BasicNameValuePair> localArrayList = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> entry : request.getParamsMap()
						.entrySet()) {
					localArrayList.add(new BasicNameValuePair(entry.getKey(),
							entry.getValue()));

					LogUtil.log("POST参数：" + entry.getKey() + "值："
							+ entry.getValue());

				}
				httpPost.setEntity(new UrlEncodedFormEntity(localArrayList,
						"UTF-8"));
				httpResponse = httpClient.execute(httpPost);
			}

			if (httpResponse.getStatusLine().getStatusCode() != 200  && httpResponse.getStatusLine().getStatusCode() != 201) {
				LogUtil.log("返回getStatusCode="
						+ httpResponse.getStatusLine().getStatusCode());
				
				JSONObject obj = new JSONObject();
				obj.put("errCode", httpResponse.getStatusLine().getStatusCode());
				obj.put("errMsg", EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8"));
				resultString = obj.toString();
				System.out.println("errMsg" + resultString);
				if (null != httpPost) {
					httpPost.abort();
				}
				if (null != httpGet) {
					httpGet.abort();
				}
				if (null != httpPut) {
					httpPut.abort();
				}
				if (null != httpDelete) {
					httpDelete.abort();
				}
				// 将来可能添加回调
			} else {
				resultString = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");
				LogUtil.log("返回result=" + resultString);

				try {
					File file = new File("/mnt/sdcard/123");
					if (!file.exists()) {
						file.mkdirs();
					}
					File file2 = new File("/mnt/sdcard/123", "123.txt");
					FileOutputStream outStream = new FileOutputStream(file2);
					outStream.write(resultString.getBytes());
					outStream.flush();
					outStream.close();
				} catch (Exception e) {
				}
				resultString = resultString == null ? "" :resultString;
				object = request.getJsonParser().parse(resultString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;

	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (null != onCompleteListener) {
			if (null == result && null == resultString) {
				onCompleteListener.onComplete(null, null);
			} else {
				onCompleteListener.onComplete(result, resultString);
			}
		}
	}

	public interface OnCompleteListener<T> {
		public void onComplete(T result, String resultString);
	}

	public OnCompleteListener getOnCompleteListener() {
		return onCompleteListener;
	}

	public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
		this.onCompleteListener = onCompleteListener;
	}
}
