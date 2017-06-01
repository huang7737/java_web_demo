package com.sinosafe.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;

import com.sinosafe.util.http.HttpHelper;
import com.sinosafe.util.http.HttpHelper.Response;


/**
 * 网络通信相关工具类，比如http发送
 * @author huangping5
 *
 */
public class NetUtil {
	/**
	 * 文本格式为 plain
	 */
	public static final String TEXT_FORMAT_PLAIN = "1";
	/**
	 * 文本格式为 xml
	 */
	public static final String TEXT_FORMAT_XML = "2";
	/**
	 * 文本格式为 xml
	 */
	public static final String TEXT_FORMAT_GBK = "3";
    /**
     *json格式
     */
    public static final String TEXT_FORMAT_JSON = "4";
	
	
	
    public static String httpSSLSend(String url, String textFormat, String  json) {
        String result = null;
        try {

            Response sslres = HttpHelper.connect(url).post(json);
            int statusCode = sslres.statusCode();
            if (statusCode == HttpStatus.SC_OK) {
                result = sslres.html();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
	public static String httpSend(String url, String textFormat, String xml)
			throws DocumentException, UnsupportedEncodingException {

		//创建HttpClientBuilder  
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
        //HttpClient  
        CloseableHttpClient client = httpClientBuilder.build();

        HttpPost  postMethod = new HttpPost(url);
		HttpEntity entity = null;
		if (TEXT_FORMAT_PLAIN.equals(textFormat)) {
			entity = new StringEntity(xml,ContentType.create("text/plain", "UTF-8"));
			postMethod.setEntity(entity);
		} else if (TEXT_FORMAT_XML.equals(textFormat)) {
			entity = new StringEntity(xml, ContentType.create("text/xml", "UTF-8"));
			postMethod.setEntity(entity);
		} else if (TEXT_FORMAT_GBK.equals(textFormat)) {
			entity = new StringEntity(xml, ContentType.create("text/xml", "GBK"));
			postMethod.setEntity(entity);
		} else if (TEXT_FORMAT_JSON.equals(textFormat)) {
			entity = new StringEntity(xml, ContentType.create("text/json", "UTF-8"));
			postMethod.setEntity(entity);
		} else {
			entity = new StringEntity(xml, ContentType.create("text/xml", "UTF-8"));
			postMethod.setEntity(entity);
		}
		String result = null;
		try {

			CloseableHttpResponse response = client.execute(postMethod);
			int statusCode=response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				try {
					HttpEntity resEntity = response.getEntity();  
	                if (entity != null) {  
						if (TEXT_FORMAT_GBK.equals(textFormat)) {
							result = EntityUtils.toString(resEntity, "GBK");
						} else {
							result = EntityUtils.toString(resEntity, "UTF-8");
						}
	                }
                }finally {  
                    response.close();  
                } 
			} else {
				System.out.println("statusCode != HttpStatus.SC_OK--------"+ statusCode);
			}
		} catch (IOException e) {
			System.out.println("Fatal transport error（传输错误）: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源    
            try {  
            	client.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
		}
		return result;
	}
	
	/**
	 * 发送二进制数据
	 * @param url
	 * @param content
	 * @return
	 * @throws DocumentException
	 * @throws UnsupportedEncodingException
	 */
	public static String httpSend(String url, byte[] content)
			throws DocumentException, UnsupportedEncodingException {
		//创建HttpClientBuilder  
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
        //HttpClient  
        CloseableHttpClient client = httpClientBuilder.build();
        
        HttpPost  postMethod = new HttpPost(url);
		HttpEntity entity = new ByteArrayEntity(content);
		postMethod.setEntity(entity);
		String result = null;
		try {
			CloseableHttpResponse response  = client.execute(postMethod);
			int statusCode=response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				try {
					HttpEntity resEntity = response.getEntity(); 
	                if (entity != null) {  
						result = EntityUtils.toString(resEntity, "UTF-8");
	                }
                }finally {  
                    response.close();  
                } 
			} else {
				System.out.println("statusCode != HttpStatus.SC_OK--------"+ statusCode);
			}
		} catch (IOException e) {
			System.out.println("Fatal transport error（传输错误）: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源    
            try {  
            	client.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
		}
		return result;
	}
	
	
}
