package com.sinosafe.base.security;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.validator.html.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ��ƽ on 2017-9-13.
 */

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static Policy policy = null;
    static {
        try {
            InputStream is=XssHttpServletRequestWrapper.class.getClassLoader().getResourceAsStream("policy/antisamy.xml");
            policy=Policy.getInstance(is);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values==null)  {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    public Map getParameterMap() {
        Map paramMap= super.getParameterMap();
        Map resultMap = new HashMap();
        try{
            Set<String> keySet=paramMap.keySet();
            for(String key:keySet){
                String[] values = (String[]) paramMap.get(key);
                if(values!=null&&values.length>0){
                    for(int i=0;i<values.length;i++){
                        values[i]=cleanXSS(values[i]);
                    }
                    resultMap.put(key,values);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultMap;
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        AntiSamy antiSamy = new AntiSamy();
        try {
            final CleanResults cr = antiSamy.scan(value, policy, AntiSamy.SAX);
            value= cr.getCleanHTML();
        } catch (ScanException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        value= StringEscapeUtils.escapeSql(value);
        return value;
    }


}
