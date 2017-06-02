package com.sinosafe.base.util;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.sinosafe.base.util.NetUtil;

public class NetUtilTest {

	@Test
	public void testHttpSendStringStringString() throws Exception {
		/**
		 * 项目启动后可执行成功
		 */
		String json=NetUtil.httpSend("http://localhost:8080/java_web_demo/app/queryCity?countryCode=CHN", NetUtil.TEXT_FORMAT_PLAIN, "");
		assertTrue(json.contains("Changsha"));
	}
	
	@Test
	public void testHttpSSLSendStringStringString() throws Exception {
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId","otESQt7it5mXjdGKyth6wpATeB3Q");
        String userInfo = NetUtil.httpSSLSend("https://test.sinosafe.com.cn/usercenter/weixinBound/findUserInfoByopenId", NetUtil.TEXT_FORMAT_PLAIN, jsonObject.toJSONString());
        System.out.println(userInfo);
        assertTrue(StringUtils.isNotBlank(userInfo));
	}

}
