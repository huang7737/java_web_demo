package com.sinosafe.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {
    /** 
     * 验证用户名和密码 
     * @param String username,String password
     * @return 
     */  
    @RequestMapping(value="/login.do",method=RequestMethod.POST)  
    public String checkLogin(HttpServletRequest request,String userName,String password) {  
        String pswdMd5=null;
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(userName,password);  
	        Subject currentUser = SecurityUtils.getSubject();  
	        if (!currentUser.isAuthenticated()){
	            //使用shiro来验证  
	            currentUser.login(token);//验证角色和权限  
	        }
	        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
	        // 获取保存的URL
	        if (savedRequest == null || savedRequest.getRequestUrl() == null) {
	            return "redirect:/admin/index.html";
	        }else{
	        	return "redirect:"+savedRequest.getRequestUrl().replaceAll(request.getContextPath(), "");
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/login/login.html";
		}
        
    }

    /** 
     * 退出登录
     */  
    @RequestMapping(value="/logout.do",method=RequestMethod.POST)    
    @ResponseBody    
    public String logout() {
        Subject currentUser = SecurityUtils.getSubject();       
        currentUser.logout();    
        return "redirect:/login/login.html";
    }
}