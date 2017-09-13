package com.sinosafe.base.pool;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * Created by wkzq on 2017-8-15.
 */
public class SocketPoolUtil {
    private static ObjectPool pool=null;

    static {
        PoolableObjectFactory factory = new SocketPoolFactory();
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        try {
            Properties props = PropertiesLoaderUtils.loadAllProperties("pool/pool.properties");
            //链接池中最大连接数
            int maxActive=Integer.parseInt(props.getProperty("kdgegw.maxActive"));
            config.maxActive = maxActive;
            //代表每次检查链接的数量，建议设置和maxActive一样大，这样每次可以有效检查所有的链接
            config.numTestsPerEvictionRun=maxActive;
            //链接池中最大空闲的连接数
            config.maxIdle = Integer.parseInt(props.getProperty("kdgegw.maxIdle"));
            //连接池中最少空闲的连接数
            config.minIdle = Integer.parseInt(props.getProperty("kdgegw.minIdle"));
            //当连接池资源耗尽时，调用者最大阻塞的时间，超时将跑出异常。
            config.maxWait = Long.parseLong(props.getProperty("kdgegw.maxWait"));
            //向调用者输出“链接”对象时，是否检测它的空闲超时；
            config.testWhileIdle = Boolean.parseBoolean(props.getProperty("kdgegw.testWhileIdle"));
            //连接空闲的最小时间，达到此值后空闲连接将可能会被移除。
            config.minEvictableIdleTimeMillis = Long.parseLong(props.getProperty("kdgegw.minEvictableIdleTimeMillis"));
            //“空闲链接”检测线程，检测的周期，毫秒数。
            config.timeBetweenEvictionRunsMillis = Long.parseLong(props.getProperty("kdgegw.timeBetweenEvictionRunsMillis"));
            pool = new GenericObjectPool(factory, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ObjectPool getPool(){
        return pool;
    }
    
    public static void main(String[] args){
    	SocketPoolUtil.getPool();
    }
}
