package com.sinosafe.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;


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
	
	
	/**
	 * https方式
	 * @param url
	 * @param textFormat
	 * @param data
	 * @return
	 */
    public static String httpSSLSend(String url, String textFormat, String  data) {
    	String result = null;
    	//HttpClient
    	CloseableHttpClient client=null;
    	try {
	        client = buildSSLCloseableHttpClient();
	    	HttpPost  postMethod = new HttpPost(url);
			HttpEntity entity = null;
			entity = createEnity(textFormat, data);
			postMethod.setEntity(entity);
		
			CloseableHttpResponse response = client.execute(postMethod);
			result = getResponse(textFormat, entity, result, response);
		} catch (Exception e) {
			System.out.println("Fatal transport error（传输错误）: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeClient(client);  
		}
		return result;
    }

    /**
     * http方式
     * @param url
     * @param textFormat
     * @param data
     * @return
     * @throws DocumentException
     * @throws UnsupportedEncodingException
     */
	public static String httpSend(String url, String textFormat, String data)
			throws DocumentException, UnsupportedEncodingException {
        //HttpClient  
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost  postMethod = new HttpPost(url);
		HttpEntity entity = null;
		entity = createEnity(textFormat, data);
		postMethod.setEntity(entity);
		String result = null;
		try {

			CloseableHttpResponse response = client.execute(postMethod);
			result = getResponse(textFormat, entity, result, response);
		} catch (IOException e) {
			System.out.println("Fatal transport error（传输错误）: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeClient(client);  
		}
		return result;
	}

	private static String getResponse(String textFormat, HttpEntity entity, String result,
			CloseableHttpResponse response) throws IOException {
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
        //HttpClient  
        CloseableHttpClient client = HttpClients.createDefault();
        
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
			closeClient(client);  
		}
		return result;
	}
	
	private static void closeClient(CloseableHttpClient client) {
		// 关闭连接,释放资源    
		try {
			client.close();  
		} catch (IOException e) {  
		    e.printStackTrace();  
		}
	}
	
	private static HttpEntity createEnity(String textFormat, String data) {
		HttpEntity entity;
		if (TEXT_FORMAT_PLAIN.equals(textFormat)) {
			entity = new StringEntity(data,ContentType.create("text/plain", "UTF-8"));
		} else if (TEXT_FORMAT_XML.equals(textFormat)) {
			entity = new StringEntity(data, ContentType.create("text/xml", "UTF-8"));
		} else if (TEXT_FORMAT_GBK.equals(textFormat)) {
			entity = new StringEntity(data, ContentType.create("text/xml", "GBK"));
		} else if (TEXT_FORMAT_JSON.equals(textFormat)) {
			entity = new StringEntity(data, ContentType.create("text/json", "UTF-8"));
		} else {
			entity = new StringEntity(data, ContentType.create("text/xml", "UTF-8"));
			
		}
		return entity;
	}
    
    private static CloseableHttpClient buildSSLCloseableHttpClient() throws Exception {  
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,  
                new TrustStrategy() {  
                    // 信任所有  
                    public boolean isTrusted(X509Certificate[] chain,  
                            String authType) throws CertificateException {  
                        return true;  
                    }  
                }).build();  
        // ALLOW_ALL_HOSTNAME_VERIFIER:这个主机名验证器基本上是关闭主机名验证的,实现的是一个空操作，并且不会抛出javax.net.ssl.SSLException异常。  
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(  
                sslContext, new String[] { "TLSv1" }, null,  
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();  
    }
	
	
}
