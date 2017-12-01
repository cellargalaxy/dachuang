package top.cellargalaxy.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import top.cellargalaxy.controlor.CodingFilter;

/**
 * Created by cellargalaxy on 17-11-14.
 */
public class ResultPost {
	private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();
	
	public static final boolean postResult(String resultPostUrl,JSONObject jsonObject){
		HttpPost httpPost = new HttpPost(resultPostUrl);
//		httpPost.addHeader("Content-Type", "application/json;charset=" + CodingFilter.getCoding());//也不要json头
		StringEntity stringEntity = new StringEntity(jsonObject.toString(), CodingFilter.getCoding());
//		stringEntity.setContentEncoding(CodingFilter.getCoding());//要不设字符
		stringEntity.setContentType("application/json");
		httpPost.setEntity(stringEntity);
		String result = getHttpRequestBaseResult(httpPost);
		if (result == null) {
			return false;
		}
		try{
			JSONObject object=new JSONObject(result);
			if (object.getInt("status")==0) {
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static final String getHttpRequestBaseResult(HttpRequestBase httpRequestBase) {
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			httpRequestBase.setConfig(requestConfig);
			HttpResponse httpResponse = HTTP_CLIENT.execute(httpRequestBase);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				return EntityUtils.toString(entity, CodingFilter.getCoding());
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
