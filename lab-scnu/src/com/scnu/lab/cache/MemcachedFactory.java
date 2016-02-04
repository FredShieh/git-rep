package com.scnu.lab.cache;

import org.apache.log4j.Logger;

import com.danga.MemCached.*;
import com.scnu.lab.general.utils.ConfigUtil;

/**
 * memcached缓存相关
 * @author fred-
 *
 */
public class MemcachedFactory {
	
	private static Logger logger=Logger.getLogger(MemcachedFactory.class);
	private static MemCachedClient client=new MemCachedClient();
	private MemcachedFactory(){
		
	}
	static{
		logger.info("memcached缓存初始化。。。。");
		//获取连接池的实例
        SockIOPool pool = SockIOPool.getInstance();
         
        //服务器列表及其权重
        String server=ConfigUtil.readPropertyValue("memcachedServer");
        String[] servers = {server};
        logger.info("memcached初始化地址："+server);
        Integer[] weights = {3};
         
        //设置服务器信息
        pool.setServers(servers);
        pool.setWeights(weights);
         
        //设置初始连接数、最小连接数、最大连接数、最大处理时间
        pool.setInitConn(10);
        pool.setMinConn(10);
        pool.setMaxConn(1000);
        pool.setMaxIdle(1000*60*60);
         
        //设置连接池守护线程的睡眠时间
        pool.setMaintSleep(60);
         
        //设置TCP参数，连接超时
        pool.setNagle(false);
        pool.setSocketTO(60);
        pool.setSocketConnectTO(0);
         
        //初始化并启动连接池
        pool.initialize();
        logger.info("memcached初始化完毕。。。");
	}
	public void init(){
		
	}
	public static boolean add(String key,Object value){
		return client.add(key, value);
	}
	
	public static boolean add(String key,Object value,Integer expire){
		return client.add(key, value, expire);
	}
	
	public static boolean set(String key,Object value){
		return client.set(key, value);
	}
	public static boolean set(String key, Object value, Integer expire) {
        return client.set(key, value, expire);
    }
	public static boolean replace(String key, Object value) {
        return client.replace(key, value);
    }
	public static boolean replace(String key, Object value, Integer expire) {
        return client.replace(key, value, expire);
    }
	public static Object get(String key) {
        return client.get(key);
    }
}
