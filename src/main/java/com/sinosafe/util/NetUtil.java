package com.sinosafe.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
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

		HttpClient client = new HttpClient();


        PostMethod postMethod = new PostMethod(url);
		RequestEntity entity = null;
		if (TEXT_FORMAT_PLAIN.equals(textFormat)) {
			entity = new StringRequestEntity(xml, "text/plain", "UTF-8");
			postMethod.setRequestEntity(entity);
		} else if (TEXT_FORMAT_XML.equals(textFormat)) {
			entity = new StringRequestEntity(xml, "text/xml", "UTF-8");
			postMethod.setRequestEntity(entity);
		} else if (TEXT_FORMAT_GBK.equals(textFormat)) {
			entity = new StringRequestEntity(xml, "text/xml", "GBK");
			postMethod.setRequestEntity(entity);
		} else if (TEXT_FORMAT_JSON.equals(textFormat)) {
			entity = new StringRequestEntity(xml, "text/json", "UTF-8");
			postMethod.setRequestEntity(entity);
		} else {
			entity = new StringRequestEntity(xml, "text/xml", "UTF-8");
			postMethod.setRequestEntity(entity);
		}
		String result = null;
		BufferedReader in = null;
		try {

			int statusCode = client.executeMethod(postMethod);
			StringBuffer resultBuffer = new StringBuffer();
			if (statusCode == HttpStatus.SC_OK) {
				in = new BufferedReader(new InputStreamReader(
						postMethod.getResponseBodyAsStream(),
						postMethod.getResponseCharSet()));
				String inputLine = null;
				while ((inputLine = in.readLine()) != null) {
					resultBuffer.append(inputLine);
				}
				if (resultBuffer != null) {
					try {
						if (TEXT_FORMAT_GBK.equals(textFormat)) {
							result = new String(resultBuffer.toString().getBytes(postMethod.getResponseCharSet()),"GBK");
						} else {
							result = new String(resultBuffer.toString().getBytes(postMethod.getResponseCharSet()),"UTF-8");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("statusCode != HttpStatus.SC_OK--------"+ statusCode);
			}
		} catch (HttpException e) {
			System.out.println("Fatal protocol violation（协议违反）: "+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Fatal transport error（传输错误）: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				System.out.println("Fatal transport error（数据流关闭）: "+ e.getMessage());
				e.printStackTrace();
			}
			postMethod.releaseConnection();
			((SimpleHttpConnectionManager) client.getHttpConnectionManager())
					.shutdown();
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
		HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
		RequestEntity entity = new InputStreamRequestEntity(new ByteArrayInputStream(content));
		postMethod.setRequestEntity(entity);
		String result = null;
		BufferedReader in = null;
		try {
			int statusCode = client.executeMethod(postMethod);
			StringBuffer resultBuffer = new StringBuffer();
			if (statusCode == HttpStatus.SC_OK) {
				in = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(),postMethod.getResponseCharSet()));
				String inputLine = null;
				while ((inputLine = in.readLine()) != null) {
					resultBuffer.append(inputLine);
				}
				if (resultBuffer != null) {
					try {
						result = new String(resultBuffer.toString().getBytes(postMethod.getResponseCharSet()),"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("statusCode != HttpStatus.SC_OK--------"+ statusCode);
			}
		} catch (HttpException e) {
			System.out.println("Fatal protocol violation（协议违反）: "+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Fatal transport error（传输错误）: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				System.out.println("Fatal transport error（数据流关闭）: "+ e.getMessage());
				e.printStackTrace();
			}
			postMethod.releaseConnection();
			((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
		}
		return result;
	}
	
	
}
