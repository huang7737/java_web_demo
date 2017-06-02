package com.sinosafe.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosafe.app.service.CommonApiService;

@Controller
@RequestMapping("/app")
public class CommonController {
	
	@Resource
	private CommonApiService commonApiService;
	
	@Value("${app.syscode}")
	private String sysCode;
	
	@RequestMapping("/queryCity")
	@ResponseBody
	public  Map<String, Object> test(HttpServletRequest request,HttpServletResponse response,@RequestParam("countryCode")String countryCode) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> data = commonApiService.queryList(countryCode);
			resultMap.put("resultCode", "0000");
			resultMap.put("resultMsg", "成功");
			resultMap.put("sysCode", sysCode);
			resultMap.put("dataList", data);
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCode", "0000");
			resultMap.put("resultMsg", "系统异常");
		}
		return resultMap;
	}
}
