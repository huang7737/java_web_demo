package com.sinosafe.app.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sinosafe.base.dao.CommonDao;

@Service("commonApiServiceImpl")
public class CommonApiServiceImpl implements CommonApiService {
	@Resource
    private CommonDao dao;

	@Override
	@Cacheable(value="springCache",key="'citylist_' + #countryCode")
	public List<Map<String, Object>> queryList(String countryCode) {
		return dao.selectList("com.sinosafe.demo.findCityList", countryCode);
	}
	
}
